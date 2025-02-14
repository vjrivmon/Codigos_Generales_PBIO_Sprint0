document.addEventListener('DOMContentLoaded', function () {
    // Obtener el elemento del input de teléfono de manera global
    const phoneInputPerfil = document.getElementById("userPhone");

    // Verificar que el elemento existe
    if (phoneInputPerfil) {
        // Escuchar el evento de entrada de texto
        phoneInputPerfil.addEventListener("input", function () {
            // Eliminar todo lo que no sea un número
            this.value = this.value.replace(/[^0-9]/g, '');
        });

        // Validación de longitud al perder el foco
        phoneInputPerfil.addEventListener("blur", function () {
            if (this.value.length !== 9) {
                showAlert("El número de teléfono debe tener exactamente 9 dígitos.");
                this.focus(); // Volver a enfocar el campo
            }
        });
    } else {
        console.error('Error: El elemento phoneInput no se encontró.');
    }
});


// Función para mostrar el mensaje de error y ocultarlo después de 3 segundos
function showAlert(message) {
    console.log(message); // Mostrar el mensaje en la consola como alternativa al alert
    
    // Crear un elemento temporal en el DOM
    const alertMessage = document.createElement('span');
    alertMessage.textContent = message;

    // Insertar el mensaje en el DOM
    document.body.appendChild(alertMessage);

    // Desaparecer el mensaje después de 3 segundos
    setTimeout(() => {
        alertMessage.remove();
    }, 3000);
}