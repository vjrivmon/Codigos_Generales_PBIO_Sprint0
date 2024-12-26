/**
 * @file main.js
 * @brief Conexión a la base de datos MariaDB y ejecución de consultas SQL.
 */

const mariadb = require('mariadb');
const dotenv = require('dotenv');
const bcrypt = require('bcryptjs');
//const enviarCorreo = require('./emailCrearUsuarioNuevo'); // Asegúrate de que la ruta sea correcta
const nodemailer = require('nodemailer');
const fs = require('fs');
// Cargar variables de entorno desde el archivo .env
dotenv.config();

// Verificar que las variables de entorno se carguen correctamente
console.log('DB_HOST:', process.env.DB_HOST);
console.log('DB_USUARIO:', process.env.DB_USUARIO);
console.log('DB_CONTRASENYA:', process.env.DB_CONTRASENYA);
console.log('DB_NOMBRE:', process.env.DB_NOMBRE);
console.log('DB_CONNECTION_LIMIT:', process.env.DB_CONNECTION_LIMIT);
console.log('EMAIL_HOST:', process.env.EMAIL_HOST);
console.log('EMAIL_USER:', process.env.EMAIL_USER);
console.log('EMAIL_PASSWORD:', process.env.EMAIL_PASSWORD);

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

var transporter = nodemailer.createTransport({
  service: process.env.EMAIL_SERVICE,
  auth: {
      user: process.env.EMAIL_FROM,
      pass: process.env.EMAIL_PASS
  }
});

