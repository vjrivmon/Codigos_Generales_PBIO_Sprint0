// Este código se encarga de mostrar un popup para que el usuario pueda reestablecer su contraseña en caso de que la haya olvidado.
// Se le pedirá al usuario que introduzca su número de teléfono y se le enviará un correo con un enlace para reestablecer su contraseña.
// Si el usuario no introduce un número de teléfono, se le mostrará una alerta pidiéndole que lo introduzca.
// Si el usuario introduce un número de teléfono, se le enviará un correo con un enlace para reestablecer su contraseña y se cerrará el popup.
// Si el usuario hace clic en el botón de cancelar, se cerrará el popup sin hacer nada más.


const popupContrasenya = document.getElementById('popupOlvidado');
const olvideContrasenyaBtn = document.getElementById('olvideContrasenyaBtn');
const confirmBtnOlvidada = document.getElementById('mandarCorreoNuevaContrasenya');
const cancelBtnOlvidada = document.getElementById('cancelBtn');
const popupPhone = document.getElementById('popupPhone');

// Mostrar el popup cuando se hace clic en "¿Olvidaste tu contraseña?"
olvideContrasenyaBtn.addEventListener('click', function() {
    console.log("Botón clickeado"); // Verificar si el evento está funcionando
    popupContrasenya.style.display = 'flex'; // Asegúrate de que se cambia a 'flex' y no solo 'block'
});

// Escuchar el evento de entrada de texto para permitir solo números
popupPhone.addEventListener("input", function() {
    this.value = this.value.replace(/[^0-9]/g, '');
});

// Confirmar y cerrar el popup con validación
confirmBtnOlvidada.addEventListener('click', async function(event) {
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

// Cancelar y cerrar el popup
cancelBtnOlvidada.addEventListener('click', function() {
    popupContrasenya.style.display = 'none';
    popupPhone.value = ''; // Limpiar el campo de teléfono
});
