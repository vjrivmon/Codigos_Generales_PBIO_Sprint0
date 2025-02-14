// Este script envía un correo electrónico al usuario cuando se solicita un cambio en su perfil de usuario.
// Se utiliza nodemailer para enviar correos electrónicos y se envía un correo electrónico con un botón de confirmación.
// El botón de confirmación lleva al usuario a la URL del sitio web donde se guardan los cambios.
// Se adjunta un logotipo en el correo electrónico y se muestra en el cuerpo del correo electrónico.
// Se debe cambiar la dirección de correo electrónico del remitente, la dirección de correo electrónico del destinatario y la URL del sitio web en el botón de confirmación.
// Se debe cambiar la ruta de la imagen del logotipo en el sistema local.

var nodemailer = require('nodemailer');


var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'irenebati4@gmail.com',
        pass: 'uins wbqo poyu yjon'
    }
});

var mailOptions = {
    from: 'irenebati4@gmail.com',
    to: 'pablomeana13@gmail.com',
    subject: 'Cambios en el perfil de usuario',
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
        <h1>Has cambiado los datos de tu perfil</h1>
      </div>
      <div class="content">
        <p>Estimado usuario,</p>
        <p>acabamos de recibir una peticion para cambiar los datos de tu perfil, </p>
        <p>Haciendo click en este botón confirmaras la intencion de la petición y se guardaran correctamente</p>
        <a href="https://www.tusitio.com" class="button">Confirmar</a> <!---- Cambiar por la URL del sitio web ---->	
        <p>¿No has sido tú? Si no solicitaste este cambio de datos, por favor ignora este mensaje o contáctanos.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
  `,
	attachments: [
        {
            filename: 'logo.svg',
            path: '../img/logo.svg', // La ruta de la imagen en tu sistema local
            cid: 'logo' // Este ID debe coincidir con el src en la etiqueta <img>
        }
    ]
};

transporter.sendMail(mailOptions, function (error, info) {
    if (error) {
        console.log(error);
    } else {
        console.log('Email enviado: ' + info.response);
    }
});