async function enviarCorreoParaVerificacion(email) {
  var mailOptions = {
    from: process.env.EMAIL_FROM,
    to: email,
    subject: 'Bienvenido a VIMYP',
    html: `
    <!DOCTYPE html>
    <html lang="es">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;700&display=swap" rel="stylesheet">
      <style>
        body {
          font-family: 'Montserrat', sans-serif;
          background-color: white;
          color: #3B3B3B;
          padding: 20px;
        }
        .header {
          text-align: center;
          padding: 10px 0 0 0;
        }
        .logo {
          width: 100px;
          height: auto;
          margin: 0 auto;
        }
        h1 {
          font-size: 2rem;
          color: #395886;
          font-family: 'Montserrat', sans-serif;
        }
        .content {
          background-color: white;
          padding: 20px;
          border-radius: 33px;
          margin: 20px auto;
          max-width: 80%;
          text-align: center;
          color: #3B3B3B;
        }
        .content p {
          font-size: 1.1rem;
          line-height: 1.5;
          text-align: left;
          font-family: 'Montserrat', sans-serif;
          color: #3B3B3B !important; /* Aseguramos que el texto sea #3B3B3B */
        }
        
        .button {
          display: inline-block;
          padding: 10px 20px;
          margin-top: 20px;
          text-decoration: none !important; /* Aseguramos que no haya subrayado en el enlace */
          border-radius: 33px;
          font-weight: bold;
          font-size: 1.1rem;
          transition: background-color 0.3s;
          background-color: #395886;
          border: 2px solid #395886;
          color: white !important; /* Aseguramos que el texto sea blanco */
          font-family: 'Montserrat', sans-serif !important;
        }

        .button:hover {
          background-color: white;
          color: #395886 !important; /* Aseguramos que el texto sea #395886 en el hover */
          border: 2px solid #395886;
        }

        .footer {
          clear: both;
          margin-top: 20px;
          padding-top: 20px;
          text-align: center;
          font-size: 0.9rem;
          color: #666;
          border-top: 1px solid #666;
        }
      </style>
    </head>
    <body>
      <div class="header">
        <img src="cid:logo" alt="Logotipo VIMYP" class="logo">
        <h1>Te damos la bienvenida a VIMYP</h1>
      </div>
      <div class="content">
        <p>Estimado usuario,</p>
        <p>Nos complace darte la bienvenida a VIMYP. Desde ahora, podrás contar con información precisa y en tiempo real sobre la calidad del aire que respiras. Nos esforzamos por ofrecerte tranquilidad y facilidad en el acceso a datos de calidad.</p>
        <p>Haciendo click en este botón ya estarás oficialmente dado de alta.</p>
        <a href="http://localhost/html/login.html" class="button">Confirmar</a> <!---- Cambiar por la URL del sitio web ---->	
        <p>¿No has sido tú? Si no solicitaste este registro, por favor ignora este mensaje o contáctanos.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
  `,
    attachments: [{
      filename: 'logo.png',
      path: __dirname + '/logo.png',
      cid: 'logo' // same cid value as in the html img src
    }]
  };

  console.log('Preparando para enviar correo con las siguientes opciones:', mailOptions);

  transporter.sendMail(mailOptions, (error, info) => {
    if (error) {
      console.log('Error al enviar correo:', error);
    } else {
      console.log('Correo enviado:', info.response);
    }
  });
}
async function enviarCorreoParaEditarDatos(email) {
  var mailOptions = {
    from: process.env.EMAIL_FROM,
    to: email,
    subject: 'Hemos visto que has hecho algunos cambios...',
    html: `
    <!DOCTYPE html>
    <html lang="es">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;700&display=swap" rel="stylesheet">
      <style>
        body {
          font-family: 'Montserrat', sans-serif;
          background-color: white;
          color: #3B3B3B;
          padding: 20px;
        }
        .header {
          text-align: center;
          padding: 10px 0 0 0;
        }
        .logo {
          width: 100px;
          height: auto;
          margin: 0 auto;
        }
        h1 {
          font-size: 2rem;
          color: #395886;
          font-family: 'Montserrat', sans-serif;
        }
        .content {
          background-color: white;
          padding: 20px;
          border-radius: 33px;
          margin: 20px auto;
          max-width: 80%;
          text-align: center;
          color: #3B3B3B;
        }
        .content p {
          font-size: 1.1rem;
          line-height: 1.5;
          text-align: left;
          font-family: 'Montserrat', sans-serif;
          color: #3B3B3B !important; /* Aseguramos que el texto sea #3B3B3B */
        }
        
        .button {
          display: inline-block;
          padding: 10px 20px;
          margin-top: 20px;
          text-decoration: none !important; /* Aseguramos que no haya subrayado en el enlace */
          border-radius: 33px;
          font-weight: bold;
          font-size: 1.1rem;
          transition: background-color 0.3s;
          background-color: #395886;
          border: 2px solid #395886;
          color: white !important; /* Aseguramos que el texto sea blanco */
          font-family: 'Montserrat', sans-serif !important;
        }

        .button:hover {
          background-color: white;
          color: #395886 !important; /* Aseguramos que el texto sea #395886 en el hover */
          border: 2px solid #395886;
        }

        .footer {
          clear: both;
          margin-top: 20px;
          padding-top: 20px;
          text-align: center;
          font-size: 0.9rem;
          color: #666;
          border-top: 1px solid #666;
        }
      </style>
    </head>
    <body>
      <div class="header">
        <img src="cid:logo" alt="Logotipo VIMYP" class="logo">
        <h1>Hemos visto que has hecho algunos cambios...</h1>
      </div>
      <div class="content">
        <p>Estimado usuario,</p>
        <p>Hemos visto que has realizado algunos cambios en los datos de tu perfil.</p>
        <p>Haciendo click en este botón confirmarás estos cambios.</p>
        <a href="http://localhost/html/login.html" class="button">Confirmar cambios</a> <!---- Cambiar por la URL del sitio web ---->	
        <p>¿No has sido tú? Si no solicitaste este cambio de datos por favor ponte en contácto con nosotros lo antes posible para asegurarnos de que nadie más ha accedido a tu cuenta.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
  `,
    attachments: [{
      filename: 'logo.png',
      path: __dirname + '/logo.png',
      cid: 'logo' // same cid value as in the html img src
    }]
  };

  console.log('Preparando para enviar correo con las siguientes opciones:', mailOptions);

  transporter.sendMail(mailOptions, (error, info) => {
    if (error) {
      console.log('Error al enviar correo:', error);
    } else {
      console.log('Correo enviado:', info.response);
    }
  });
}
async function enviarCorreoParaRestablecerContrasena(email) {
  var mailOptions = {
    from: process.env.EMAIL_FROM,
    to: email,
    subject: 'Hemos visto que has hecho algunos cambios...',
    html: `
    <!DOCTYPE html>
    <html lang="es">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;700&display=swap" rel="stylesheet">
      <style>
        body {
          font-family: 'Montserrat', sans-serif;
          background-color: white;
          color: #3B3B3B;
          padding: 20px;
        }
        .header {
          text-align: center;
          padding: 10px 0 0 0;
        }
        .logo {
          width: 100px;
          height: auto;
          margin: 0 auto;
        }
        h1 {
          font-size: 2rem;
          color: #395886;
          font-family: 'Montserrat', sans-serif;
        }
        .content {
          background-color: white;
          padding: 20px;
          border-radius: 33px;
          margin: 20px auto;
          max-width: 80%;
          text-align: center;
          color: #3B3B3B;
        }
        .content p {
          font-size: 1.1rem;
          line-height: 1.5;
          text-align: left;
          font-family: 'Montserrat', sans-serif;
          color: #3B3B3B !important; /* Aseguramos que el texto sea #3B3B3B */
        }
        
        .button {
          display: inline-block;
          padding: 10px 20px;
          margin-top: 20px;
          text-decoration: none !important; /* Aseguramos que no haya subrayado en el enlace */
          border-radius: 33px;
          font-weight: bold;
          font-size: 1.1rem;
          transition: background-color 0.3s;
          background-color: #395886;
          border: 2px solid #395886;
          color: white !important; /* Aseguramos que el texto sea blanco */
          font-family: 'Montserrat', sans-serif !important;
        }

        .button:hover {
          background-color: white;
          color: #395886 !important; /* Aseguramos que el texto sea #395886 en el hover */
          border: 2px solid #395886;
        }

        .footer {
          clear: both;
          margin-top: 20px;
          padding-top: 20px;
          text-align: center;
          font-size: 0.9rem;
          color: #666;
          border-top: 1px solid #666;
        }
      </style>
    </head>
    <body>
      <div class="header">
        <img src="cid:logo" alt="Logotipo VIMYP" class="logo">
        <h1>Hemos visto que has hecho algunos cambios...</h1>
      </div>
      <div class="content">
       <p>Estimado usuario,</p>
        <p>Hemos visto que has cambiado la contraseña de tu perfil.</p>
        <p>Haciendo click en este botón confirmarás estos cambios.</p>
        <a href="http://localhost/html/login.html" class="button">Confirmar cambios</a> <!---- Cambiar por la URL del sitio web ---->	
        <p>¿No has sido tú? Si no solicitaste este cambio de datos por favor ponte en contácto con nosotros lo antes posible para asegurarnos de que nadie más ha accedido a tu cuenta.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
  `,
    attachments: [{
      filename: 'logo.png',
      path: __dirname + '/logo.png',
      cid: 'logo' // same cid value as in the html img src
    }]
  };

  console.log('Preparando para enviar correo con las siguientes opciones:', mailOptions);

  transporter.sendMail(mailOptions, (error, info) => {
    if (error) {
      console.log('Error al enviar correo:', error);
    } else {
      console.log('Correo enviado:', info.response);
    }
  });
}
async function enviarCorreoParaRecuperarContrasena(email) {
  var mailOptions = {
    from: process.env.EMAIL_FROM,
    to: email,
    subject: 'Restablecer contraseña',
    html: `
    <!DOCTYPE html>
    <html lang="es">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;700&display=swap" rel="stylesheet">
      <style>
        body {
          font-family: 'Montserrat', sans-serif;
          background-color: white;
          color: #3B3B3B;
          padding: 20px;
        }
        .header {
          text-align: center;
          padding: 10px 0 0 0;
        }
        .logo {
          width: 100px;
          height: auto;
          margin: 0 auto;
        }
        h1 {
          font-size: 2rem;
          color: #395886;
          font-family: 'Montserrat', sans-serif;
        }
        .content {
          background-color: white;
          padding: 20px;
          border-radius: 33px;
          margin: 20px auto;
          max-width: 80%;
          text-align: center;
          color: #3B3B3B;
        }
        .content p {
          font-size: 1.1rem;
          line-height: 1.5;
          text-align: left;
          font-family: 'Montserrat', sans-serif;
          color: #3B3B3B !important; /* Aseguramos que el texto sea #3B3B3B */
        }
        
        .button {
          display: inline-block;
          padding: 10px 20px;
          margin-top: 20px;
          text-decoration: none !important; /* Aseguramos que no haya subrayado en el enlace */
          border-radius: 33px;
          font-weight: bold;
          font-size: 1.1rem;
          transition: background-color 0.3s;
          background-color: #395886;
          border: 2px solid #395886;
          color: white !important; /* Aseguramos que el texto sea blanco */
          font-family: 'Montserrat', sans-serif !important;
        }

        .button:hover {
          background-color: white;
          color: #395886 !important; /* Aseguramos que el texto sea #395886 en el hover */
          border: 2px solid #395886;
        }

        .footer {
          clear: both;
          margin-top: 20px;
          padding-top: 20px;
          text-align: center;
          font-size: 0.9rem;
          color: #666;
          border-top: 1px solid #666;
        }
      </style>
    </head>
    <body>
      <div class="header">
        <img src="cid:logo" alt="Logotipo VIMYP" class="logo">
        <h1>Te damos la bienvenida a VIMYP</h1>
      </div>
      <div class="content">
        <p>Estimado usuario,</p>
        <p>Hemos visto que has olvidado tu contraseña.</p>
        <p>Haciendo click en este botón podrás cambiar tu contraseña.</p>
        <a href="http://localhost/html/escribirNuevaContrasenya.html" class="button">Confirmar cambios</a> <!---- Cambiar por la URL del sitio web ---->	
        <p>¿No has sido tú? Si no solicitaste este cambio de datos por favor ponte en contácto con nosotros lo antes posible para asegurarnos de que nadie más ha accedido a tu cuenta.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
  `,
    attachments: [{
      filename: 'logo.png',
      path: __dirname + '/logo.png',
      cid: 'logo' // same cid value as in the html img src
    }]
  };

  console.log('Preparando para enviar correo con las siguientes opciones:', mailOptions);

  transporter.sendMail(mailOptions, (error, info) => {
    if (error) {
      console.log('Error al enviar correo:', error);
    } else {
      console.log('Correo enviado:', info.response);
    }
  });
}

