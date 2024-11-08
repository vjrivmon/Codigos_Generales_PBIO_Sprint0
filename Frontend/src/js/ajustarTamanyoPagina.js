// Esperar a que el DOM esté completamente cargado
/*document.addEventListener('DOMContentLoaded', function() {
    // Seleccionar el contenedor
    var container = document.getElementById("container");

    // Seleccionar las secciones de inicio de sesión y registro
    var signUpForm = document.querySelector('.sign-up');
    var signInForm = document.querySelector('.sign-in');

    // Función para ajustar el min-height basado en la vista actual
    function adjustContainerHeight() {
        // Verificar si el formulario de registro es visible
        if (signUpForm.style.display !== 'none') {
            container.style.minHeight = '600px';  // Si está en la pantalla de registro, establecer 600px
        } else {
            container.style.minHeight = '500px';  // Si está en la pantalla de inicio de sesión, establecer 500px
        }
    }

    // Mostrar el formulario de registro y ajustar el tamaño del contenedor
    function showSignUp() {
        signInForm.style.display = 'none';
        signUpForm.style.display = 'block';
        adjustContainerHeight();
    }

    // Mostrar el formulario de inicio de sesión y ajustar el tamaño del contenedor
    function showSignIn() {
        signUpForm.style.display = 'none';
        signInForm.style.display = 'block';
        adjustContainerHeight();
    }

    // Inicialmente mostrar el formulario de inicio de sesión
    showSignIn();

    // Eventos de los botones para cambiar entre las pantallas de login y registro
    var registerButton = document.getElementById('register');
    var loginButton = document.getElementById('login');

    registerButton.addEventListener('click', showSignUp);
    loginButton.addEventListener('click', showSignIn);
});*/



// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    // Seleccionar el contenedor
    var container = document.getElementById("container");

    // Seleccionar las secciones de inicio de sesión y registro
    var signUpForm = document.querySelector('.sign-up');
    var signInForm = document.querySelector('.sign-in');

    // Seleccionar el logo
    var logo = document.querySelector('nav'); // Seleccionamos la imagen del logo

    // Función para ajustar el min-height basado en la vista actual
    function adjustContainerHeight() {
        // Verificar si el formulario de registro es visible
        if (signUpForm.style.display !== 'none') {
            container.style.minHeight = '600px';  // Si está en la pantalla de registro, establecer 600px
            logo.style.marginTop = '60px';  // Agregar margen al logo en la pantalla de registro
        } else {
            container.style.minHeight = '500px';  // Si está en la pantalla de inicio de sesión, establecer 500px
            logo.style.marginTop = '0';  // Eliminar el margen superior en la pantalla de inicio de sesión
        }
    }

    // Mostrar el formulario de registro y ajustar el tamaño del contenedor
    function showSignUp() {
        signInForm.style.display = 'none';
        signUpForm.style.display = 'block';
        adjustContainerHeight();
    }

    // Mostrar el formulario de inicio de sesión y ajustar el tamaño del contenedor
    function showSignIn() {
        signUpForm.style.display = 'none';
        signInForm.style.display = 'block';
        adjustContainerHeight();
    }

    // Inicialmente mostrar el formulario de inicio de sesión
    showSignIn();

    // Eventos de los botones para cambiar entre las pantallas de login y registro
    var registerButton = document.getElementById('register');
    var loginButton = document.getElementById('login');

    registerButton.addEventListener('click', showSignUp);
    loginButton.addEventListener('click', showSignIn);
});
