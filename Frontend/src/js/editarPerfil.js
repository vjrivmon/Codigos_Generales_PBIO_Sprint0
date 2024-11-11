// Asegurarse de que el DOM esté completamente cargado antes de ejecutar el código
document.addEventListener('DOMContentLoaded', function() {
    const popup = document.getElementById('popup2');
    const editBtn = document.getElementById('editBtn'); // Botón de editar
    const confirmBtn = document.getElementById('confirmBtn2'); // Botón de confirmar
    const cancelBtn = document.getElementById('cancelBtn2'); // Botón de cancelar
    const userName = document.getElementById('userName'); // Campo de teléfono en el popup
    const userPhone = document.getElementById('userPhone'); // Campo de teléfono en el popup
    const userEmail = document.getElementById('userEmail'); // Campo de correo en el popup
    const sensorName = document.getElementById('sensorName'); // Campo de nombre del sensor en el popup

    // Verificar que todos los elementos existen
    if (!popup || !editBtn || !confirmBtn || !cancelBtn || !userName || !userPhone || !userEmail || !sensorName) {
        console.error('Error: Uno o más elementos del DOM no se encontraron.');
        return;
    }

    /*--------------------- NOMBRE -------------------------*/
    // Validación de longitud al perder el foco
    userName.addEventListener("blur", function() {
        if (this.value.length < 2) {
            alert("El nombre debe tener al menos 2 caracteres.");
            this.focus(); // Volver a enfocar el campo
        }
    });

    /*--------------------- CORREO -------------------------*/
    // Validación de formato al perder el foco
    userEmail.addEventListener("blur", function() {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(this.value)) {
            alert("Por favor, ingrese un correo electrónico válido.")
            this.focus(); // Volver a enfocar el campo
        }
    });

    /*--------------------- TELEFONO -------------------------*/
    // Escuchar el evento de entrada de texto
    userPhone.addEventListener("input", function() {
        // Eliminar todo lo que no sea un número
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // Validación de longitud al perder el foco
    userPhone.addEventListener("blur", function() {
        if (this.value.length !== 9) {
            alert("El número de teléfono debe tener exactamente 9 dígitos.");
            this.focus(); // Volver a enfocar el campo
        }
    });

    /*-------------------------------- APARECER POPUP EDITAR DATOS ---------------------------------*/

    // JavaScript para habilitar/deshabilitar inputs
    document.getElementById("editBtn").addEventListener("click", function() {
        const inputs = document.querySelectorAll("#userName, #userPhone, #userEmail, #sensorName");
        inputs.forEach(input => input.disabled = !input.disabled);
        this.textContent = inputs[0].disabled ? "Editar" : "Guardar";
        if (this.textContent === "Editar") {  // Botón para abrir popup
            popup.style.display = 'flex'; 
        }
    });

    /*-------------------- CONFIRMAR --------------------------*/
    confirmBtn.addEventListener('click', async function(event) { 
        if (!userName.value || !userPhone.value || !userEmail.value || !sensorName.value) {
            alert('Rellene todos los campos.');
            return;
        }

        // Validar que el campo no esté vacío y tenga exactamente 9 dígitos
        if (!userPhone.value) {
            alert('Rellene el campo con su número de teléfono.');
            return;
        } else if (userPhone.value.length !== 9) {
            alert("El número de teléfono debe tener exactamente 9 dígitos.");
            return;
        } else {
            // const mail = await enviarCorreoDatosUsuarioCambiados(userEmail)
            alert('Se ha enviado un correo para reestablecer su contraseña al correo asociado al teléfono que nos ha proporcionado.');
            /* ENVIAR CORREO AL USUARIO - FALTA POR IMPLEMENTAR*/
            
            popup.style.display = 'none'; // Oculta el popup
        }
    });

    /*----------------------- CANCELAR  ------------------------*/
    cancelBtn.addEventListener('click', function() {
        popup.style.display = 'none'; // Oculta el popup
    });
});