/**
 * @brief Función para consultar medida en la base de datos.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para obtener las mediciones del sensor con ID determinado.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
const ConsultarMedida = async (req, res) => {
  let connection;
  try {
    connection = await pool.getConnection();
    console.log('Intentando obtener conexión para el sensor ID:', req.params.id_sensor);

    // Obtener la fecha y hora actual
    const now = new Date();
    // Calcular la fecha y hora de hace 8 horas
    const eightHoursAgo = new Date(now.getTime() - 8 * 60 * 60 * 1000);
    const formattedEightHoursAgo = eightHoursAgo.toISOString().slice(0, 19).replace('T', ' ');

    const rows = await connection.query(
      'SELECT * FROM mediciones WHERE id_sensor = ? AND fecha_hora >= ? ORDER BY fecha_hora DESC',
      [req.params.id_sensor, formattedEightHoursAgo]
    );
    console.log('Resultado de la consulta de mediciones:', rows);
    res.json(Array.isArray(rows) ? rows : [rows]);
  } catch (err) {
    console.error('Error al ejecutar la consulta:', err);
    res.status(500).send('Error en la consulta');
  } finally {
    if (connection) {
      connection.release();
    }
  }
};

/**
 * @brief Función para agregar una nueva medición.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para insertar una nueva medición en la tabla mediciones.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
const agregarMedicion = async (req, res) => {
  let connection;
  try {
    connection = await pool.getConnection();
    const nuevaMedicion = req.body;
    console.log('Datos de la nueva medición:', nuevaMedicion);

    // Convertir fecha_hora al formato compatible con DATETIME
    const fechaHoraFormateada = new Date(nuevaMedicion.fecha_hora).toISOString().slice(0, 19).replace('T', ' ');

    await connection.query(
      'INSERT INTO mediciones (id_sensor, fecha_hora, ubicacion, tipo_medicion, valor) VALUES (?, ?, ?, ?, ?)',
      [nuevaMedicion.id_sensor, fechaHoraFormateada, JSON.stringify(nuevaMedicion.ubicacion), nuevaMedicion.tipo_medicion, nuevaMedicion.valor]
    );
    console.log('Medición agregada con éxito');
    res.status(201).json(nuevaMedicion);
  } catch (err) {
    console.error('Error al agregar la medición:', err);
    res.status(500).send('Error al insertar los datos');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
};

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

    const query = `
      SELECT usuarios.*, senusu.id_sensor 
      FROM usuarios 
      LEFT JOIN senusu ON usuarios.id_usuario = senusu.id_usuario 
      WHERE usuarios.id_usuario = ?
    `;
    console.log('Ejecutando consulta para obtener datos del usuario');
    const rows = await connection.query(query, [id_usuario]);
    console.log('Consulta ejecutada con éxito');
    if (rows.length > 0) {
      res.json(rows[0]); // Devolver solo el primer resultado
    } else {
      res.status(404).send('Usuario no encontrado');
    }
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
  const { correo, contrasena } = req.body;
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
    // Verificar la contraseña proporcionada por el usuario
    const isMatch = await bcrypt.compare(contrasena, usuario.contrasena);
    if (isMatch) {
      console.log('Contraseña correcta');
      return res.json({ success: true, id_usuario: usuario.id_usuario });
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

    // Encriptar la contraseña
    const salt = await bcrypt.genSalt(10);
    const hash = await bcrypt.hash(nuevoUsuario.contrasena, salt);
    nuevoUsuario.contrasena = hash;

    const query = 'INSERT INTO usuarios (nombre, telefono, correo, contrasena) VALUES (?, ?, ?, ?)';
    console.log('Ejecutando consulta para agregar usuario');
    const result = await connection.query(query, [
      nuevoUsuario.nombre,
      nuevoUsuario.telefono,
      nuevoUsuario.correo,
      nuevoUsuario.contrasena
    ]);
    console.log('Consulta de inserción ejecutada con éxito');

    nuevoUsuario.id_usuario = result.insertId; // Cambiar a id_usuario
    // Excluir la contraseña del objeto devuelto
    const { contrasena, ...usuarioSinContrasena } = nuevoUsuario;

    // Enviar correo de bienvenida
    // await enviarCorreo(nuevoUsuario.correo, 'Bienvenido', 'Gracias por registrarte!');

    res.status(201).json(usuarioSinContrasena);
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

    console.log('Ejecutando consulta para obtener datos de la tabla roles');
    const roles = await connection.query('SELECT * FROM roles');
    console.log('Consulta de roles ejecutada con éxito');

    console.log('Ejecutando consulta para obtener datos de la tabla senusu');
    const senusu = await connection.query('SELECT * FROM senusu');
    console.log('Consulta de senusu ejecutada con éxito');

    console.log('Ejecutando consulta para obtener datos de la tabla usro');
    const usro = await connection.query('SELECT * FROM usro');
    console.log('Consulta de usro ejecutada con éxito');
  
    res.json({ mediciones, sensores, senusu, usuarios, usro, roles });
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

/**
 * @brief Función para recuperar la contraseña de un usuario.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para actualizar la contraseña del usuario con el correo proporcionado.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function recuperarContrasena(req, res) {
  const { correo, nuevaContrasena } = req.body;
  let connection;
  try {
    console.log(`Intentando obtener conexión para actualizar contraseña del usuario con correo: ${correo}`);
    connection = await pool.getConnection();
    console.log('Conexión obtenida con éxito');

    // Verificar si el usuario existe
    const checkUserQuery = 'SELECT * FROM usuarios WHERE correo = ?';
    const userRows = await connection.query(checkUserQuery, [correo]);
    console.log('Resultado de la consulta de usuarios:', userRows);
    if (userRows.length === 0) {
      console.warn('Usuario no encontrado');
      return res.status(404).send('Usuario no encontrado');
    }

    // Encriptar la nueva contraseña utilizando bcrypt
    const salt = await bcrypt.genSalt(10);
    const hash = await bcrypt.hash(nuevaContrasena, salt);

    const query = 'UPDATE usuarios SET contrasena = ? WHERE correo = ?';
    const result = await connection.query(query, [hash, correo]);
    console.log('Resultado de la actualización de contraseña:', result);

    if (result.affectedRows === 0) {
      console.warn('No se pudo actualizar la contraseña');
      return res.status(500).send('No se pudo actualizar la contraseña');
    }

    console.log('Contraseña actualizada correctamente para el usuario con correo:', correo);
    res.status(200).send('Contraseña actualizada correctamente');
  } catch (err) {
    console.error('Error al actualizar la contraseña:', err);
    res.status(500).send('Error al actualizar la contraseña');
  } finally {
    if (connection) {
      console.log('Liberando conexión');
      connection.release();
    }
  }
}

/**
 * @brief Función para editar los datos de un usuario.
 *
 * Se obtiene una conexión a la base de datos desde la piscina y se ejecuta una
 * consulta SQL para actualizar los datos del usuario con el correo proporcionado.
 *
 * @param req Objeto de solicitud HTTP (Express.js).
 * @param res Objeto de respuesta HTTP (Express.js).
 */
