const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

import { enviarCorreoVerificarCorreo } from "./mail.service.js";

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});

document.getElementById("privacy-policy").addEventListener("change", function() {
            document.getElementById("register-btn").disabled = !this.checked;
        });

// Función para registrar un nuevo usuario
async function registrarUsuario( email, password) {
    try {
        const response = await fetch('http://192.168.0.101:8080/usuarios', { // Ip Torre Pablo
        //const response = await fetch('http://172.20.10.11:8080/usuarios', { // Ip Portatil Pablo Wifi Pablo
        //const response = await fetch('http://192.168.0.20:8080/usuarios', { // Ip Ordenador Vicente
        //const response = await fetch('http://192.168.0.17:8080/usuarios', { // Ip Ordenador Irene
        //const response = await fetch('http://172.20.10.5:8080/usuarios', { // Ip Portátil visi Universidad
        
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ correo: email, contrasena: password }) // Asegúrate de que estos nombres coincidan con los de tu base de datos
        });

        if (!response.ok) {
            throw new Error('Error en la respuesta de la API: ' + response.status);
        }

        const data = await response.json();
        alert('Usuario registrado exitosamente!'); // Mensaje de éxito

        // Opcionalmente, puedes redirigir al usuario al iniciar sesión
        container.classList.remove("active"); // Volver a la vista de inicio de sesión
    } catch (error) {
        console.error('Error al registrar el usuario:', error);
        alert('Ocurrió un error al registrar el usuario. Inténtalo de nuevo más tarde.');
    }
}
async function ConsultarDatosUsuario(email, password) {
    try {
      // Codificar la contraseña antes de enviarla (esto solo es para fines ilustrativos, NO es seguro)
      const encodedPassword = encodeURIComponent(password);
  
       //Realizar una solicitud GET al servidor con los parámetros
      const response = await fetch(`http://192.168.0.101:8080/usuarios?correo=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(password)}`, { //  IP Torre Pablo 
      //const response = await fetch(`http://172.20.10.11:8080/usuarios?correo=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(password)}`, { // IP Portatil Pablo Wifi Pablo
      //const response = await fetch(`http://192.168.0.20:8080/usuarios?correo=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(password)}`, { // Ip ordenador vicente
      //const response = await fetch(`http://192.168.0.17:8080/usuarios?correo=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(password)}`, { // Ip ordenador vicente
      //const response = await fetch(`http://172.20.10.5:8080/usuarios?correo=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(password)}`, { //  Ip Portátil visi Universidad
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      const result = await response.json();
  
      if (response.ok) {
        if (result.success) {
          window.location.href = 'datosYMapa.html';
        } else {
          alert('Contraseña incorrecta');
        }
      } else if (result.error === 'Usuario no existe') {
        alert('El usuario no existe');
      } else {
        alert('Ocurrió un error inesperado');
      }
    } catch (error) {
      console.error('Error al verificar el usuario:', error);
      alert('Ocurrió un error al conectar con el servidor');
    }
  }
  
  

// Manejar el evento de clic en el botón de registrarse
document.getElementById('register-btn').addEventListener('click', function(event) {
    event.preventDefault(); // Evitar el envío del formulario
    const email = document.getElementById('signUpEmail').value; // Obtener el correo
    const password = document.getElementById('signUpPassword').value; // Obtener la contraseña
    const phone = document.getElementById('signUpPhone').value; // Obtener la contraseña
    const name = document.getElementById('signUpName').value; // Obtener la contraseña

    
    // Validar que los campos no estén vacíos
    if (!email || !password || !phone || !name) {
        alert('Todos los campos son obligatorios.'); // Mensaje de error
        return; // Salir de la función si hay campos vacíos
    }
    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Expresión regular para validar el formato de email
    if (!emailRegex.test(email)) {
        alert('Por favor, introduce un email válido.'); // Mensaje de error
        return; // Salir de la función si el email no es válido
	}
    // Validar formato de contraseña
	const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{7,200}$/;
    if (!passwordRegex.test(password)) {
        alert('La contraseña debe tener mínimo 8 caracteres, incluir al menos una mayúscula, una minúscula, un número y un carácter especial (!@#$%^&*)'); // Mensaje de error
        return; // Salir de la función si la contraseña no es válida
    }
    registrarUsuario( email, password); // Llamar a la función para registrar el usuario3
		
});

// Manejar el evento de clic en el botón de iniciar sesion
document.getElementById('botonIniciarSesion').addEventListener('click', function(event) { 
    event.preventDefault(); // Evitar el envío del formulario

    const email = document.getElementById('loginEmail').value; // Obtener el correo 
    const password = document.getElementById('loginPassword').value; // Obtener la contraseña 
    // Validar que los campos no estén vacíos
    if (!email || !password) {
        alert('Todos los campos son obligatorios.'); // Mensaje de error
        return; // Salir de la función si hay campos vacíos
    }
   
    ConsultarDatosUsuario( email, password); // Llamar a la función para comprobarel usuario 
});
