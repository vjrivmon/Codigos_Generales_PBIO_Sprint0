/*-------------------------------- APARECER POPUP ---------------------------------*/
const popup = document.getElementById('popup');
const olvideContrasenyaBtn = document.getElementById('olvideContrasenyaBtn');
const confirmBtn = document.getElementById('mandarCorreoNuevaContrasenya');
const cancelBtn = document.getElementById('cancelBtn');
const popupPhone = document.getElementById('popupPhone'); // Referencia al contenedor del teléfono

// Mostrar el popup cuando se hace clic en "¿Has olvidado tu contraseña?"
olvideContrasenyaBtn.addEventListener('click', function() {
    popup.style.display = 'flex'; 
});

/*--------------------- TELEFONO -------------------------*/
// Escuchar el evento de entrada de texto
popupPhone.addEventListener("input", function() {
    // Eliminar todo lo que no sea un número
    this.value = this.value.replace(/[^0-9]/g, '');
});

/*-------------------- CONFIRMAR --------------------------*/
confirmBtn.addEventListener('click', function(event) { 
    // Validar que el campo no esté vacío y tenga exactamente 9 dígitos
    if (!popupPhone.value) {
        alert('Rellene el campo con su número de teléfono.');
        return;
    } else if (popupPhone.value.length !== 9) {
        alert("El número de teléfono debe tener exactamente 9 dígitos.");
        return;
    } else {
        alert('Se ha enviado un correo para reestablecer su contraseña al correo asociado al teléfono que nos ha proporcionado.');
        /* ENVIAR CORREO AL USUARIO - FALTA POR IMPLEMENTAR*/
        popup.style.display = 'none'; // Oculta el popup
    }
});

/*----------------------- CANCELAR  ------------------------*/
cancelBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // Oculta el popup
    popupPhone.value = ''; // Limpiar el campo de teléfono
});
