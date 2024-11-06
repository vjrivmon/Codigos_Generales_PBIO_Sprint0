/*-------------------------------- APARECER POPUP ---------------------------------*/
const popup = document.getElementById('popup');
const olvideContrasenyaBtn = document.getElementById('olvideContrasenyaBtn');
const confirmBtn = document.getElementById('mandarCorreoNuevaContrasenya');
const cancelBtn = document.getElementById('cancelBtn');
const phoneContainer = document.getElementById('phone-container'); // Referencia al contenedor del teléfono

// Mostrar el popup cuando se hace clic en "¿Has olvidado tu contraseña?"
olvideContrasenyaBtn.addEventListener('click', function() {
    popup.style.display = 'flex'; 
});

// Cuando se hace clic en "Confirmar"
confirmBtn.addEventListener('click', function() {
   
});

// Cuando se hace clic en "Cancelar"
cancelBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // Oculta el popup
});