async function editarDatosUsuario(req, res) {
    const { id_usuario, nombre, telefono, correo, contrasena } = req.body;
    console.log('Datos recibidos para actualizar el usuario:', req.body);
    let connection;
    try {
        console.log(`Datos recibidos para la actualización: id_usuario: ${id_usuario}, nombre: ${nombre}, telefono: ${telefono}, correo: ${correo}, contrasena: ${contrasena}`);

        // Obtener conexión a la base de datos
        console.log(`Intentando obtener conexión para actualizar datos del usuario con ID: ${id_usuario}`);
        connection = await pool.getConnection();
        console.log('Conexión obtenida con éxito');

        // Verificar si el usuario existe
        const checkUserQuery = 'SELECT * FROM usuarios WHERE id_usuario = ?';
        const userRows = await connection.query(checkUserQuery, [id_usuario]);
        console.log('Resultado de la consulta de usuario:', userRows);

        if (userRows.length === 0) {
            console.warn('Usuario no encontrado');
            return res.status(404).send('Usuario no encontrado');
        }

        // Construir la consulta de actualización sin la contraseña, si no se proporciona
        let query = 'UPDATE usuarios SET nombre = ?, telefono = ?, correo = ? WHERE id_usuario = ?';
        let params = [nombre, telefono, correo, id_usuario];

        // Si la contraseña es proporcionada, encriptarla e incluirla en la actualización
        if (contrasena) {
            const salt = await bcrypt.genSalt(10);
            const hash = await bcrypt.hash(contrasena, salt);
            query = 'UPDATE usuarios SET nombre = ?, telefono = ?, correo = ?, contrasena = ? WHERE id_usuario = ?';
            params = [nombre, telefono, correo, hash, id_usuario];
        }

        // Ejecutar la actualización
        const result = await connection.query(query, params);
        console.log('Resultado de la actualización de datos del usuario:', result);

        if (result.affectedRows === 0) {
            console.warn('No se pudieron actualizar los datos del usuario');
            return res.status(500).send('No se pudieron actualizar los datos del usuario');
        }

        console.log('Datos del usuario actualizados correctamente para el usuario con ID:', id_usuario);

        // Enviar correo de notificación
        await enviarCorreoParaEditarDatos(correo);

        res.status(200).send('Datos del usuario actualizados correctamente');
    } catch (err) {
        console.error('Error al actualizar los datos del usuario:', err);
        res.status(500).send('Error al actualizar los datos del usuario');
    } finally {
        if (connection) {
            console.log('Liberando conexión');
            connection.release();
        }
    }
}

