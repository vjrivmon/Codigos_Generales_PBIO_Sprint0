// Este código implementa la funcionalidad de la página de restablecimiento de contraseña para mostrar y ocultar la nueva contraseña,
// actualizar la fuerza de la contraseña en tiempo real y verificar la fortaleza de la contraseña. Se utilizan eventos para el
// botón de mostrar/ocultar contraseña, la entrada de la nueva contraseña y la verificación de la fortaleza de la contraseña. Además,
// se define una función para actualizar la fuerza de la contraseña y se actualizan las condiciones con íconos de verificación según
// la fortaleza de la contraseña.

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
// password -> updatePasswordStrength() -> código de colores barra de progreso
function updatePasswordStrength(password) {
    let strength = 0;

    // Actualiza cada condición con íconos de verificación
    document.querySelector('.low-upper-case img').src = /([a-z].*[A-Z])|([A-Z].*[a-z])/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.one-number img').src = /\d/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.one-special-char img').src = /[!@#$%^&*]/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.eight-character img').src = password.length >= 8 ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';

    // Calcula la fuerza de la contraseña
    if ((/[a-z].*[A-Z]|[A-Z].*[a-z]/).test(password)) strength++;  // Si contiene al menos una letra minúscula y una mayúscula
    if (/\d/.test(password)) strength++;  // Si contiene al menos un número
    if (/[!@#$%^&*]/.test(password)) strength++;  // Si contiene al menos un carácter especial
    if (password.length >= 8) strength++;  // Si tiene al menos 8 caracteres

    const strengthPercentage = (strength / 4) * 100; // Calcula el porcentaje del espacio de la barra de progreso
    passwordStrengthBar.style.width = strengthPercentage + "%";

    // Cambia la clase de la barra de progreso según la fuerza
    if (strength < 2) {
        passwordStrengthBar.className = "progress-bar progress-bar-danger";  // Contraseña débil - Rojo
    } else if (strength === 3) {
        passwordStrengthBar.className = "progress-bar progress-bar-warning";  // Contraseña moderada - Amarillo
    } else if (strength === 4) {
        passwordStrengthBar.className = "progress-bar progress-bar-success";  // Contraseña fuerte - Verde
    }
}
