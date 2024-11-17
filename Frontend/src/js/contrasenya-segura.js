//Elementos para el registro
const passwordInput = document.getElementById("signUpPassword");
const passwordStrengthText = document.getElementById("password-strength-text");
const passwordStrengthBar = document.getElementById("password-strength");
const passwordToggle = document.getElementById("toggle-password-visibility");

// Elementos para el login
const loginPasswordInput = document.getElementById("loginPassword");
const loginPasswordToggle = document.getElementById("toggle-login-password-visibility");

// registro
passwordToggle.addEventListener("click", () => {
    passwordInput.type = passwordInput.type === "password" ? "text" : "password";
    passwordToggle.src = passwordInput.type === "password" ? "../assets/eye.svg" : "../assets/eye-off.svg";
});

passwordInput.addEventListener("keyup", () => {
    const password = passwordInput.value;
    updatePasswordStrength(password);
});


//login

loginPasswordToggle.addEventListener("click", () => {
    loginPasswordInput.type = loginPasswordInput.type === "password" ? "text" : "password";
    loginPasswordToggle.src = loginPasswordInput.type === "password" ? "../assets/eye.svg" : "../assets/eye-off.svg";
});

loginPasswordInput.addEventListener("keyup", () => {
    const password = loginPasswordInput.value;
    //updatePasswordStrength(password);
});

function updatePasswordStrength(password) {
    let strength = 0;
    // Actualiza cada condición con iconos de verificación
    document.querySelector('.low-upper-case img').src = /([a-z].*[A-Z])|([A-Z].*[a-z])/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.one-number img').src = /\d/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.one-special-char img').src = /[!@#$%^&*]/.test(password) ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';
    document.querySelector('.eight-character img').src = password.length >= 8 ? '../assets/circle-check.svg' : '../assets/circle-cross.svg';

    if ((/[a-z].*[A-Z]|[A-Z].*[a-z]/).test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[!@#$%^&*]/.test(password)) strength++;
    if (password.length >= 8) strength++;

    const strengthPercentage = (strength / 4) * 100;
    passwordStrengthBar.style.width = strengthPercentage + "%";

    if (strength < 2) {
        /*passwordStrengthText.textContent = "Débil";*/
        passwordStrengthBar.className = "progress-bar progress-bar-danger";
    } else if (strength === 3) {
        /*passwordStrengthText.textContent = "Moderada";*/
        passwordStrengthBar.className = "progress-bar progress-bar-warning";
    } else if (strength === 4) {
        /*passwordStrengthText.textContent = "Fuerte";*/
        passwordStrengthBar.className = "progress-bar progress-bar-success";
    }
}
