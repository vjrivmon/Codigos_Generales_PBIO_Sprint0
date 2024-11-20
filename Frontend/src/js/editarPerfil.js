// Este script se encarga de manejar la funcionalidad de editar el perfil del usuario en la página web.
// Se encarga de validar los campos del formulario, mostrar un popup para editar los datos y enviar los datos al servidor.
// Se debe incluir en la página web que contenga el formulario de edición de perfil.

// Asegurarse de que el DOM esté completamente cargado antes de ejecutar el código
document.addEventListener('DOMContentLoaded', function() {
    const popup = document.getElementById('popup2');
    const editBtn = document.getElementById('editBtn');
    const confirmBtn = document.getElementById('confirmBtn2');
    const cancelBtn = document.getElementById('cancelBtn2');
    const userName = document.getElementById('userName');
    const userPhone = document.getElementById('userPhone');
    const userEmail = document.getElementById('userEmail');
    const sensorName = document.getElementById('sensorName');
    const userPassword = document.getElementById('userPassword');

    if (!popup || !editBtn || !confirmBtn || !cancelBtn || !userName || !userPhone || !userEmail || !sensorName) {
        console.error('Error: Uno o más elementos del DOM no se encontraron.');
        return;
    }

    /*--------------------- NOMBRE -------------------------*/
    // Validar que el nombre tenga al menos 2 caracteres 
    // (se puede cambiar la longitud mínima si se desea)
    userName.addEventListener("blur", function() {
        if (this.value.length < 2) {
            alert("El nombre debe tener al menos 2 caracteres.");
            this.focus();
        }
    });

    /*--------------------- CORREO -------------------------*/
    // Validar que el correo tenga un formato válido con una expresión regular 
    // El correo debe tener un @ y un punto después del @ (ej .com) (no se permiten espacios)
    userEmail.addEventListener("blur", function() {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(this.value)) {
            alert("Por favor, ingrese un correo electrónico válido.")
            this.focus();
        }
    });

    /*--------------------- TELEFONO -------------------------*/
    // Validar que el teléfono tenga exactamente 9 dígitos y solo números
    userPhone.addEventListener("input", function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    userPhone.addEventListener("blur", function() {
        if (this.value.length !== 9) {
            alert("El número de teléfono debe tener exactamente 9 dígitos.");
            this.focus();
        }
    });

    /*-------------------------------- APARECER POPUP EDITAR DATOS ---------------------------------*/
    // Al hacer clic en el botón de editar, se habilitan los campos para editar los datos y se muestra el popup
    editBtn.addEventListener('click', function() {
        const inputs = [userName, userPhone, userEmail, sensorName];
        if (this.textContent === "Editar") {
            inputs.forEach(input => input.disabled = false);
            userPassword.disabled = true;
            this.textContent = "Guardar";
            editBtn.disabled = false;
        } else {
            inputs.forEach(input => input.disabled = true);
            this.textContent = "Editar";
            popup.style.display = 'flex';
        }
    });

    /*-------------------- CONFIRMAR --------------------------*/
    // Al hacer clic en el botón de confirmar, se envían los datos al servidor para actualizar el perfil del usuario

    confirmBtn.addEventListener('click', async function(event) {
        event.preventDefault();
        
        if (!userName.value || !userPhone.value || !userEmail.value || !sensorName.value) { // Verificar que todos los campos estén llenos
            alert('Rellene todos los campos.');
            return;
        }

        if (userPhone.value.length !== 9) { // Verificar que el teléfono tenga exactamente 9 dígitos

            alert("El número de teléfono debe tener exactamente 9 dígitos.");
            return;
        }
        
        try {
            const id_usuario = getCookie('id_usuario');
            if (!id_usuario) {
                alert('No se pudo obtener el ID del usuario.');
                popup.style.display = 'none';
                editBtn.disabled = false;
                return;
            }

            const datosUsuario = {
                nombre: userName.value,
                telefono: userPhone.value,
                correo: userEmail.value
            };
            
            console.log(`http://localhost:8080/usuarios/${encodeURIComponent(id_usuario)}`);
            
            // Enviar los datos al servidor
            const response = await fetch(`http://localhost:8080/usuarios/${encodeURIComponent(id_usuario)}`, {
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
            alert('Se ha enviado un correo para reestablecer su contraseña al correo asociado al teléfono que nos ha proporcionado.');
            popup.style.display = 'none'; // Oculta el popup
            
        } catch (error) { // Capturar errores
            console.error('Error al actualizar los datos del usuario:', error);
            alert('Hubo un problema al actualizar los datos del usuario.');
            popup.style.display = 'none'; // Oculta el popup
            editBtn.disabled = false;
        }
    });

    cancelBtn.addEventListener('click', function() {
        popup.style.display = 'none';
        editBtn.disabled = false;
    });

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }
});