// Asegurarse de que el DOM esté completamente cargado antes de ejecutar el código
document.addEventListener('DOMContentLoaded', function() {
    // Obtener elementos del DOM
    const popup = document.getElementById('popup2');
    const editBtn = document.getElementById('editBtn');
    const confirmBtn = document.getElementById('confirmBtn2');
    const cancelBtn = document.getElementById('cancelBtn2');
    const emailInput = document.getElementById('userEmail');
    const passwordInput = document.getElementById('userPassword');
    const nameInput = document.getElementById('userName');
    const phoneInput = document.getElementById('userPhone');

    // Verificar que todos los elementos existen
    if (!popup || !editBtn || !confirmBtn || !cancelBtn || !emailInput || !passwordInput || !nameInput || !phoneInput) {
        console.error('Error: Uno o más elementos del DOM no se encontraron.');
        return;
    }

    // Función para habilitar la edición de los campos
    editBtn.addEventListener('click', function() {
        emailInput.disabled = false; // Habilitar campo de correo
        passwordInput.disabled = false; // Habilitar campo de contraseña
        nameInput.disabled = false; // Habilitar campo de nombre
        phoneInput.disabled = false; // Habilitar campo de teléfono
        confirmBtn.disabled = false; // Habilitar botón de guardar cambios
        editBtn.disabled = true; // Deshabilitar botón de editar datos
    });

    // Mostrar popup al hacer clic en "Guardar cambios"
    confirmBtn.addEventListener('click', function() {
        popup.style.display = 'flex'; // Mostrar popup
    });

    // Confirmar cambios al hacer clic en "Confirmar"
    confirmBtn.addEventListener('click', async function() {
        const email = emailInput.value;
        const password = passwordInput.value;
        const name = nameInput.value;
        const phone = phoneInput.value;

        // Obtener id_usuario de la cookie
        const id_usuario = getCookie('id_usuario');
        if (!id_usuario) {
            alert('No se pudo obtener el ID del usuario.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/usuarios/${encodeURIComponent(id_usuario)}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ correo: email, contrasena: password, telefono: phone, nombre: name })
            });

            if (!response.ok) {
                throw new Error('Error en la respuesta de la API: ' + response.status);
            }

            const data = await response.json();
            console.log('Cambios guardados:', data);

            // Cerrar popup
            popup.style.display = 'none';

            // Deshabilitar campos nuevamente y habilitar botón de editar
            emailInput.disabled = true;
            passwordInput.disabled = true;
            nameInput.disabled = true;
            phoneInput.disabled = true;
            confirmBtn.disabled = true;
            editBtn.disabled = false; // Volver a habilitar botón de editar
            alert('Te hemos enviado un correo para verificar los cambios, por favor compruébalo'); // Mensaje de confirmación
        } catch (error) {
            console.error('Error al guardar los cambios:', error);
            alert('Ocurrió un error al guardar los cambios. Inténtalo de nuevo más tarde.');
        }
    });

    // Cerrar popup al hacer clic en "Cancelar"
    cancelBtn.addEventListener('click', function() {
        popup.style.display = 'none'; // Cerrar popup sin guardar cambios
    });
    
    // ---------------------------- Antes de tocar esto, consultar esto con Vicente, se puede romper el código ----------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------
    // Función para obtener el valor de una cookie por su nombre
    // Llamada a la función para actualizar los datos del usuario en la base de datos
            function getCookie(name) {
                const value = `; ${document.cookie}`;
                const parts = value.split(`; ${name}=`);
                if (parts.length === 2) return parts.pop().split(';').shift();
            }
        
            // Función para cargar id del usuario al cargar la página
            const id_usuario = getCookie('id_usuario'); // Obtener ID del usuario desde la cookie
            console.log(`id_usuario obtenido de la cookie: ${id_usuario}`);
            if (!id_usuario) {
                alert('No se pudo obtener el ID del usuario.');
                return;
            }
                // const id_usuario =4;

    fetch(`http://localhost:8080/usuarios/${encodeURIComponent(id_usuario)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al cargar los datos del usuario: ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            if (data && data.length > 0) {
                const userData = data[0];
                emailInput.value = userData.correo;
                passwordInput.value = '';
                nameInput.value = userData.nombre;
                phoneInput.value = userData.telefono;
            } else {
                console.error('Error: Datos del usuario no encontrados.');
                alert('No se pudieron cargar los datos del usuario.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert(`No se pudieron cargar los datos del usuario. Detalles: ${error.message}`);
        });
            // ------------------------------------------------------------------------------------------------------------------------------------
});

// // Función para registrar un nuevo usuario
// async function registrarUsuario(email, password, phone, name) {
//     try {
//         const response = await fetch('http://localhost:8080/usuarios', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify({ correo: email, contrasena: password, telefono: phone, nombre: name })
//         });

//         if (!response.ok) {
//             throw new Error('Error en la respuesta de la API: ' + response.status);
//         }

//         const data = await response.json();
//         alert('Usuario registrado exitosamente! Por favor, verifica tu correo.');

//         // Enviar correo de verificación
//         await fetch('http://localhost:8080/enviar-correo', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify({ email: email })
//         });

//         container.classList.remove("active"); // Volver a la vista de inicio de sesión
//     } catch (error) {
//         console.error('Error al registrar el usuario:', error);
//         alert('Ocurrió un error al registrar el usuario. Inténtalo de nuevo más tarde.');
//     }
// }

// // Función para verificar si el usuario ha confirmado su correo
// async function verificarCorreo(email) {
//     try {
//         const response = await fetch(`http://localhost:8080/usuarios/verificar-correo?email=${encodeURIComponent(email)}`);
//         const data = await response.json();
//         return data.verificado;
//     } catch (error) {
//         console.error('Error al verificar el correo:', error);
//         return false;
//     }
// }

async function ConsultarDatosUsuario(email, password) {
    try {
        const response = await fetch('http://localhost:8080/usuarios/verificar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ correo: email, contrasena: password })
        });

        const result = await response.json();

        if (response.ok) {
            if (result.success) {
                const correoVerificado = await verificarCorreo(email);
                if (!correoVerificado) {
                    alert('Por favor, verifica tu correo antes de iniciar sesión.');
                    return;
                }
                document.cookie = `id_usuario=${result.id_usuario}; path=/; secure; SameSite=Strict`;
                console.log(`id_usuario almacenado en cookie: ${result.id_usuario}`);
                window.location.href = 'datosYMapa.html';
            } else {
                alert('Contraseña incorrecta');
            }
        } else if (result.error === 'Usuario no existe') {
            alert('El usuario no existe');
        } else {
            alert('Ocurrió un error inesperado');
        }
    } catch (error) {
        console.error('Error al verificar el usuario:', error);
        alert('Ocurrió un error al conectar con el servidor');
    }
}
