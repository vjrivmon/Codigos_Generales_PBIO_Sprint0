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

    // // Función para habilitar la edición de los campos
    // editBtn.addEventListener('click', function() {
    //     emailInput.disabled = false; // Habilitar campo de correo
    //     passwordInput.disabled = false; // Habilitar campo de contraseña
    //     nameInput.disabled = false; // Habilitar campo de nombre
    //     phoneInput.disabled = false; // Habilitar campo de teléfono
    //     confirmBtn.disabled = false; // Habilitar botón de guardar cambios
    //     editBtn.disabled = true; // Deshabilitar botón de editar datos
    // });

    // // Mostrar popup al hacer clic en "Guardar cambios"
    // confirmBtn.addEventListener('click', function() {
    //     popup.style.display = 'flex'; // Mostrar popup
    // });

    // // Confirmar cambios al hacer clic en "Confirmar"
    // confirmBtn.addEventListener('click', function() {
    //     const email = emailInput.value;
    //     const password = passwordInput.value;
    //     const name = nameInput.value;
    //     const phone = phoneInput.value;

    //     // Aquí puedes agregar la lógica para validar y guardar los cambios
    //     console.log('Cambios guardados:', { email, password, name, phone });

    //     // Cerrar popup
    //     popup.style.display = 'none';

    //     // Deshabilitar campos nuevamente y habilitar botón de editar
    //     emailInput.disabled = true;
    //     passwordInput.disabled = true;
    //     nameInput.disabled = true;
    //     phoneInput.disabled = true;
    //     confirmBtn.disabled = true;
    //     editBtn.disabled = false; // Volver a habilitar botón de editar
    //     alert('Te hemos enviado un correo para verificar los cambios, porfavor compruebelo'); // Mensaje de confirmación
    // });

    // // Cerrar popup al hacer clic en "Cancelar"
    // cancelBtn.addEventListener('click', function() {
    //     popup.style.display = 'none'; // Cerrar popup sin guardar cambios
    // });
    
    // ---------------------------- Antes de tocar esto, consultar esto con Vicente, se puede romper el código ----------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------
    // Función para obtener el valor de una cookie por su nombre
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    // Función para cargar datos del usuario al cargar la página
    const id_usuario = getCookie('id_usuario'); // Obtener ID del usuario desde la cookie
    console.log(`id_usuario obtenido de la cookie: ${id_usuario}`);
    if (!id_usuario) {
        alert('No se pudo obtener el ID del usuario.');
        return;
    }

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