/**
 * @brief Función para encriptar contraseñas no encriptadas en la base de datos.
 */
async function encriptarContrasenas() {
  let connection;
  try {
    connection = await pool.getConnection();
    const usuarios = await connection.query('SELECT id_usuario, contrasena FROM usuarios');

    for (const usuario of usuarios) {
      // Verificar si la contraseña ya está encriptada
      if (!usuario.contrasena.startsWith('$2a$')) {
        const salt = await bcrypt.genSalt(10);
        const hash = await bcrypt.hash(usuario.contrasena, salt);
        await connection.query('UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?', [hash, usuario.id_usuario]);
      }
    }

    console.log('Contraseñas encriptadas y actualizadas correctamente');
  } catch (err) {
    console.error('Error al encriptar las contraseñas:', err);
  } finally {
    if (connection) {
      connection.release();
    }
  }
}

// Llamar a la función encriptarContrasenas al iniciar el servidor
encriptarContrasenas();

// async function verificarCorreo(req, res) {
//   const { email } = req.query;
//   let connection;
//   try {
//     console.log(`Intentando obtener conexión para verificar correo: ${email}`);
//     connection = await pool.getConnection();
//     console.log('Conexión obtenida con éxito');

//     const query = 'UPDATE usuarios SET verificado = 1 WHERE correo = ?';
//     const result = await connection.query(query, [email]);
//     console.log('Resultado de la verificación de correo:', result);

