import nodemailer from "nodemailer";
rt dotenv from  "dotenv";dotenv.config();
const transporter= nodemailer.createTransport({
    host:process.env.EMAIL_HOST,
    port:587,
    secure:true,
    auth: {
        user:process.env.EMAIL_USER,
        pass:process.env.EMAIL_PASSWORD,
    }
})


export async function enviarCorreoVerificarCorreo(direccion, token) {
    transporter.sendMail({
        from: "VIMYP <vimyp.s.l@gmail.com>",
        to: direccion,
        subject:"Verificación de nueva cuenta -  VIMYP"
        html: crearMailVerificacion(token)
    })
    
}

function crearMailVerificacion(token){
    return `
    <!DOCTYPE html> <html lang="es">
<style>
html{
background-color: Iwhite;
}
body {
max-width: 600px;
font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif
margin: auto;
background-color:
padding: 40px;
border-radius: 4px;
Irgb(229, 255, 246);
margin-top: 10px;
}
</style>
<body>
<h1>Verificación de correo electrónico - puntoJson.com</h1>
<p>Se ha creado una cuenta en vimyp.com con este correo electronico
<p>Si esta cuenta no fue creada por usted, desestime este correo <p>

</p>Si usted creó la cuenta, entonces verifique la cuenta 
<a href="http://192.168.0.101:8080/verificar/${token}" target="_blank" rel="noopener noreferr"> haciendo click aquí</a>.             <p>
    <strong>Calo</strong></p>

----<p>CEO PuntoJson.</p>
</body>
</html>
`
}