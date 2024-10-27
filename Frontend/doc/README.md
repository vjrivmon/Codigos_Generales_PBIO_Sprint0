# Estructura del directorio src

Este directorio contiene el código fuente principal de la página web del proyecto VIMYP.

## Estructura de carpetas

- `html/`: Contiene todos los archivos html de cada una de las páginas diseñadas en Axure.
- `style/`: Contiene archivos CSS para estilos globales y específicos de cada html.
- `js/`: Contiene archivos JavaScript para la lógica de la aplicación y algunas funcionalidades extra del frontend como animaciones.
- `img/`: Contiene imágenes utilizadas en la aplicación, como logos, iconos y fondos.
- `assets/`: Contiene iconos utilizados en el login.

## Archivos principales

- `index.html`: Página principal de la aplicación, que se carga al iniciar la aplicación.
- `styles.css`: Archivo CSS global que contiene estilos comunes utilizados en toda la aplicación.

## Tecnologías utilizadas

- HTML, CSS y JavaScript para la estructura, estilo y lógica de la aplicación frontend.
- Bootstrap para la creación de componentes de interfaz de usuario y la maquetación responsive.
- Google Fonts para la tipografía utilizada en la aplicación.
- Font Awesome para los iconos utilizados en la aplicación.


## Cómo empezar

1. Asegúrate de tener Docker instalado y corriendo en tu máquina local.
2. Asegúrate de tener tu IP puesta en el archivo login.js
3. Ejecuta el comando `.\automatizacion.ps1` en la raíz del proyecto para iniciar la aplicación frontend en un contenedor Docker.
4. Abre tu navegador y busca `http://localhost:8080` para ver la aplicación en funcionamiento.


## Notas adicionales
Esta versión está creada para el Sprint 1. Se espera que en futuros sprints se agreguen más funcionalidades y puede que se realicen cambios en la estructura de src, añadiendo más directorios si fuesen mecesarios. Se pueden ver los usuarios ya registrados si introducimos `http://localhost:8080/base-datos` en el navegador.