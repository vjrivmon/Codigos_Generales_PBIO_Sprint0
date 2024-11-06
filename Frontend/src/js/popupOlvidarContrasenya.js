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

// Cuando se hace clic en "Confirmar"
confirmBtn.addEventListener('click', function() {
   
});

// Cuando se hace clic en "Cancelar"
cancelBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // Oculta el popup
});


/*--------------------- TELEFONO -------------------------*/
// Escuchar el evento de entrada de texto
popupPhone.addEventListener("input", function() {
    // Eliminar todo lo que no sea un número
    this.value = this.value.replace(/[^0-9]/g, '');
});

// Validación de longitud al perder el foco
popupPhone.addEventListener("blur", function() {
    if (this.value.length !== 9) {
        alert("El número de teléfono debe tener exactamente 9 dígitos.");
        this.focus(); // Volver a enfocar el campo
    }
});



/*-------------------- CONFIRMAR --------------------------*/
document.getElementById('mandarCorreoNuevaContrasenya').addEventListener('click', function(event) { 
    // Validar que los campos no estén vacíos
    if (!popupPhone) {
        alert('Rellene el campo con su número de teléfono.'); // Mensaje de error
        return; // Salir de la función si hay campos vacíos
    }
	else {
		alert('Se ha enviado un correo para reestablecer su contraseña al correo asociado al teléfono que nos ha proporcionado.'); // Mensaje de error
		/* ENVIAR CORREO AL USUARIO - FALTA POR IMPLEMENTAR*/
		    popup.style.display = 'none'; // Oculta el popup

	}
});
