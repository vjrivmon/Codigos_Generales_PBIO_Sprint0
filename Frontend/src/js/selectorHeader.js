// Función para obtener el valor de una cookie por nombre
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

// Verificar si la sesión está activa
const sessionActive = getCookie('session_active') === 'true';

// Mostrar u ocultar headers basado en el estado de la sesión
if (sessionActive) {
    document.getElementById('header_usuario').style.display = 'block';
    document.getElementById('header_publico').style.display = 'none';
} else {
    document.getElementById('loggedInHeader').style.display = 'none';
    document.getElementById('loggedOutHeader').style.display = 'block';
}
