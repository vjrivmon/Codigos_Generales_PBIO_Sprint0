// Este archivo contiene el código JavaScript necesario para manejar el registro e inicio de sesión de usuarios en la aplicación web.
// Se encarga de enviar los datos de registro al backend y de verificar los datos de inicio de sesión. También maneja la redirección 
// a la página principal de la aplicación una vez que el usuario ha iniciado sesión correctamente.
// Requiere un backend que responda a las rutas '/usuarios' y '/usuarios/verificar' con las operaciones de registro e inicio de sesión, 
// respectivamente.

// Obtener los elementos del DOM
const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => { // Evento de clic en el botón de registrarse
    container.classList.add("active"); 
});

loginBtn.addEventListener('click', () => { // Evento de clic en el botón de iniciar sesión
    container.classList.remove("active");
});

document.getElementById("privacy-policy").addEventListener("change", function() {  // Evento de cambio en la casilla de verificación de política de privacidad
            document.getElementById("register-btn").disabled = !this.checked; // Habilitar o deshabilitar el botón de registro según el estado de la casilla
        });

// Función para registrar un nuevo usuario 
// Txt, Txt, N, Txt -> registrarUsuario() ->
// - email: correo electrónico del usuario
// - password: contraseña del usuario
// - phone: teléfono del usuario
// - name: nombre del usuario
// La función envía los datos de registro al backend y muestra un mensaje de éxito o error al usuario.
// Si el registro es exitoso, se envía un correo de verificación al usuario.
// Si ocurre un error, se muestra un mensaje de error al usuario.

async function registrarUsuario(email, password, phone, name) { 
    try {
        const response = await fetch('http://localhost:8080/usuarios', { // Cambiado para que use localhost
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ correo: email, contrasena: password, telefono: phone, nombre: name }) // Asegúrate de que estos nombres coincidan con los de tu base de datos
        });

        if (!response.ok) {
            throw new Error('Error en la respuesta de la API: ' + response.status);
        }

        const data = await response.json();
        alert('Usuario registrado exitosamente! Por favor, verifica tu correo.'); // Mensaje de éxito

        // Enviar correo de verificación
        const correoResponse = await fetch('http://localhost:8080/verificar-correo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        });

        if (!correoResponse.ok) {
            throw new Error('Error al enviar el correo de verificación: ' + correoResponse.status);
        }

        container.classList.remove("active"); // Volver a la vista de inicio de sesión
    } catch (error) {
        console.error('Error al registrar el usuario:', error);
        alert('Ocurrió un error al registrar el usuario. Inténtalo de nuevo más tarde.');
    }
}

// Función para verificar si un correo ha sido verificado
// Txt, txt -> verificarCorreo() -> V/F
// - email: correo electrónico del usuario
// - password: contraseña del usuario
// La función envía una solicitud al backend para verificar si el correo ha sido verificado.
async function ConsultarDatosUsuario(email, password) {
    try {
        const response = await fetch('http://localhost:8080/usuarios/verificar', { // Cambiado para que use localhost
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ correo: email, contrasena: password })
        });

        const result = await response.json();

        // ---------------------------- Antes de tocar esto, consultar esto con Vicente, se puede romper el código ----------------------------
        // ------------------------------------------------------------------------------------------------------------------------------------
        if (response.ok) {
            if (result.success) {
                // const correoVerificado = await verificarCorreo(email);
                // if (!correoVerificado) {
                //     alert('Por favor, verifica tu correo antes de iniciar sesión.');
                //     return;
                // }
                // Almacenar el id_usuario en una cookie segura
                document.cookie = `id_usuario=${result.id_usuario}; path=/; secure; SameSite=Strict`;
                console.log(`id_usuario almacenado en cookie: ${result.id_usuario}`);
                // Redirigir según el correo electrónico
                if (email.trim().toLowerCase() === 'irene08@gmail.com') {
                    window.location.href = 'admin.html';
                } else {
                    window.location.href = 'datosYMapa.html';
                }
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
        alert('Porfavor verifica tu correo, para poder iniciar sesión');
    }
    // ------------------------------------------------------------------------------------------------------------------------------------

}

// Manejar el evento de clic en el botón de registrarse
document.getElementById('register-btn').addEventListener('click', function(event) {
    event.preventDefault(); // Evitar el envío del formulario

    const email = document.getElementById('signUpEmail').value; // Obtener el correo
    const password = document.getElementById('signUpPassword').value; // Obtener la contraseña
    const phone = document.getElementById('signUpPhone').value; // Obtener el teléfono
    const name = document.getElementById('signUpName').value; // Obtener el nombre

    // Validar que los campos no estén vacíos
    if (!email || !password || !phone || !name) {
        alert('Todos los campos son obligatorios.'); // Mensaje de error
        return; // Salir de la función si hay campos vacíos
    }
    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Expresión regular para validar el formato de email. Debe contener @ y al menos un punto. 
    if (!emailRegex.test(email)) {
        alert('Por favor, introduce un email válido.'); // Mensaje de error
        return; // Salir de la función si el email no es válido
    }
    // Validar formato de contraseña
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{7,200}$/; 
    // La contraseña debe tener mínimo 8 caracteres, incluir al menos una mayúscula, una minúscula, un número y un carácter especial (!@#$%^&*)
    if (!passwordRegex.test(password)) {
        alert('La contraseña debe tener mínimo 8 caracteres, incluir al menos una mayúscula, una minúscula, un número y un carácter especial (!@#$%^&*)'); // Mensaje de error
        return; // Salir de la función si la contraseña no es válida
    }
    registrarUsuario(email, password, phone, name); // Llamar a la función para registrar el usuario
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