//     if (result.affectedRows === 0) {
//       console.warn('No se pudo verificar el correo');
//       return res.status(500).send('No se pudo verificar el correo');
//     }

//     console.log('Correo verificado correctamente para el usuario con correo:', email);
//     res.status(200).send('Correo verificado correctamente');
//   } catch (err) {
//     console.error('Error al verificar el correo:', err);
//     res.status(500).send('Error al verificar el correo');
//   } finally {
//     if (connection) {
//       console.log('Liberando conexión');
//       connection.release();
//     }
//   }
// }

async function enviarCorreoVerificacion(req, res) {
    const { email } = req.body;
    try {
        await enviarCorreoParaVerificacion(email);
        res.status(200).send('Correo de verificación enviado correctamente');
    } catch (error) {
        console.error('Error al enviar el correo de verificación:', error);
        res.status(500).send('Error al enviar el correo de verificación');
    }
}
async function enviarCorreoEditarDatos(req, res) {
  const { email } = req.body;
  try {
      await enviarCorreoParaEditarDatos(email);
      res.status(200).send('Correo de Editar datos enviado correctamente');
  } catch (error) {
      console.error('Error al enviar el correo de Editar datos:', error);
      res.status(500).send('Error al enviar el correo de Editar datos');
  }
}
async function enviarCorreoRestablecerContrasena(req, res) {
  const { email } = req.body;
  try {
      await enviarCorreoParaRestablecerContrasena(email);
      res.status(200).send('Correo de Restablecer Contrasena enviado correctamente');
  } catch (error) {
      console.error('Error al enviar el correo de Restablecer Contrasena:', error);
      res.status(500).send('Error al enviar el correo de Restablecer Contrasena');
  }
}
async function enviarCorreoRecuperarContrasena(req, res) {
  const { email } = req.body;
  try {
      await enviarCorreoParaRecuperarContrasena(email);
      res.status(200).send('Correo de Recuperar Contrasena enviado correctamente');
  } catch (error) {
      console.error('Error al enviar el correo de Recuperar Contrasena:', error);
      res.status(500).send('Error al enviar el correo de Recuperar Contrasena');
  }
}


/**
 * @function asociarSensorAUsuario
 * @description Asocia un sensor a un usuario. Si el sensor no existe, lo crea.
 * @param {Object} req - Objeto de solicitud HTTP
 * @param {Object} res - Objeto de respuesta HTTP
 */
