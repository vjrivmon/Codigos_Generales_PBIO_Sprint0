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
// Función para crear una nueva medición
async function agregarMedicion(req, res) {
  const nuevaMedicion = req.body;
  let connection;
  try {
    connection = await pool.getConnection();

    // Obtener el primer id_sensor disponible
    const sensorQuery = 'SELECT id_sensor FROM sensores LIMIT 1'; // Puedes modificar esto para obtener el sensor deseado
    const sensorRows = await connection.query(sensorQuery);
    if (sensorRows.length === 0) {
      return res.status(400).send('No hay sensores disponibles');
    }

    const id_sensor = sensorRows[0].id_sensor; // Asignar el primer id_sensor

    const query = 'INSERT INTO mediciones (hora, latitud, longitud, id_sensor, valorGas, valorTemperatura) VALUES (?, ?, ?, ?, ?, ?)';
    const result = await connection.query(query, [
      nuevaMedicion.hora,
      nuevaMedicion.latitud,
      nuevaMedicion.longitud,
      id_sensor, // Usar el id_sensor obtenido
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

// Función para agregar un usuario
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

// Función para eliminar un usuario
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
// Función para consultar todas las tablas de la base de datos
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
