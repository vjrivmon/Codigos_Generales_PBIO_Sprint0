// Manejar el evento de clic en el botón de registrarse
document.getElementById('resetPasswordBtn').addEventListener('click', function(event) {
    event.preventDefault(); // Evitar el envío del formulario
    const newPass = document.getElementById('newPassword').value; // Obtener el correo
    const newPassConfirm = document.getElementById('confirmPassword').value; // Obtener la contraseña

    
    // Validar que los campos no estén vacíos
    if (!newPass || !newPassConfirm) {
        alert('Todos los campos son obligatorios.'); // Mensaje de error
        return; // Salir de la función si hay campos vacíos
    }
    
    // Validar formato de contraseña
	const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{7,200}$/;
    if (!passwordRegex.test(newPass)) {
        alert('La contraseña debe tener mínimo 8 caracteres, incluir al menos una mayúscula, una minúscula, un número y un carácter especial (!@#$%^&*)'); // Mensaje de error
        return; // Salir de la función si la contraseña no es válida
    }
	
    alert('Se han confirmado los cambios, revisa tu bandeja de entrada'); // Mensaje de error

	// Llamar a la función para enviar el correo
    //enviarCorreo(email);
	
});