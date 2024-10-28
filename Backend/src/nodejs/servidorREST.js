/**
 * @file main.js
 * @brief Conexión a la base de datos MariaDB y ejecución de consultas SQL.
 */

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

/**
 * @brief Configuración de la base de datos MariaDB.
 *
 * Se crea una piscina de conexiones a la base de datos, con un límite de conexiones
 * establecido por el valor de la variable de entorno DB_CONNECTION_LIMIT.
 */
const pool = mariadb.createPool({
  host: process.env.DB_HOST,
  user: process.env.DB_USUARIO,
  password: process.env.DB_CONTRASENYA,
  database: process.env.DB_NOMBRE,
  connectionLimit: parseInt(process.env.DB_CONNECTION_LIMIT, 10)
});

/**
 * @brief Función para consultar medida en la base de datos.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para obtener las mediciones del sensor con ID determinado.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function ConsultarMedida(req, res) {
  const { id_sensor } = req.params;
  let connection;

  try {
    console.log(`Intentando obtener conexión para el sensor ID: ${id_sensor}`);
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');
    
    // Verifica que la conexión esté activa antes de ejecutar la consulta
    if (!connection) {
      console.error('No se pudo obtener una conexión');
      res.status(500).send('No se pudo obtener una conexión');
      return;
    }

    const rows = await connection.query('SELECT * FROM mediciones WHERE id_sensor = ?', [id_sensor]);
    console.log('Consulta ejecutada con éxito');
    res.json(rows);
  } catch (err) {
    console.error('Error al ejecutar la consulta:', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}
/**
 * @brief Función para agregar una nueva medición.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para insertar una nueva medición en la tabla mediciones.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function agregarMedicion(req, res) {
  const nuevaMedicion = req.body;
  let connection;
  try {
    connection = await pool.getConnection();

    // Obtener el primer id_sensor disponible
    const sensorQuery = 'SELECT id_sensor FROM sensores LIMIT 1'; 
    const sensorRows = await connection.query(sensorQuery);
    if (sensorRows.length === 0) {
      return res.status(400).send('No hay sensores disponibles');
    }

    const id_sensor = sensorRows[0].id_sensor; 

    const query = 'INSERT INTO mediciones (hora, latitud, longitud, id_sensor, valorGas, valorTemperatura) VALUES (?, ?, ?, ?, ?, ?)';
    const result = await connection.query(query, [
      nuevaMedicion.hora,
      nuevaMedicion.latitud,
      nuevaMedicion.longitud,
      id_sensor, 
      nuevaMedicion.valorGas,
      nuevaMedicion.valorTemperatura
    ]);

    nuevaMedicion.id = result.insertId; 
    res.status(201).json(nuevaMedicion);
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error al insertar los datos');
  } finally {
    if (connection) connection.release();
  }
}
/**
 * @brief Función para consultar datos de usuario.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para obtener los datos del usuario con ID determinado.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function ConsultarDatosUsuario(req, res) {
  const { id_usuario } = req.params;
  let connection;
  try {
    console.log(`Intentando obtener conexión para consultar datos del usuario ID: ${id_usuario}`);
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    const query = 'SELECT * FROM usuarios WHERE id_usuario = ?';
    console.log('Ejecutando consulta para obtener datos del usuario');
    const rows = await connection.query(query, [id_usuario]);
    console.log('Consulta ejecutada con éxito');
    res.json(rows);
  } catch (err) {
    console.error('Error en la consulta:', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}

// Función para verificar usuario por correo y contraseña
async function verificarUsuario(req, res) {
  const { correo, contrasena } = req.query;
  let connection;
  try {
    console.log(`Intentando obtener conexión para verificar usuario con correo: ${correo}`);
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    const query = 'SELECT * FROM usuarios WHERE correo = ?';
    console.log('Ejecutando consulta para verificar usuario');
    const rows = await connection.query(query, [correo]);
    console.log('Consulta ejecutada con éxito');

    if (rows.length === 0) {
      console.warn('Usuario no existe');
      return res.status(404).json({ success: false, error: 'Usuario no existe' });
    }

    const usuario = rows[0];
    if (usuario.contrasena === contrasena) {
      console.log('Contraseña correcta');
      return res.json({ success: true });
    } else {
      console.warn('Contraseña incorrecta');
      return res.json({ success: false, error: 'Contraseña incorrecta' });
    }
  } catch (err) {
    console.error('Error al verificar usuario:', err);
    res.status(500).json({ success: false, error: 'Error en el servidor' });
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}

// Función para consultar si hay alerta
async function ConsultarSiHayAlerta(req, res) {
  const { id_sensor } = req.params;
  let connection;
  try {
    connection = await pool.getConnection();
    const rows = await connection.query('SELECT valorGas FROM mediciones WHERE id_sensor = ?', [id_sensor]);
    if (rows.length === 0) {
      return res.json({ hayAlerta: false, message: 'No hay mediciones para este sensor' });
    }

    // Suponiendo que quieres verificar si cualquier valorGas es mayor a 30
    const hayAlerta = rows.some(row => row.valorGas > 30);
    res.json({ hayAlerta });
  } catch (err) {
    console.error('Error: ', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) connection.release();
  }
}

/**
 * @brief Función para agregar un usuario.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para insertar un nuevo usuario en la tabla usuarios.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function agregarUsuario(req, res) {
  const nuevoUsuario = req.body;
  let connection;
  try {
    console.log('Intentando obtener conexión para agregar un nuevo usuario');
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    const query = 'INSERT INTO usuarios (correo, contrasena) VALUES (?, ?)';
    console.log('Ejecutando consulta para agregar usuario');
    const result = await connection.query(query, [
      nuevoUsuario.correo,
      nuevoUsuario.contrasena
    ]);
    console.log('Consulta de inserción ejecutada con éxito');

    nuevoUsuario.id = result.insertId;
    res.status(201).json(nuevoUsuario);
  } catch (err) {
    console.error('Error al agregar el usuario:', err);
    res.status(500).send('Error al agregar el usuario');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}

/*
  @brief Función para eliminar un usuario
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para borrar el usuario con ID determinado.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function EliminarUsuario(req, res) {
  const { id_usuario } = req.params;
  let connection;
  try {
    console.log(`Intentando obtener conexión para eliminar usuario ID: ${id_usuario}`);
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    const query = 'DELETE FROM usuarios WHERE id_usuario = ?';
    console.log('Ejecutando consulta para eliminar usuario');
    const result = await connection.query(query, [id_usuario]);
    console.log('Consulta de eliminación ejecutada con éxito');

    if (result.affectedRows === 0) {
      console.warn('Usuario no encontrado');
      res.status(404).send('Usuario no encontrado');
    } else {
      res.status(200).send('Usuario eliminado correctamente');
    }
  } catch (err) {
    console.error('Error al eliminar el usuario:', err);
    res.status(500).send('Error al eliminar el usuario');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}

// Por implementar
// Función para asociar un sensor a un usuario
/*
async function asociarSensorAUsuario(req, res) {
  const { id_usuario, id_sensor } = req.body;
  let connection;
  try {
    console.log(`Intentando obtener conexión para asociar sensor ${id_sensor} al usuario ${id_usuario}`);
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    const query = 'UPDATE sensores SET id_usuario = ? WHERE id_sensor = ?';
    const result = await connection.query(query, [id_usuario, id_sensor]);

    if (result.affectedRows === 0) {
      console.warn('Sensor no encontrado o no se pudo asociar');
      return res.status(404).send('Sensor no encontrado o no se pudo asociar');
    }

    res.status(200).send('Sensor asociado al usuario correctamente');
  } catch (err) {
    console.error('Error al asociar sensor al usuario:', err);
    res.status(500).send('Error al asociar sensor al usuario');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}
  */
/**
 * @brief Función para consultar todas las tablas de la base de datos.
 *
 * Esta función obtiene una conexión a la base de datos y ejecuta consultas SQL
 * para obtener todos los registros de las tablas mediciones, usuarios y sensores.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function ConsultarBaseDeDatos(req, res) {
  let connection;
  try {
    console.log('Intentando obtener conexión para consultar todas las tablas');
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    console.log('Ejecutando consulta para obtener todas las mediciones');
    const mediciones = await connection.query('SELECT * FROM mediciones');
    console.log('Consulta de mediciones ejecutada con éxito');

    console.log('Ejecutando consulta para obtener todos los usuarios');
    const usuarios = await connection.query('SELECT * FROM usuarios');
    console.log('Consulta de usuarios ejecutada con éxito');

    console.log('Ejecutando consulta para obtener todos los sensores');
    const sensores = await connection.query('SELECT * FROM sensores');
    console.log('Consulta de sensores ejecutada con éxito');

    res.json({ mediciones, usuarios, sensores });
  } catch (err) {
    console.error('Error en la consulta a la base de datos:', err);
    res.status(500).send('Error en la consulta a la base de datos');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
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
  ConsultarBaseDeDatos,
  verificarUsuario
};

