# Colores para los mensajes (PowerShell no tiene soporte nativo para colores como bash, pero se puede simular)
function print-message {
    param (
        [string]$color,
        [string]$message,
        [string]$emoji
    )
    Write-Host "$emoji $message" -ForegroundColor $color
}

# Función para imprimir un banner de bienvenida
function print-banner {
    param (
        [string]$appName,
        [string]$version,
        [string]$author
    )
    Write-Host "" -ForegroundColor Cyan
    Write-Host "#######################################################################" -ForegroundColor Cyan
    Write-Host "        🚀 Bienvenido al despliegue de la App de $appName ! 🚀" -ForegroundColor Cyan
    Write-Host "#######################################################################" -ForegroundColor Cyan
    Write-Host "Aplicación: $appName" -ForegroundColor Green
    Write-Host "Versión: $version" -ForegroundColor Green
    Write-Host "Autor: $author" -ForegroundColor Green
    Write-Host "" -ForegroundColor Cyan
}

# Variables
$TEMP_DIR = "tempdir"
$DOCKER_IMAGE = "sprint3_ngx"
$DOCKER_CONTAINER = "sprint3"
$PORT = 80 # En Windows no se utiliza
$DOCKERFILE_PATH = "Dockerfile" # En Windows no se utiliza
$APP_NAME = "VIMYP"
$AUTHOR = "Vicente Rivas Monferrer"

# Verificar si Docker está instalado
if (Get-Command docker -ErrorAction SilentlyContinue) {
    print-message "Green" "Docker está instalado." "✅"
} else {
    print-message "Red" "Docker no está instalado." "❌"
    exit 1
}

# Leer versión de la aplicación
if (Test-Path "Backend/src/nodejs/package.json") {
    $APP_VERSION = (Get-Content "Backend/src/nodejs/package.json" | ConvertFrom-Json).version
    print-message "Green" "Versión de la aplicación: $APP_VERSION"
    print-banner -appName $APP_NAME -version $APP_VERSION -author $AUTHOR
} else {
    print-message "Red" "No se ha encontrado el archivo package.json" "❌"
    exit 1
}

# Eliminar contenedores e imágenes antiguas, específicamente los que estou usando (sprint3_ngx, sprint3_njs y sprint3_mdb)
print-message "Yellow" "Eliminando contenedores e imágenes antiguas..." "🗑️"
docker rm -f $DOCKER_CONTAINER
# Obtenemos la lista de imágenes relacionadas con la aplicación
$RELATED_IMAGES = docker images --format="{{.Repository}}:{{.Tag}}" | Select-String sprint3_
if ($RELATED_IMAGES) {
    # Eliminamos cada una de las imágenes relacionadas
    foreach ($image in $RELATED_IMAGES) {
        docker rmi -f $image
    }
} else {
    print-message "Green" "No se encontraron imágenes relacionadas" "✅"
}
# Obtenemos la lista de contenedores activos con el nombre sprint3_
$ACTIVE_CONTAINERS = docker ps -q --filter name=sprint3_
if ($ACTIVE_CONTAINERS) {
    # Eliminamos cada uno de los contenedores activos
    foreach ($container in $ACTIVE_CONTAINERS) {
        docker rm -f $container
    }
} else {
    print-message "Green" "No se encontraron contenedores activos" "✅"
}

# Eliminar contenedor y imagen del contenedor relacionado con la aplicación sprint3_
docker rmi -f "${DOCKER_IMAGE}:${APP_VERSION}"

# Crear la estructura de carpetas
print-message "Yellow" "Creando estructura de carpetas..." "📂"
New-Item -Path "$TEMP_DIR/Backend/src/nodejs/app_web" -ItemType Directory -Force
New-Item -Path "$TEMP_DIR/Backend/src/mariadb" -ItemType Directory -Force
Copy-Item "Backend/src/nodejs/app_web/Dockerfile" "$TEMP_DIR/Backend/src/nodejs/app_web"
Copy-Item -Recurse "Backend/src/nodejs/*" "$TEMP_DIR/Backend/src/nodejs" -Exclude "app_web", "node_modules"
Copy-Item -Recurse "Backend/src/mariadb/*" "$TEMP_DIR/Backend/src/mariadb"
Copy-Item -Recurse "Frontend/*" "$TEMP_DIR/Backend/src/nodejs/app_web"
Copy-Item "Backend/src/variables.env" "$TEMP_DIR/Backend/src"
Copy-Item "Frontend/src/img/logo.png" "$TEMP_DIR/Backend/src/nodejs"

