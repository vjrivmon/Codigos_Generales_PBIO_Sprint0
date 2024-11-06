// Esperar a que todo el DOM esté cargado
document.addEventListener('DOMContentLoaded', function() {
    // Usamos var en lugar de const para garantizar compatibilidad
    var passwordInput = document.getElementById("signUpPassword");
    var passwordConfirmInput = document.getElementById("signUpPasswordConfirm");

    // Función para verificar si las contraseñas coinciden
    function checkPasswordsMatch() {
        var password = passwordInput.value;
        var confirmPassword = passwordConfirmInput.value;

        // Cambiar el borde según si coinciden o no
        if (password === confirmPassword) {
            passwordConfirmInput.style.border = "2px solid green"; // Borde verde si coinciden
        } else {
            passwordConfirmInput.style.border = "2px solid red"; // Borde rojo si no coinciden
        }
    }

    // Añadir los eventos de escucha a ambos campos de contraseña
    passwordInput.addEventListener("input", checkPasswordsMatch);
    passwordConfirmInput.addEventListener("input", checkPasswordsMatch);
});
