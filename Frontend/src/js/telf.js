// Obtener el elemento del input de teléfono de manera global
const phoneInput = document.getElementById("signUpPhone");

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





// Obtener el elemento del input de teléfono de manera global
const phoneInputPerfil = document.getElementById("userPhone");

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

