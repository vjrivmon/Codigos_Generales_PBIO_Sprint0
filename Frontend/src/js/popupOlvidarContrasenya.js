// Este código se encarga de mostrar un popup para que el usuario pueda introducir su número de teléfono y recibir un correo con un enlace para reestablecer su contraseña.
// Se encarga de validar que el número de teléfono introducido sea correcto y de enviar un correo al usuario con un enlace para reestablecer su contraseña.
// Falta por implementar el envío del correo al usuario.

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
confirmBtn.addEventListener('click', async function(event) { 
    // Validar que el campo no esté vacío y tenga exactamente 9 dígitos
    if (!popupPhone.value) {
        alert('Rellene el campo con su número de teléfono.');
        return;
    } else if (popupPhone.value.length !== 9) {
        alert("El número de teléfono debe tener exactamente 9 dígitos.");
        return;
    } else {
        try {
            // Fetch the email of the user with id_usuario 8
            const response = await fetch('http://localhost:8080/usuarios/8');
            if (!response.ok) {
                throw new Error('Error al obtener el correo del usuario: ' + response.status);
            }
            const userData = await response.json();
            const email = userData[0].correo;
            console.log('Email del usuario es:', email);
            // Send password reset email
            const correoResponse = await fetch('http://localhost:8080/recuperar-contrasena', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: email })
            });

            if (!correoResponse.ok) {
                throw new Error('Error al enviar el correo de restablecer contrasena: ' + correoResponse.status);
            }

            alert('Se ha enviado un correo para reestablecer su contraseña al correo asociado al teléfono que nos ha proporcionado.');
            popup.style.display = 'none'; // Oculta el popup
        } catch (error) {
            console.error('Error:', error);
            alert('Ocurrió un error al intentar enviar el correo de restablecer contraseña.');
        }
    }
});

/*----------------------- CANCELAR  ------------------------*/
cancelBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // Oculta el popup
    popupPhone.value = ''; // Limpiar el campo de teléfono
});