# Copiar los archivos SQL al contenedor de MariaDB
if (Test-Path "Backend/src/mariadb/sql/ejemploBBDD.sql") {
    Copy-Item -Recurse "Backend/src/mariadb/sql/ejemploBBDD.sql" "$TEMP_DIR/Backend/src/mariadb/sql"
} else {
    print-message "Yellow" "Advertencia: No se ha encontrado el archivo ejemploBBDD.sql para importar datos a la base de datos" "⚠️"
}

# Asegúrate de copiar el archivo docker-compose.yml
if (Test-Path "Backend/src/docker-compose.yml") {
    Copy-Item "Backend/src/docker-compose.yml" "$TEMP_DIR/Backend/src/"
} else {
    print-message "Red" "No se ha encontrado el archivo docker-compose.yml" "❌"
    exit 1
}

print-message "Green" "Hecho!" "✅"

# Guardar el directorio original
$originalDir = Get-Location

# Cambiar el directorio de trabajo al directorio temporal
Set-Location "$TEMP_DIR/Backend/src"

# Cargar variables de entorno desde el archivo variables.env
$envVars = Get-Content "Backend/src/variables.env" | ForEach-Object {
    $name, $value = $_ -split '='
    [System.Environment]::SetEnvironmentVariable($name, $value)
}

# Detener y eliminar contenedores y volúmenes con docker-compose
print-message "Yellow" "Deteniendo y eliminando contenedores y volúmenes con docker-compose..." "🛑"
if (Get-Command docker-compose -ErrorAction SilentlyContinue) {
    & docker-compose down -v
} else {
    print-message "Red" "docker-compose no está instalado. Instalando docker-compose..." "❌"
    Invoke-WebRequest "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-Windows-x86_64.exe" -OutFile "$env:ProgramFiles\docker-compose.exe"
    $env:Path += ";$env:ProgramFiles"
    & "$env:ProgramFiles\docker-compose.exe" down -v
}

print-message "Green" "Contenedores y volúmenes eliminados correctamente!" "✅"

# Levantar contenedores con docker-compose
print-message "Yellow" "Levantando contenedores con docker-compose..." "🚢 🐳"
if (Get-Command docker-compose -ErrorAction SilentlyContinue) {
    & docker-compose up --build -d
} else {
    print-message "Red" "docker-compose no está instalado. Instalando docker-compose..."
    Invoke-WebRequest "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-Windows-x86_64.exe" -OutFile "$env:ProgramFiles\docker-compose.exe"
    $env:Path += ";$env:ProgramFiles"
    & "$env:ProgramFiles\docker-compose.exe" up --build -d
}

print-message "Green" "Contenedores levantados correctamente!" "✅"

# Esperar un momento para asegurarse de que los contenedores estén completamente levantados
Start-Sleep -Seconds 10

# Ejecutar el archivo SQL dentro del contenedor de MariaDB usando variables de entorno
print-message "Yellow" "Ejecutando el archivo SQL dentro del contenedor de MariaDB..." "📄"
Invoke-Expression "docker exec -i $(docker-compose ps -q mariadb) mysql -u $env:DB_USUARIO -p$env:DB_CONTRASENYA $env:DB_NOMBRE < $TEMP_DIR/Backend/src/mariadb/sql/ejemploBBDD.sql"

# Volver al directorio original
Set-Location -Path $originalDir

# Limpieza del directorio temporal
print-message "Green" "Limpiando el directorio temporal..." "🧹"
Remove-Item -Recurse -Force $TEMP_DIR

# Ejecutar pruebas dentro del contenedor de la aplicación
print-message "Yellow" "Ejecutando pruebas dentro del contenedor..." "📜"
docker exec sprint3_njs npm test

# Esperar un momento para asegurarse de que los resultados de las pruebas se impriman completamente
Start-Sleep -Seconds 2

# Mostrar logs de los contenedores
print-message "Yellow" "Mostrando logs de los contenedores..."
docker-compose -f "$TEMP_DIR/Backend/src/docker-compose.yml" logs -f

# Volver al directorio original
Set-Location -Path $originalDir