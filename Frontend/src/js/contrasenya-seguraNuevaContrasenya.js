// Elementos de restablecimiento de contraseña
const newPasswordInput = document.getElementById("newPassword");
const confirmPasswordInput = document.getElementById("confirmPassword");
const passwordStrengthText = document.getElementById("password-strength-text");
const passwordStrengthBar = document.getElementById("password-strength");
const newPasswordToggle = document.getElementById("toggle-new-password-visibility");

// Toggle para mostrar/ocultar nueva contraseña
newPasswordToggle.addEventListener("click", () => {
    newPasswordInput.type = newPasswordInput.type === "password" ? "text" : "password";
    newPasswordToggle.src = newPasswordInput.type === "password" ? "../assets/eye.svg" : "../assets/eye-off.svg";
});

// Actualización de la fuerza de la contraseña en tiempo real
newPasswordInput.addEventListener("keyup", () => {
    const password = newPasswordInput.value;
    updatePasswordStrength(password);
});

// Lógica de verificación de fortaleza de la contraseña
function updatePasswordStrength(password) {
    let strength = 0;

    // Actualiza cada condición con íconos de verificación
    document.querySelector('.low-upper-case img').src = /([a-z].*[A-Z])|([A-Z].*[a-z])/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.one-number img').src = /\d/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.one-special-char img').src = /[!@#$%^&*]/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.eight-character img').src = password.length >= 8 ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';

    // Calcula la fuerza de la contraseña
    if ((/[a-z].*[A-Z]|[A-Z].*[a-z]/).test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[!@#$%^&*]/.test(password)) strength++;
    if (password.length >= 8) strength++;

    const strengthPercentage = (strength / 4) * 100;
    passwordStrengthBar.style.width = strengthPercentage + "%";

    // Cambia la clase de la barra de progreso según la fuerza
    if (strength < 2) {
        passwordStrengthBar.className = "progress-bar progress-bar-danger";
    } else if (strength === 3) {
        passwordStrengthBar.className = "progress-bar progress-bar-warning";
    } else if (strength === 4) {
        passwordStrengthBar.className = "progress-bar progress-bar-success";
    }
}
