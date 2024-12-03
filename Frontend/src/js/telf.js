//Este código es para validar que el número de teléfono tenga exactamente 9 dígitos. Aquí hay una explicación de lo que hace cada parte:
// 1. Se obtiene el elemento del input de teléfono.
// 2. Se verifica que el elemento existe.
// 3. Se escucha el evento de entrada de texto.
// 4. Se elimina todo lo que no sea un número.
// 5. Se añade una validación de longitud al perder el foco.
// 6. Se muestra una alerta si el número de teléfono no tiene 9 dígitos.
// 7. Se vuelve a enfocar el campo si no tiene 9 dígitos.
// 8. Se muestra un error si el elemento no se encontró.

// Asegurarse de que el DOM esté completamente cargado antes de ejecutar el código
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
                alert("El número de teléfono debe tener exactamente 9 dígitos.");
                this.focus(); // Volver a enfocar el campo
            }
        });
    } else {
        console.error('Error: El elemento phoneInput no se encontró.');
    }
});

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
                alert("El número de teléfono debe tener exactamente 9 dígitos.");
                this.focus(); // Volver a enfocar el campo
            }
        });
    } else {
        //console.error('Error: El elemento phoneInputPerfil no se encontró.');
    }
});