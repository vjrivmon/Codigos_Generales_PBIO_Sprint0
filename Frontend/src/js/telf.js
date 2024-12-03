document.addEventListener('DOMContentLoaded', function() {
    // Obtener el elemento del input de teléfono de manera global
    const phoneInput = document.getElementById("signUpPhone");

    // Verificar que el elemento existe
    if (phoneInput) {
        // Escuchar el evento de entrada de texto
        phoneInput.addEventListener("input", function() {
            // Eliminar todo lo que no sea un número
            this.value = this.value.replace(/[^0-9]/g, '');
        });

        // Validación de longitud al perder el foco
        phoneInput.addEventListener("blur", function() {
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
    // Crear el elemento de alerta
    const alertDiv = document.createElement("div");
    alertDiv.classList.add("alert");
    alertDiv.innerText = message;

    // Agregar el mensaje de alerta al cuerpo del documento
    document.body.appendChild(alertDiv);

    // Desaparecer la alerta después de 3 segundos
    setTimeout(function() {
        alertDiv.remove();
    }, 3000);
}

document.addEventListener('DOMContentLoaded', function() {
    // Obtener el elemento del input de teléfono de manera global
    const phoneInputPerfil = document.getElementById("userPhone");

    // Verificar que el elemento existe
    if (phoneInputPerfil) {
        // Escuchar el evento de entrada de texto
        phoneInputPerfil.addEventListener("input", function() {
            // Eliminar todo lo que no sea un número
            this.value = this.value.replace(/[^0-9]/g, '');
        });

        // Validación de longitud al perder el foco
        phoneInputPerfil.addEventListener("blur", function() {
            if (this.value.length !== 9) {
                showAlert("El número de teléfono debe tener exactamente 9 dígitos.");
                this.focus(); // Volver a enfocar el campo
            }
        });
    } else {
        //console.error('Error: El elemento phoneInputPerfil no se encontró.');
    }
});
