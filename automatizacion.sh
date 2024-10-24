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
         if command -v $1 &>/dev/null; then
            print_message $GREEN "$1 instalado correctamente."
         else
            print_message $RED "Error al instalar $1."
            exit 1
         fi
      else
         print_message $RED "No se puede instalar automáticamente $1."
         exit 1
      fi
   fi
}

# Llamar a la función anterior
check_tools docker

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
docker rm -f $DOCKER_CONTAINER
docker rmi -f $DOCKER_IMAGE:$APP_VERSION
docker rm -f $(docker ps -a -q)
docker rmi -f $(docker images -q)

# Crear la estructura de carpetas/directorios
print_message $YELLOW "Creando estructura de carpetas..."
mkdir -p $TEMP_DIR/Backend/src/nodejs/app_web
mkdir -p $TEMP_DIR/Backend/src/mariadb

cp Backend/src/nodejs/app_web/Dockerfile $TEMP_DIR/Backend/src/nodejs/app_web
rsync -av --exclude='app_web' Backend/src/nodejs/* $TEMP_DIR/Backend/src/nodejs
cp -r Backend/src/mariadb/* $TEMP_DIR/Backend/src/mariadb
cp -r Frontend/* $TEMP_DIR/Backend/src/nodejs/app_web
cp -r Backend/src/variables.env $TEMP_DIR/Backend/src

# Asegúrate de copiar el archivo docker-compose.yml desde Backend/src/
if [ -f "Backend/src/docker-compose.yml" ]; then
   cp Backend/src/docker-compose.yml $TEMP_DIR/Backend/src/
else
   print_message $RED "No se ha encontrado el archivo docker-compose.yml"
   exit 1
fi

print_message $GREEN "Hecho!"

# Montar los contenedores con docker-compose
print_message $YELLOW "Levantando contenedores con docker-compose..."
if command -v docker-compose &>/dev/null; then
   docker-compose -f $TEMP_DIR/Backend/src/docker-compose.yml up --build
else
   print_message $RED "docker-compose no está instalado. Instalando docker-compose..."
   if command -v curl &>/dev/null; then
      sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
      sudo chmod +x /usr/local/bin/docker-compose
      if command -v docker-compose &>/dev/null; then
         print_message $GREEN "docker-compose instalado correctamente."
         docker-compose -f $TEMP_DIR/Backend/src/docker-compose.yml up --build
      else
         print_message $RED "Error al instalar docker-compose."
         exit 1
      fi
   else
      print_message $RED "curl no está instalado. Instalando curl..."
      sudo apt-get update
      sudo apt-get install -y curl
      if command -v curl &>/dev/null; then
         sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
         sudo chmod +x /usr/local/bin/docker-compose
         if command -v docker-compose &>/dev/null; then
            print_message $GREEN "docker-compose instalado correctamente."
            docker-compose -f $TEMP_DIR/Backend/src/docker-compose.yml up --build
         else
            print_message $RED "Error al instalar docker-compose."
            exit 1
         fi
      else
         print_message $RED "Error al instalar curl."
         exit 1
      fi
   fi
fi
print_message $GREEN "Contenedores levantados correctamente!"
#set -y
# Limpieza del directorio temporal
#print_message $GREEN "Limpiando el directorio temporal..."
#rm -rf $TEMP_DIR