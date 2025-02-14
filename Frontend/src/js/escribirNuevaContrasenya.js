// Referencias a los elementos del DOM
const popupConfirmarNuevaContrasenya = document.getElementById('popup-cambiar-contrasenya');
const cancelBtnContra = document.getElementById('cancelBtnContrasenya');
const confirmBtnContra = document.getElementById('confirmBtnContrasenya');
const resetPasswordBtn = document.getElementById('resetPasswordBtn');

// Función para obtener los datos del usuario con id_usuario 3 desde la base de datos
async function obtenerDatosUsuario() {
    try {
        const response = await fetch('http://localhost:8080/usuarios/8');
        if (!response.ok) {
            throw new Error('Error al obtener los datos del usuario');
        }
        const usuario = await response.json();
        return usuario[0];
    } catch (error) {
        console.error('Error:', error);
        return {};
    }
}

/* ----------- Para que salga rojo o verde si las contraseñas coinciden o no ----------- */
document.addEventListener('DOMContentLoaded', async function () {
    const passwordInput = document.getElementById("newPassword");
    const passwordConfirmInput = document.getElementById("confirmPassword"); // Cambié el ID para que coincida

    // Mostrar los datos del usuario con id_usuario 3
    const usuario = await obtenerDatosUsuario();
    const correoInput = document.getElementById('correo');
    correoInput.value = usuario.correo;

    // Función para verificar si las contraseñas coinciden
    function checkPasswordsMatch() {
        const password = passwordInput.value;
        const confirmPassword = passwordConfirmInput.value;

        // Cambiar el borde según si coinciden o no
        if (password && confirmPassword) {
            if (password === confirmPassword) {
                passwordConfirmInput.style.border = "2px solid green"; // Borde verde si coinciden
            } else {
                passwordConfirmInput.style.border = "2px solid red"; // Borde rojo si no coinciden
            }
        } else {
            passwordConfirmInput.style.border = ""; // Sin borde si está vacío
        }
    }

    // Añadir los eventos de escucha a ambos campos de contraseña
    passwordInput.addEventListener("input", checkPasswordsMatch);
    passwordConfirmInput.addEventListener("input", checkPasswordsMatch);
});

// Evento del botón "Restablecer Contraseña"
resetPasswordBtn.addEventListener('click', function (event) {
    event.preventDefault(); // Evitar envío del formulario

    // Obtener los valores actuales de los campos
    const currentNewPass = document.getElementById('newPassword').value.trim();
    const currentNewPassConfirm = document.getElementById('confirmPassword').value.trim();

    // Mostrar el popup
    popupConfirmarNuevaContrasenya.style.display = 'flex';

    // Evento para confirmar los cambios
    confirmBtnContra.addEventListener('click', async function confirmar() {
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
            const usuario = await obtenerDatosUsuario();
            const datosUsuario = {
                id_usuario: 8,
                nombre: usuario.nombre,
                telefono: usuario.telefono,
                correo: usuario.correo,
                contrasena: currentNewPass
            };

            // Enviar los datos al servidor
            const response = await fetch(`http://localhost:8080/usuarios/8`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(datosUsuario)
            });

            if (!response.ok) { // Verificar si la respuesta tuvo éxito
                throw new Error('No se pudieron actualizar los datos del usuario');
            }

            const resultado = await response.text(); // Obtener el mensaje de éxito desde la respuesta del servidor

            alert(resultado); // Muestra mensaje de éxito
            const email = usuario.correo;
            console.log('Email del usuario es:', email);
            // Enviar correo de verificación edición de contraseña
            const correoResponse = await fetch('http://localhost:8080/restablecer-contrasena', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: email }) // Asegúrate de enviar el correo correcto
            });

            if (!correoResponse.ok) {
                throw new Error('Error al enviar el correo de restablecer contrasena: ' + correoResponse.status);
            }
            alert('Se ha enviado un correo para reestablecer su contraseña al correo asociado al teléfono que nos ha proporcionado.');

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
