const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});

document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evitar el envío del formulario

    const name = document.getElementById('registerName').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;

    // Hacer una solicitud POST para registrar al usuario
    /*
    
    
    // hay que revisar la siguiente direccion
    
    ordenador de Vicente 172.20.0.5
    Torre Pablo  192.168.0.101
    
    
    */
    fetch('https://192.168.0.101/api/registro', {   
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, email, password })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Registro exitoso');
            // Redirigir o realizar otra acción
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch(error => console.error('Error:', error));
});

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evitar el envío del formulario

    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    /*
    
    
    // hay que revisar la siguiente direccion
    
    
    
    
    */
    // Hacer una solicitud POST para iniciar sesión
    fetch('https://192.168.0.101/api/registro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Inicio de sesión exitoso');
            // Redirigir o realizar otra acción
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch(error => console.error('Error:', error));
});