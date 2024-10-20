# Usamos la imagen oficial de MariaDB
FROM mariadb:latest

# Definir argumentos para las variables de entorno
#ARG DB_USUARIO
#ARG DB_CONTRASENYA
#ARG DB_NOMBRE

# Definir las variables de entorno para MariaDB
#ENV MARIADB_DATABASE=${DB_NOMBRE}
#ENV MARIADB_ROOT_PASSWORD=${DB_CONTRASENYA}

# Copiame el fichero .sql (que crea la base de datos con la tabla de prueba) dentro del contenedor
# Si lo ponemos en esye directorio, ejecutará el script
# la primera vez que se arranque el contenedor a partir de esta imagen
COPY ./sql/*.sql /docker-entrypoint-initdb.d/
