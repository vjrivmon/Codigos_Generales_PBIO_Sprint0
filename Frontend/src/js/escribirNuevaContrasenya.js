// Referencias a los elementos del DOM
const popupConfirmarNuevaContrasenya = document.getElementById('popup-cambiar-contrasenya');
const cancelBtnContra = document.getElementById('cancelBtnContrasenya');
const confirmBtnContra = document.getElementById('confirmBtnContrasenya');
const resetPasswordBtn = document.getElementById('resetPasswordBtn');

// Evento del botón "Restablecer Contraseña"
resetPasswordBtn.addEventListener('click', function (event) {
    event.preventDefault(); // Evitar envío del formulario

    // Obtener los valores actuales de los campos
    const currentNewPass = document.getElementById('newPassword').value.trim();
    const currentNewPassConfirm = document.getElementById('confirmPassword').value.trim();

    // Mostrar el popup
    popupConfirmarNuevaContrasenya.style.display = 'flex';

    // Evento para confirmar los cambios
    confirmBtnContra.addEventListener('click', function confirmar() {
        // Validar los campos con if, else if, else
        if (!currentNewPass || !currentNewPassConfirm) {
            // Si algún campo está vacío
            alert('Todos los campos son obligatorios.');
            popupConfirmarNuevaContrasenya.style.display = 'none';
        } else if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/.test(currentNewPass)) {
            // Si el formato de la contraseña es inválido
            alert(
                'La contraseña debe tener mínimo 8 caracteres, incluir al menos una mayúscula, una minúscula, un número y un carácter especial (!@#$%^&*).'
            );
            popupConfirmarNuevaContrasenya.style.display = 'none';
        } else if (currentNewPass !== currentNewPassConfirm) {
            // Si las contraseñas no coinciden
            alert('Las contraseñas no coinciden.');
            popupConfirmarNuevaContrasenya.style.display = 'none';
        } else {
            // Si todas las validaciones son correctas
            alert('Se han confirmado los cambios. Revisa tu bandeja de entrada.');
            popupConfirmarNuevaContrasenya.style.display = 'none';
        }

        // Remover el listener para evitar duplicados si se vuelve a abrir el popup
        confirmBtnContra.removeEventListener('click', confirmar);
    });

    // Evento para cancelar el cambio de contraseña
    cancelBtnContra.addEventListener('click', function cancelar() {
        popupConfirmarNuevaContrasenya.style.display = 'none';

        // Remover listener para evitar duplicados
        cancelBtnContra.removeEventListener('click', cancelar);
    });
});