const asociarSensorAUsuario = async (req, res) => {
    const { correo, id_sensor, nombre, funciona } = req.body;
    let connection;

    try {
        connection = await pool.getConnection();
        console.log('Conexión a la base de datos establecida.');

        // Verificar si el usuario existe
        const usuario = await connection.query('SELECT * FROM usuarios WHERE correo = ?', [correo]);
        if (usuario.length === 0) {
            console.log('Usuario no encontrado:', correo);
            return res.status(404).send('Usuario no encontrado');
        }

        // Verificar si el sensor ya está asociado
        const sensor = await connection.query('SELECT * FROM sensores WHERE id_sensor = ?', [id_sensor]);
        if (sensor.length === 0) {
            // Crear el sensor si no existe
            await connection.query('INSERT INTO sensores (id_sensor, nombre, funciona) VALUES (?, ?, ?)', [id_sensor, nombre, funciona]);
            console.log('Sensor creado:', id_sensor);
        }

        // Asociar el sensor al usuario
        const rows = await connection.query('SELECT id_usuario FROM usuarios WHERE correo = ?', [correo]);
        const id_usuario = rows[0].id_usuario;
        console.log('ID de usuario obtenido:', id_usuario);
        
        // Asociar el sensor al usuario en la tabla senusu
        await connection.query('INSERT INTO senusu (id_usuario, id_sensor) VALUES (?, ?) ON DUPLICATE KEY UPDATE id_sensor = VALUES(id_sensor)', [id_usuario, id_sensor]);
        console.log('Sensor asociado al usuario:', id_sensor, id_usuario);

        res.status(200).send('Sensor asociado al usuario correctamente');
    } catch (error) {
        console.error('Error al asociar el sensor al usuario:', error);
        res.status(500).send('Error del servidor');
    } finally {
        if (connection) {
            connection.release();
        }
    }
};

const obtenerSensorPorCorreo = async (req, res) => {
    const { correo } = req.params;
    let connection;

    try {
        connection = await pool.getConnection();
        console.log('Conexión a la base de datos establecida.');

        // Obtener el sensor asociado al usuario
        const rows = await connection.query('SELECT id_sensor FROM senusu JOIN usuarios ON senusu.id_usuario = usuarios.id_usuario WHERE usuarios.correo = ?', [correo]);
        if (rows.length === 0) {
            console.log('No se encontró ningún sensor asociado al usuario:', correo);
            return res.status(404).send('No se encontró ningún sensor asociado al usuario');
        }

        const id_sensor = rows[0].id_sensor;
        console.log('ID del sensor obtenido:', id_sensor);

        res.status(200).send({ id_sensor });
    } catch (error) {
        console.error('Error al obtener el sensor del usuario:', error);
        res.status(500).send('Error del servidor');
    } finally {
        if (connection) {
            connection.release();
        }
    }
};

const editarNombreSensor = async (req, res) => {
    const { id_sensor } = req.params;
    const { nombre } = req.body;
    let connection;

    try {
        connection = await pool.getConnection();
        console.log('Conexión a la base de datos establecida.');

        // Actualizar el nombre del sensor
        const result = await connection.query('UPDATE sensores SET nombre = ? WHERE id_sensor = ?', [nombre, id_sensor]);
        if (result.affectedRows === 0) {
            console.log('No se encontró ningún sensor con el ID:', id_sensor);
            return res.status(404).send('No se encontró ningún sensor con el ID proporcionado');
        }

        console.log('Nombre del sensor actualizado:', nombre);
        res.status(200).send('Nombre del sensor actualizado correctamente');
    } catch (error) {
        console.error('Error al actualizar el nombre del sensor:', error);
        res.status(500).send('Error del servidor');
    } finally {
        if (connection) {
            connection.release();
        }
    }
};

