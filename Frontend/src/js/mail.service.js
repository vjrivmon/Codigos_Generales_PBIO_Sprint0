import nodemailer from "nodemailer";
import dotenv from "dotenv";
dotenv.config();

const transporter = nodemailer.createTransport({
    host: process.env.EMAIL_HOST,
    port: 465,
    secure: true,
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASSWORD,
    }
});

export async function enviarCorreoVerificarCorreo(direccion) {
    transporter.sendMail({
        from: "VIMYP <vimyp.s.l@gmail.com>",
        to: direccion,
        subject: "Verificación de nueva cuenta - VIMYP",
        html: crearMailVerificacion()
    });
}

export async function enviarCorreoDatosUsuarioCambiados(direccion) {
    transporter.sendMail({
        from: "VIMYP <vimyp.s.l@gmail.com>",
        to: direccion,
        subject: "Cambios datos perfil - VIMYP",
        html: crearMailDatosUsuarioCambiados()
    });
}

function crearMailVerificacion() {
    return `
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
          color: #3B3B3B !important;
        }
        .button {
          display: inline-block;
          padding: 10px 20px;
          margin-top: 20px;
          text-decoration: none !important;
          border-radius: 33px;
          font-weight: bold;
          font-size: 1.1rem;
          transition: background-color 0.3s;
          background-color: #395886;
          border: 2px solid #395886;
          color: white !important;
        }
        .button:hover {
          background-color: white;
          color: #395886 !important;
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
        <a href="https://www.tusitio.com" class="button">Confirmar</a>
        <p>¿No has sido tú? Si no solicitaste este registro, por favor ignora este mensaje o contáctanos.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
    `;
}

function crearMailDatosUsuarioCambiados() {
    return `
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
          color: #3B3B3B !important;
        }
        .button {
          display: inline-block;
          padding: 10px 20px;
          margin-top: 20px;
          text-decoration: none !important;
          border-radius: 33px;
          font-weight: bold;
          font-size: 1.1rem;
          transition: background-color 0.3s;
          background-color: #395886;
          border: 2px solid #395886;
          color: white !important;
        }
        .button:hover {
          background-color: white;
          color: #395886 !important;
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
        <p>Acabamos de recibir una petición para cambiar los datos de tu perfil.</p>
        <p>Haciendo click en este botón confirmarás la intención de la petición y se guardarán correctamente.</p>
        <a href="https://www.tusitio.com" class="button">Confirmar</a>
        <p>¿No has sido tú? Si no solicitaste este cambio de datos, por favor ignora este mensaje o contáctanos.</p>
      </div>
      <div class="footer">
        © 2024 VIMYP. Todos los derechos reservados.
      </div>
    </body>
    </html>
    `;
}