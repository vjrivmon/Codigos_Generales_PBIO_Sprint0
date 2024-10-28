// Obtener elementos del DOM
const popup = document.getElementById('popup');
const editBtn = document.getElementById('editBtn');
const saveBtn = document.getElementById('guardarcambios');
const confirmBtn = document.getElementById('confirmBtn');
const cancelBtn = document.getElementById('cancelBtn');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

// Función para habilitar la edición de los campos
editBtn.addEventListener('click', function() {
    emailInput.disabled = false; // Habilitar campo de correo
    passwordInput.disabled = false; // Habilitar campo de contraseña
    saveBtn.disabled = false; // Habilitar botón de guardar cambios
    editBtn.disabled = true; // Deshabilitar botón de editar datos
});

// Mostrar popup al hacer clic en "Guardar cambios"
saveBtn.addEventListener('click', function() {
    popup.style.display = 'flex'; // Mostrar popup
});

// Confirmar cambios al hacer clic en "Confirmar"
confirmBtn.addEventListener('click', function() {
    const email = emailInput.value;
    const password = passwordInput.value;

    // Aquí puedes agregar la lógica para validar y guardar los cambios
    console.log('Cambios guardados:', { email, password });

    // Cerrar popup
    popup.style.display = 'none';

    // Deshabilitar campos nuevamente y habilitar botón de editar
    emailInput.disabled = true;
    passwordInput.disabled = true;
    saveBtn.disabled = true;
    editBtn.disabled = false; // Volver a habilitar botón de editar
    alert('Cambios confirmados'); // Mensaje de confirmación
});

// Cerrar popup al hacer clic en "Cancelar"
cancelBtn.addEventListener('click', function() {
    popup.style.display = 'none'; // Cerrar popup sin guardar cambios
});

// Función para cargar datos del usuario al cargar la página
window.onload = function() {
    const userId = 1; // ID del usuario a cargar
    fetch(`http://172.20.10.5:8080/usuarios/${userId}`) // Cambiado para que use el ID en la ruta
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
        })
        .catch(error => {
            console.error('Error:', error);
            alert('No se pudieron cargar los datos del usuario.');
        });
};