async function generarDatosSinteticosParaSensores() {
  const sensores = [
      '00:1A:2B:3M:4D:5E',
      '11:2B:2X:3L:4K:5F',
      '22:3C:2T:3U:4H:5G',
      '33:4A:2R:3Q:4G:5H',
      '09:20:21:01:04:03'
  ];
  const tiposMedicion = ['SO3', 'NO2', 'O3', 'Temperatura'];
  const umbrales = {
      'SO3': { bueno: [0, 125], moderado: [126, 350], malo: [351, 500] },
      'NO2': { bueno: [0, 40], moderado: [41, 200], malo: [201, 300] },
      'O3': { bueno: [0, 120], moderado: [121, 180], malo: [181, 300] },
      'Temperatura': [15, 30]
  };
  const puntos = [
      { lat: 39.01810060621549, lon: -0.18162830452004086 }, // Rotonda Ribera Baixa
      { lat: 39.02097808802224, lon: -0.17507040635569954 }, // Gandía Surf
      { lat: 38.996021545908675, lon: -0.15585276580706064 }, // Club Real
      { lat: 38.99576319630499, lon: -0.16683205471027718 } // Repsol
  ];

  function getRandomCoordinate(min, max) {
      return (Math.random() * (max - min) + min).toFixed(6);
  }

  function getRandomValue(range) {
      return (Math.random() * (range[1] - range[0]) + range[0]).toFixed(2);
  }

  function getRandomDate(start, end) {
      return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
  }

  function getRandomValueByDistribution(tipo) {
      const random = Math.random();
      if (random < 0.2) {
          return getRandomValue(umbrales[tipo].malo); // 20% malas
      } else if (random < 0.5) {
          return getRandomValue(umbrales[tipo].moderado); // 30% moderadas
      } else {
          return getRandomValue(umbrales[tipo].bueno); // 50% buenas
      }
  }

  const datos = [];
  const fechaInicio = new Date('2025-01-10T00:00:00');
  const fechaFin = new Date('2025-01-17T23:59:59');

  for (let i = 0; i < sensores.length; i++) {
      for (let j = 0; j < 25; j++) { // 25 bloques de 4 mediciones para cada sensor
          const sensor = sensores[i];
          const fecha_hora = getRandomDate(fechaInicio, fechaFin).toISOString().replace('T', ' ').substring(0, 19);
          const latitud = getRandomCoordinate(puntos[3].lat, puntos[1].lat);
          const longitud = getRandomCoordinate(puntos[0].lon, puntos[2].lon);
          const ubicacion = `{"latitud": ${latitud}, "longitud": ${longitud}}`;

          for (let tipo of tiposMedicion) {
              let valor;
              if (tipo === 'Temperatura') {
                  valor = getRandomValue(umbrales[tipo]);
              } else {
                  valor = getRandomValueByDistribution(tipo);
              }
              datos.push([sensor, fecha_hora, ubicacion, tipo, valor]);
          }
      }
  }

  return datos;
}

async function insertarDatosSinteticos() {
  const datosSinteticos = await generarDatosSinteticosParaSensores();
  let connection;

  try {
    connection = await pool.getConnection();

    // Escapa los valores correctamente.
    const values = datosSinteticos
      .map(
        ([id_sensor, fecha_hora, ubicacion, tipo_medicion, valor]) =>
          `('${id_sensor}', '${fecha_hora}', '${ubicacion.replace(/'/g, "\\'")}', '${tipo_medicion}', ${valor})`
      )
      .join(',');

    const query = `INSERT INTO mediciones (id_sensor, fecha_hora, ubicacion, tipo_medicion, valor) VALUES ${values}`;

    await connection.query(query);
    console.log('Datos sintéticos insertados en la base de datos correctamente');
  } catch (err) {
    console.error('Error al insertar los datos sintéticos en la base de datos:', err);
  } finally {
    if (connection) {
      connection.release();
    }
  }
}

insertarDatosSinteticos();

async function obtenerRolPorCorreo(req, res) {
    const { correo } = req.params;
    let connection;

    try {
        connection = await pool.getConnection();
        console.log('Conexión a la base de datos establecida.');

        // Obtener el id_usuario a partir del correo
        const usuarioQuery = 'SELECT id_usuario FROM usuarios WHERE correo = ?';
        const usuarioRows = await connection.query(usuarioQuery, [correo]);
        if (usuarioRows.length === 0) {
            console.log('Usuario no encontrado:', correo);
            return res.status(404).send('Usuario no encontrado');
        }

        const id_usuario = usuarioRows[0].id_usuario;

        // Obtener el id_rol a partir del id_usuario
        const rolQuery = 'SELECT id_rol FROM usro WHERE id_usuario = ?';
        const rolRows = await connection.query(rolQuery, [id_usuario]);
        if (rolRows.length === 0) {
            console.log('Rol no encontrado para el usuario:', id_usuario);
            return res.status(404).send('Rol no encontrado para el usuario');
        }

        const id_rol = rolRows[0].id_rol;
        res.status(200).send({ id_rol });
    } catch (error) {
        console.error('Error al obtener el rol del usuario:', error);
        res.status(500).send('Error del servidor');
    } finally {
        if (connection) {
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
  verificarUsuario,
  recuperarContrasena,
  editarDatosUsuario,
  encriptarContrasenas,
  enviarCorreoVerificacion,
  enviarCorreoParaVerificacion,
  enviarCorreoEditarDatos,
  enviarCorreoParaEditarDatos,
  enviarCorreoRecuperarContrasena,
  enviarCorreoParaRecuperarContrasena,
  enviarCorreoRestablecerContrasena,
  enviarCorreoParaRestablecerContrasena,
  asociarSensorAUsuario,
  obtenerSensorPorCorreo,
  editarNombreSensor,
  insertarDatosSinteticos,
  obtenerRolPorCorreo
};

