// Este código implementa la funcionalidad de la página de registro y login para cambiar entre las pantallas de registro
// y login, y ajustar el tamaño del contenedor en función de la vista actual. Se utilizan funciones para mostrar el formulario
// de registro y login, y para ajustar el tamaño del contenedor. Además, se definen eventos para los botones de registro y login
// que llaman a estas funciones al hacer clic en ellos.

// Esperar a que el DOM esté completamente cargado
// Seleccionar el contenedor y los formularios de inicio de sesión y registro en la página de inicio de sesión y registro

document.addEventListener('DOMContentLoaded', function() {
    // Seleccionar el contenedor
    var container = document.getElementById("container");

    // Seleccionar las secciones de inicio de sesión y registro
    var signUpForm = document.querySelector('.sign-up');
    var signInForm = document.querySelector('.sign-in');

    // Seleccionar el logo
    var logo = document.querySelector('nav'); // Seleccionamos la imagen del logo

    // Función para ajustar el min-height basado en la vista actual
    // Si el formulario de registro está visible, establecer el min-height en 600px
    // Si el formulario de inicio de sesión está visible, establecer el min-height en 500px
    // -> adjustContainerHeight()
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
    // -> showSignUp()
    function showSignUp() {
        signInForm.style.display = 'none';
        signUpForm.style.display = 'block';
        adjustContainerHeight();
    }

    // Mostrar el formulario de inicio de sesión y ajustar el tamaño del contenedor
    // -> showSignIn()
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

    registerButton.addEventListener('click', showSignUp); // Evento para mostrar el formulario de registro
    loginButton.addEventListener('click', showSignIn); // Evento para mostrar el formulario de inicio de sesión
});
