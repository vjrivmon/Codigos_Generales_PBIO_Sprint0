#!/bin/bash

#set -x

# Colores para los mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Variables
TEMP_DIR="tempdir"
DOCKER_IMAGE="sprint1_ngx"
DOCKER_CONTAINER="sprint1"
PORT=80
DOCKERFILE_PATH="Dockerfile"
SQL_FILE="ejemploBBDD.sql"
DOCKER_COMPOSE_FILE="docker-compose.yml"

print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

check_tools() {
    if command -v $1 &>/dev/null; then
        print_message $GREEN "$1 está instalado."
    else
        print_message $RED "$1 no está instalado. Instalando $1..."
        if [ "$1" == "docker" ]; then
            sudo apt-get update
            sudo apt-get install -y docker.io
            sudo systemctl start docker
            sudo systemctl enable docker
        elif [ "$1" == "jq" ]; then
            sudo apt-get update
            sudo apt-get install -y jq
        elif [ "$1" == "rsync" ]; then
            sudo apt-get update
            sudo apt-get install -y rsync
        elif [ "$1" == "docker-compose" ]; then
            sudo apt-get update
            sudo apt-get install -y docker-compose
        else
            print_message $RED "No se puede instalar automáticamente $1."
            exit 1
        fi

        # Verificar si se ha instalado correctamente
        if command -v $1 &>/dev/null; then
            print_message $GREEN "$1 instalado correctamente."
        else
            print_message $RED "Error al instalar $1."
            exit 1
        fi
    fi
}

# Función para verificar que Docker Desktop esté corriendo
check_docker_desktop() {
    if ! docker info &>/dev/null; then
        print_message $RED "Docker Desktop no está corriendo o no está integrado con WSL 2. Por favor, asegúrate de que Docker Desktop esté iniciado y la integración con WSL 2 esté habilitada."
        exit 1
    fi
}

# Llamar a la función para verificar/instalar las herramientas necesarias
check_tools docker
check_tools jq
check_tools rsync
check_tools docker-compose

# Verificar que Docker Desktop esté corriendo
check_docker_desktop

# Leer versión de la aplicación
if [ -f "Backend/src/nodejs/package.json" ]; then
    APP_VERSION=$(jq -r '.version' Backend/src/nodejs/package.json)
    print_message $GREEN "Versión de la aplicación: $APP_VERSION"
else
    print_message $RED "No se ha encontrado el archivo package.json"
    exit 1
fi

# Eliminar antiguos contenedores e imágenes en ejecución
print_message $YELLOW "Eliminando contenedores e imágenes antiguas..."
docker ps -a --filter "name=$DOCKER_CONTAINER" -q | xargs -r docker rm -f
docker images --filter=reference="$DOCKER_IMAGE:$APP_VERSION" -q | xargs -r docker rmi -f

# Crear la estructura de carpetas/directorios
print_message $YELLOW "Creando estructura de carpetas..."
mkdir -p $TEMP_DIR/Backend/src/nodejs/app_web
mkdir -p $TEMP_DIR/Backend/src/mariadb
mkdir -p $TEMP_DIR/Backend/src/mariadb/sql
cp Backend/src/nodejs/app_web/Dockerfile $TEMP_DIR/Backend/src/nodejs/app_web
rsync -av --exclude='app_web' --exclude='node_modules' Backend/src/nodejs/ $TEMP_DIR/Backend/src/nodejs
cp -r Backend/src/mariadb/* $TEMP_DIR/Backend/src/mariadb
cp -r Frontend/* $TEMP_DIR/Backend/src/nodejs/app_web

# Verificar si el archivo variables.env existe
if [ -f "Backend/src/variables.env" ]; then
    cp Backend/src/variables.env $TEMP_DIR/Backend/src
else
    print_message $YELLOW "Advertencia: No se ha encontrado el archivo variables.env"
fi

# Verificar si el archivo SQL existe y copiarlo
if [ -f "Backend/src/mariadb/sql/$SQL_FILE" ]; then
    cp "Backend/src/mariadb/sql/$SQL_FILE" $TEMP_DIR/Backend/src/mariadb/sql
else
    print_message $YELLOW "Advertencia: No se ha encontrado el archivo $SQL_FILE para importar datos a la base de datos"
fi

# Asegúrate de copiar el archivo docker-compose.yml desde Backend/src/
if [ -f "Backend/src/$DOCKER_COMPOSE_FILE" ]; then
    cp Backend/src/$DOCKER_COMPOSE_FILE $TEMP_DIR/Backend/src/
else
    print_message $RED "No se ha encontrado el archivo $DOCKER_COMPOSE_FILE"
    exit 1
fi

print_message $GREEN "Hecho!"

# Cambiar el directorio de trabajo al directorio temporal
cd "$TEMP_DIR/Backend/src"

# Detener y eliminar contenedores y volúmenes con docker-compose
print_message $YELLOW "Deteniendo y eliminando contenedores y volúmenes con docker-compose..."
docker-compose down -v

print_message $GREEN "Contenedores y volúmenes eliminados correctamente!"

# Levantar contenedores con docker-compose
print_message $YELLOW "Levantando contenedores con docker-compose..."
docker-compose up --build -d

print_message $GREEN "Contenedores levantados correctamente!"

# Esperar un momento para asegurarse de que los contenedores se inicien correctamente
sleep 2

# Verificar el estado de los contenedores
print_message $YELLOW "Verificando el estado de los contenedores..."
docker ps -a

# Obtener el nombre del contenedor en ejecución
RUNNING_CONTAINER=$(docker ps --filter "name=$DOCKER_CONTAINER" --format "{{.Names}}")

if [ -z "$RUNNING_CONTAINER" ]; then
    print_message $RED "Error: No se encontró un contenedor en ejecución con el nombre $DOCKER_CONTAINER"
    exit 1
fi

# Ejecutar pruebas dentro del contenedor de la aplicación
print_message $YELLOW "Ejecutando pruebas dentro del contenedor..."
docker exec sprint1_njs npm test

# Mostrar logs de los contenedores
print_message $YELLOW "Mostrando logs de los contenedores..."
docker-compose logs -f

# Volver al directorio original
cd -