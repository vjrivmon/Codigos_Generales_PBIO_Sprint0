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
    confirmBtn.addEventListener('click', function() {
        const email = emailInput.value;
        const password = passwordInput.value;
        const name = nameInput.value;
        const phone = phoneInput.value;

        // Aquí puedes agregar la lógica para validar y guardar los cambios
        console.log('Cambios guardados:', { email, password, name, phone });

        // Cerrar popup
        popup.style.display = 'none';

        // Deshabilitar campos nuevamente y habilitar botón de editar
        emailInput.disabled = true;
        passwordInput.disabled = true;
        nameInput.disabled = true;
        phoneInput.disabled = true;
        confirmBtn.disabled = true;
        editBtn.disabled = false; // Volver a habilitar botón de editar
        alert('Te hemos enviado un correo para verificar los cambios, porfavor compruebelo'); // Mensaje de confirmación
    });

    // Cerrar popup al hacer clic en "Cancelar"
    cancelBtn.addEventListener('click', function() {
        popup.style.display = 'none'; // Cerrar popup sin guardar cambios
    });

    // Función para cargar datos del usuario al cargar la página
    const id_usuario = 1; // ID del usuario a cargar, esto debería ser dinámico
    fetch(`http://192.168.0.25:8080/usuarios/${encodeURIComponent(id_usuario)}`) // Cambiado para que use el ID en la ruta
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar los datos del usuario');
            }
            return response.json();
        })
        .then(data => {
            // Asumiendo que la consulta devuelve un array, tomar el primer elemento
            const userData = data[0]; // Asegúrate de que tu consulta devuelva un solo usuario
            emailInput.value = userData.correo; // Asignar el correo al input
            passwordInput.value = ''; // No mostrar la contraseña
            nameInput.value = userData.nombre; // Asignar el nombre al input
            phoneInput.value = userData.telefono; // Asignar el teléfono al input
        })
        .catch(error => {
            console.error('Error:', error);
            alert('No se pudieron cargar los datos del usuario.');
        });
});
