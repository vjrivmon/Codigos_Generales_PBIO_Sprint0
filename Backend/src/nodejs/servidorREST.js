const mariadb = require('mariadb');
const dotenv = require('dotenv');
// Cargar variables de entorno desde el archivo .env
dotenv.config();

// Verificar que las variables de entorno se carguen correctamente
console.log('DB_HOST:', process.env.DB_HOST);
console.log('DB_USUARIO:', process.env.DB_USUARIO);
console.log('DB_CONTRASENYA:', process.env.DB_CONTRASENYA);
console.log('DB_NOMBRE:', process.env.DB_NOMBRE);
console.log('DB_CONNECTION_LIMIT:', process.env.DB_CONNECTION_LIMIT);

// Configuración de la base de datos (similar a main.js)
const pool = mariadb.createPool({
  host: process.env.DB_HOST,
  user: process.env.DB_USUARIO,
  password: process.env.DB_CONTRASENYA,
  database: process.env.DB_NOMBRE,
  connectionLimit: parseInt(process.env.DB_CONNECTION_LIMIT, 10)
});

// Función para consultar medida
async function ConsultarMedida(req, res) {
  const { id_sensor } = req.params;
  let connection;
  try {
    connection = await pool.getConnection();
    const rows = await connection.query('SELECT * FROM mediciones WHERE id_sensor = ?', [id_sensor]);
    res.json(rows);
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) connection.release();
  }
}

// Función para crear una nueva medición
async function agregarMedicion(req, res) {
  const nuevaMedicion = req.body;
  let connection;
  try {
    connection = await pool.getConnection();
    const query = 'INSERT INTO mediciones (hora, latitud, longitud, id_sensor, valorGas, valorTemperatura) VALUES (?, ?, ?, ?, ?, ?)';
    const result = await connection.query(query, [
      nuevaMedicion.hora, 
      nuevaMedicion.latitud,
      nuevaMedicion.longitud,
      nuevaMedicion.id_sensor, 
      nuevaMedicion.valorGas, 
      nuevaMedicion.valorTemperatura
    ]);
    nuevaMedicion.id = result.insertId; // Asigna el ID generado automáticamente
    res.status(201).json(nuevaMedicion);
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error al insertar los datos');
  } finally {
    if (connection) connection.release();
  }
}

// Función para consultar datos de usuario
async function ConsultarDatosUsuario(req, res) {
  const { id_usuario } = req.params;
  let connection;
  try {
    connection = await pool.getConnection();
    const rows = await connection.query('SELECT * FROM usuarios WHERE id_usuario = ?', [id_usuario]);
    res.json(rows);
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) connection.release();
  }
}

// Función para consultar si hay alerta
async function ConsultarSiHayAlerta(req, res) {
  const { id_sensor } = req.params;
  let connection;
  try {
    connection = await pool.getConnection();
    const query = 'SELECT valorGas FROM mediciones WHERE id_sensor = ? ORDER BY fecha DESC LIMIT 1';
    const rows = await connection.query(query, [id_sensor]);

    const hayAlerta = rows.length > 0 && rows[0].valorGas > 50;  // Ejemplo: si valorGas es mayor a 50 hay alerta
    res.json({ hayAlerta });
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) connection.release();
  }
}

// Función para agregar un usuario
async function agregarUsuario(req, res) {
  const nuevoUsuario = req.body;
  let connection;
  try {
    connection = await pool.getConnection();
    const query = 'INSERT INTO usuarios (correo, contrasenya) VALUES (?, ?)';
    const result = await connection.query(query, [
      nuevoUsuario.correo,
      nuevoUsuario.contrasenya
    ]);
    nuevoUsuario.id = result.insertId;
    res.status(201).json(nuevoUsuario);
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error al agregar el usuario');
  } finally {
    if (connection) connection.release();
  }
}

// Función para eliminar un usuario
async function EliminarUsuario(req, res) {
  const { id_usuario } = req.params;
  let connection;
  try {
    connection = await pool.getConnection();
    const query = 'DELETE FROM usuarios WHERE id_usuario = ?';
    const result = await connection.query(query, [id_usuario]);
    if (result.affectedRows === 0) {
      res.status(404).send('Usuario no encontrado');
    } else {
      res.status(200).send('Usuario eliminado correctamente');
    }
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error al eliminar el usuario');
  } finally {
    if (connection) connection.release();
  }
}

// Función para consultar todas las tablas de la base de datos
async function ConsultarBaseDeDatos(req, res) {
  let connection;
  try {
    connection = await pool.getConnection();

    // Consultar todas las tablas
    const mediciones = await connection.query('SELECT * FROM mediciones');
    const usuarios = await connection.query('SELECT * FROM usuarios');
    const sensores = await connection.query('SELECT * FROM sensores');

    // Enviar los resultados como respuesta
    res.json({ mediciones, usuarios, sensores });
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error en la consulta a la base de datos');
  } finally {
    if (connection) connection.release();
  }
}

// Exportar funciones para ser usadas en APIRest.js
module.exports = {
  ConsultarMedida,
  agregarMedicion,
  ConsultarDatosUsuario,
  ConsultarSiHayAlerta,
  agregarUsuario,
  EliminarUsuario,
  ConsultarBaseDeDatos
};
