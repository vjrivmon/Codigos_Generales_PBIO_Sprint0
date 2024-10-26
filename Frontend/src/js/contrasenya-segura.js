/*const input = document.querySelector(".input__field");
const inputIcon = document.querySelector(".input__icon");
const passwordStrength = document.querySelector(".progress-bar");

inputIcon.addEventListener("click", (e) => {
    e.preventDefault();

    inputIcon.setAttribute(
        'src', 
        input.getAttribute('type') === 'password' ?
        'assets/eye-off.svg'
          :
        'assets/eye.svg'
    );

    input.setAttribute(
        'type', 
        input.getAttribute('type') === 'password' ? 
        'text'
          :
        'password'
    );
});

input.addEventListener("keyup", function(){
    let pass = document.getElementById("password").value;
    checkStrength(pass);
});

function checkStrength(password) {
    let strength = 0;

    if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) {
        strength += 1;
        const img = document.querySelector('.low-upper-case img');    
        img.src = 'assets/circle-check.svg';
    } else {
        const img = document.querySelector('.low-upper-case img');        
        img.src = 'assets/circle-cross.svg';
    }

    if (password.match(/([0-9])/)) {
        strength += 1;
        const img = document.querySelector('.one-number img');    
        img.src = 'assets/circle-check.svg';
    } else {
        const img = document.querySelector('.one-number img');        
        img.src = 'assets/circle-cross.svg';
    }

    if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
        strength += 1;
        const img = document.querySelector('.one-special-char img');    
        img.src = 'assets/circle-check.svg';
    } else {
        const img = document.querySelector('.one-special-char img');    
        img.src = 'assets/circle-cross.svg';
    }

    if (password.length > 7) {
        strength += 1;
        const img = document.querySelector('.eight-character img');
        img.src = 'assets/circle-check.svg';
    } else {
        const img = document.querySelector('.eight-character img');
        img.src = 'assets/circle-cross.svg';
    }

    console.log(strength)

    if (strength < 2) {
        passwordStrength.classList.remove('progress-bar-warning');
        passwordStrength.classList.remove('progress-bar-success');
        passwordStrength.classList.add('progress-bar-danger');
        passwordStrength.style = 'width: 10%';
    } else if (strength == 3) {
        passwordStrength.classList.remove('progress-bar-success');
        passwordStrength.classList.remove('progress-bar-danger');
        passwordStrength.classList.add('progress-bar-warning');
        passwordStrength.style = 'width: 60%';
    } else if (strength == 4) {
        passwordStrength.classList.remove('progress-bar-warning');
        passwordStrength.classList.remove('progress-bar-danger');
        passwordStrength.classList.add('progress-bar-success');
        passwordStrength.style = 'width: 100%';
    }
}*/


const passwordInput = document.getElementById("signUpPassword");
const passwordStrengthText = document.getElementById("password-strength-text");
const passwordStrengthBar = document.getElementById("password-strength");
const passwordToggle = document.getElementById("toggle-password-visibility");

passwordToggle.addEventListener("click", () => {
    passwordInput.type = passwordInput.type === "password" ? "text" : "password";
    passwordToggle.src = passwordInput.type === "password" ? "../assets/eye.svg" : "../assets/eye-off.svg";
});

passwordInput.addEventListener("keyup", () => {
    const password = passwordInput.value;
    updatePasswordStrength(password);
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
