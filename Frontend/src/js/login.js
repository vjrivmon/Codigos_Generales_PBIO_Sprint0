const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});

// Inicio Sesion de usuario
async function verificarUsuario(email, password) {
    try {
        const response = await fetch('http://192.168.0.101:8080/usuarios'); // Obtener todos los usuarios
        if (!response.ok) {
            throw new Error('Error en la respuesta de la API: ' + response.status);
        }

        const usuarios = await response.json();

        // Buscar el usuario por correo electrónico
        const usuarioEncontrado = usuarios.find(user => user.correo === email); // Cambiar 'email' a 'correo'

        if (!usuarioEncontrado) {
            alert('Usuario no existente'); // Alerta si el usuario no existe
            return;
        }

        // Comparar la contraseña
        if (usuarioEncontrado.contrasena === password) { // Cambiar 'password' a 'contrasena'
            // Si la contraseña es correcta, redirigir a datosYMapa.html
            window.location.href = 'datosYMapa.html';
        } else {
            alert('Contraseña errónea'); // Alerta si la contraseña no es correcta
        }

    } catch (error) {
        console.error('Error al verificar el usuario:', error);
        alert('Ocurrió un error al verificar el usuario. Inténtalo de nuevo más tarde.');
    }
}

// Manejar el evento de clic en el botón de iniciar sesión
document.getElementById('botonIniciarSesion').addEventListener('click', function() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    verificarUsuario(email, password); // Llamar a la función para verificar el usuario
});
