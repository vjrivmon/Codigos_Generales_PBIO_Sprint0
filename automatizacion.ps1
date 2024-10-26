# Colores para los mensajes (PowerShell no tiene soporte nativo para colores como bash, pero se puede simular)
function print-message {
    param (
        [string]$color,
        [string]$message
    )
    Write-Host $message -ForegroundColor $color
}

# Variables
$TEMP_DIR = "tempdir"
$DOCKER_IMAGE = "sprint1_ngx"
$DOCKER_CONTAINER = "sprint1"
$PORT = 80 # En Windows no se utiliza
$DOCKERFILE_PATH = "Dockerfile" # En Windows no se utiliza

# Verificar si Docker está instalado
if (Get-Command docker -ErrorAction SilentlyContinue) {
    print-message "Green" "Docker está instalado."
} else {
    print-message "Red" "Docker no está instalado."
    exit 1
}

# Leer versión de la aplicación
if (Test-Path "Backend/src/nodejs/package.json") {
    $APP_VERSION = (Get-Content "Backend/src/nodejs/package.json" | ConvertFrom-Json).version
    print-message "Green" "Versión de la aplicación: $APP_VERSION"
} else {
    print-message "Red" "No se ha encontrado el archivo package.json"
    exit 1
}

# Eliminar contenedores e imágenes antiguas
print-message "Yellow" "Eliminando contenedores e imágenes antiguas..."
docker rm -f $DOCKER_CONTAINER
docker rmi -f "${DOCKER_IMAGE}:${APP_VERSION}"
docker rm -f (docker ps -a -q)
docker rmi -f (docker images -q)

# Crear la estructura de carpetas
print-message "Yellow" "Creando estructura de carpetas..."
New-Item -Path "$TEMP_DIR/Backend/src/nodejs/app_web" -ItemType Directory -Force
New-Item -Path "$TEMP_DIR/Backend/src/mariadb" -ItemType Directory -Force
Copy-Item "Backend/src/nodejs/app_web/Dockerfile" "$TEMP_DIR/Backend/src/nodejs/app_web"
Copy-Item -Recurse "Backend/src/nodejs/*" "$TEMP_DIR/Backend/src/nodejs" -Exclude "app_web", "node_modules"
Copy-Item -Recurse "Backend/src/mariadb/*" "$TEMP_DIR/Backend/src/mariadb"
Copy-Item -Recurse "Frontend/*" "$TEMP_DIR/Backend/src/nodejs/app_web"
Copy-Item "Backend/src/variables.env" "$TEMP_DIR/Backend/src"

# Asegúrate de copiar el archivo docker-compose.yml
if (Test-Path "Backend/src/docker-compose.yml") {
    Copy-Item "Backend/src/docker-compose.yml" "$TEMP_DIR/Backend/src/"
} else {
    print-message "Red" "No se ha encontrado el archivo docker-compose.yml"
    exit 1
}

print-message "Green" "Hecho!"

# Levantar contenedores con docker-compose
print-message "Yellow" "Levantando contenedores con docker-compose..."
if (Get-Command docker-compose -ErrorAction SilentlyContinue) {
    docker-compose -f "$TEMP_DIR/Backend/src/docker-compose.yml" up --build -d
} else {
    print-message "Red" "docker-compose no está instalado. Instalando docker-compose..."
    Invoke-WebRequest "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-Windows-x86_64.exe" -OutFile "$env:ProgramFiles\docker-compose.exe"
    $env:Path += ";$env:ProgramFiles"
    & "$env:ProgramFiles\docker-compose.exe" -f "$TEMP_DIR/Backend/src/docker-compose.yml" up --build -d
}

print-message "Green" "Contenedores levantados correctamente!"

# Ejecutar pruebas dentro del contenedor de la aplicación
print-message "Yellow" "Ejecutando pruebas dentro del contenedor..."
docker exec sprint1_njs npm test

# Esperar un momento para asegurarse de que los resultados de las pruebas se impriman completamente
Start-Sleep -Seconds 2

# Mostrar logs de los contenedores
print-message "Yellow" "Mostrando logs de los contenedores..."
docker-compose -f "$TEMP_DIR/Backend/src/docker-compose.yml" logs -f

# Limpieza del directorio temporal
print-message "Green" "Limpiando el directorio temporal..."
Remove-Item -Recurse -Force $TEMP_DIR
