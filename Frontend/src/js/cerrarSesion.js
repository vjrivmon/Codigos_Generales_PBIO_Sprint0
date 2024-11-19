//Este código sirve para cerrar la sesión de un usuario en la web. Se implementa un popup que se muestra al hacer clic 
//en el botón de cerrar sesión, y se cierra al hacer clic en el botón de cancelar o confirmar. Al confirmar, se redirige 
//al usuario a la página de inicio. Si cancelas, se cierra el popup y no se realiza ninguna acción.

// Seleccionar el botón de cerrar sesión, el popup y los botones de confirmar y cancelar
const logoutBtn = document.getElementById('logoutBtn');
const popup = document.getElementById('popup-cerrar-sesion');
const confirmBtn = document.getElementById('confirmBtn-cerrar');
const cancelBtn = document.getElementById('cancelBtn-cerrar');

// Abrir el popup
logoutBtn.addEventListener('click', () => {
    popup.style.display = 'flex'; // Muestra el popup
});

// Cerrar el popup al cancelar
cancelBtn.addEventListener('click', () => {
    popup.style.display = 'none'; // Oculta el popup
});

// Confirmar cierre de sesión
confirmBtn.addEventListener('click', () => {
    // Aquí redirige o realiza una acción específica, por ejemplo:
    window.location.href = '../index.html'; // Redirige al inicio
});
