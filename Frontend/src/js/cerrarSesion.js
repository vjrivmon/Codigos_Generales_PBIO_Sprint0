// Selección de elementos
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
