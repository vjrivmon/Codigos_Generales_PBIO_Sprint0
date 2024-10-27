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
    // Aquí puedes agregar la lógica para guardar los cambios
    const email = emailInput.value;
    const password = passwordInput.value;
    
    // Simular el almacenamiento de cambios
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
