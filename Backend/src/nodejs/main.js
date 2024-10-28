/**
 * @file API REST con base de datos en MariaDB
 * @author Vicente Rivas Monferrer
 */

const mariadb = require('mariadb'); /**< Biblioteca para interactuar con MariaDB */
const express = require('express'); /**< Framework para crear APIs y aplicaciones web */
const dotenv = require('dotenv'); /**< Paquete para leer variables de entorno desde un archivo .env */
const app = express(); /**< Instancia del framework Express */

/**
 * @var {number} port Puerto al que se escucha el servidor
 */
const port = 8080;

/**
 * Carga las variables de entorno desde el archivo .env.
 *
 * @see dotenv.config()
 */
dotenv.config();

/**
 * Configuración general de la base de datos en MariaDB.
 */
const config = {
  /**
   * Direccion del host donde se encuentra la base de datos
   */
  host: process.env.DB_HOST,

  /**
   * Usuario con el que se conecta a la base de datos
   */
  user: process.env.DB_USUARIO,

  /**
   * Contraseña para acceder a la base de datos
   */
  password: process.env.DB_CONTRASENYA,

  /**
   * Nombre de la base de datos a la que se conectará
   */
  database: process.env.DB_NOMBRE,

  /**
   * Limite de conexiones simultaneas con la base de datos
   *
   * @see mariadb.createPool()
   */
  connectionLimit: parseInt(process.env.DB_CONNECTION_LIMIT, 10)
};

/**
 * Crea un pool de conexiones a la base de datos.
 *
 * @see mariadb.createPool()
 */
console.log("Creando pool...");
const pool = mariadb.createPool(config);

/**
 * Manejador para detectar el evento SIGINT (señal de control) que se genera cuando se presiona Ctrl+C
 */
process.on("SIGINT", function() {
    console.log("ctl-C detectado");
    process.exit();
});

/**
 * Ruta GET para consultar la base de datos.
 *
 * @param {object} req Objeto que contiene la solicitud recibida por el servidor
 * @param {object} res Objeto que permite enviar una respuesta al cliente
 */
app.get('/', async (req, res) => {
  let connection;
  try {
    console.log("Conectando...");
    // Obtener una conexión del pool
    connection = await pool.getConnection();
    console.log("Conexión establecida.");

    /**
     * Consulta SQL para obtener todas las filas de la tabla mediciones.
     *
     * @see connection.query()
     */
    const rows = await connection.query('SELECT * FROM mediciones');
    
    // Enviar el resultado como respuesta
    res.json(rows);

  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error en la consulta a la base de datos');
  } finally {
    console.log("Liberando conexión...");
    // Liberar la conexión de vuelta al pool
    if (connection) {
      connection.release();
    }
    console.log("Fin del bloque finally.");
  }
});

/**
 * Importa y utiliza las rutas de API REST desde el archivo ./APIRest.js.
 *
 * @see require()
 */
const APIRest = require('./APIRest');
app.use('/', APIRest);

/**
 * Inicia el servidor en el puerto especificado.
 *
 * @see app.listen()
 */
app.listen(8080, () => {
  console.log(`API REST corriendo en http://localhost:${port}/api-docs/`);
  console.log(`APP WEB corriendo en http://localhost`);
console.log(`Consulta completa de la base de datos corriendo en http://localhost:8080/base-datos`);
});