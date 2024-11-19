document.addEventListener('DOMContentLoaded', function() {
    const popup = document.getElementById('popup2');
    const editBtn = document.getElementById('editBtn');
    const confirmBtn = document.getElementById('confirmBtn2');
    const cancelBtn = document.getElementById('cancelBtn2');
    const userName = document.getElementById('userName');
    const userPhone = document.getElementById('userPhone');
    const userEmail = document.getElementById('userEmail');
    const sensorName = document.getElementById('sensorName');

    if (!popup || !editBtn || !confirmBtn || !cancelBtn || !userName || !userPhone || !userEmail || !sensorName) {
        console.error('Error: Uno o más elementos del DOM no se encontraron.');
        return;
    }

    editBtn.addEventListener('click', function() {
        const inputs = [userName, userPhone, userEmail, sensorName];
        if (this.textContent === "Editar") {
            inputs.forEach(input => input.disabled = false);
            this.textContent = "Guardar";
        } else {
            inputs.forEach(input => input.disabled = true);
            this.textContent = "Editar";
            popup.style.display = 'flex';
        }
    });

    confirmBtn.addEventListener('click', async function(event) {
        event.preventDefault();

        if (!userName.value || !userPhone.value || !userEmail.value || !sensorName.value) {
            alert('Rellene todos los campos.');
            return;
        }

        if (userPhone.value.length !== 9) {
            alert("El número de teléfono debe tener exactamente 9 dígitos.");
            return;
        }

        try {
            const id_usuario = getCookie('id_usuario');
            if (!id_usuario) {
                alert('No se pudo obtener el ID del usuario.');
                return;
            }

            const datosUsuario = {
                nombre: userName.value,
                telefono: userPhone.value,
                correo: userEmail.value
            };

            const response = await fetch(`http://localhost:8080/usuarios/${encodeURIComponent(id_usuario)}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(datosUsuario)
            });

            if (!response.ok) {
                throw new Error('No se pudieron actualizar los datos del usuario');
            }

            const resultado = await response.text();
            alert(resultado);
            popup.style.display = 'none';
        } catch (error) {
            console.error('Error al actualizar los datos del usuario:', error);
            alert('Hubo un problema al actualizar los datos del usuario.');
            popup.style.display = 'none';
        }
    });

    cancelBtn.addEventListener('click', function() {
        popup.style.display = 'none';
    });

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }
});