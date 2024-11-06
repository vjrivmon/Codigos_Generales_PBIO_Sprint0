// Obtener los elementos de las contraseñas, el botón y el checkbox
/*const passwordInput = document.getElementById("signUpPassword");
const passwordConfirmInput = document.getElementById("signUpPasswordConfirm");
const registerBtn = document.getElementById("register-btn");
const privacyPolicyCheckbox = document.getElementById("privacy-policy");

// Función para verificar si las contraseñas coinciden y si el checkbox está marcado
function checkFormValidity() {
    const password = passwordInput.value;
    const confirmPassword = passwordConfirmInput.value;
    const isPrivacyChecked = privacyPolicyCheckbox.checked;

    // Verificar si las contraseñas coinciden y si el checkbox está marcado
    if (password === confirmPassword && isPrivacyChecked) {
        // Si las contraseñas coinciden y el checkbox está marcado, habilitar el botón de registro
        registerBtn.disabled = false;
    } else {
        // Si no coinciden o el checkbox no está marcado, deshabilitar el botón de registro
        registerBtn.disabled = true;
    }
}

// Añadir eventos para cuando el usuario escriba en cualquiera de los dos campos de contraseña
passwordInput.addEventListener("keyup", checkFormValidity);
passwordConfirmInput.addEventListener("keyup", checkFormValidity);

// También llamar a la función cuando el campo "Repetir Contraseña" pierda el foco
passwordConfirmInput.addEventListener("blur", checkFormValidity);

// Añadir un evento para cuando el usuario marque o desmarque el checkbox de privacidad
privacyPolicyCheckbox.addEventListener("change", checkFormValidity);*/
