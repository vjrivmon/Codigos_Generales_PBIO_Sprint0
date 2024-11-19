//Este código  es para verificar si las contraseñas coinciden. Aquí hay una explicación de lo que hace cada parte:
// 1. Se obtienen los elementos de los inputs de contraseña y confirmación de contraseña.
// 2. Se crea una función para verificar si las contraseñas coinciden.
// 3. Se añaden los eventos de escucha a ambos campos de contraseña.
// 4. Se cambia el borde de los campos según si las contraseñas coinciden o no. Si coinciden, el borde se vuelve verde; si no, se vuelve rojo.
// Esperar a que todo el DOM esté cargado
document.addEventListener('DOMContentLoaded', function() {
    // Usamos var en lugar de const para garantizar compatibilidad
    var passwordInput = document.getElementById("signUpPassword");
    var passwordConfirmInput = document.getElementById("signUpPasswordConfirm");

    // Función para verificar si las contraseñas coinciden

    // -> chechkPasswordsMatch()
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
