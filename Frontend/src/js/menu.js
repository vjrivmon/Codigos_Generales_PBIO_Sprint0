// Este script se encarga de manejar el menú de navegación en pantallas pequeñas (móviles).
// Al hacer clic en el botón de menú, se muestra u oculta el menú de navegación.
// Si el usuario cambia a una pantalla más grande, el menú se oculta automáticamente.
// Se utiliza una clase de CSS para mostrar/ocultar el menú y cambiar el aspecto del botón de menú.

const mobileMenu = document.getElementById("mobile-menu");
const navLinks = document.querySelector(".nav-links");

// Evento para abrir/cerrar el menú en móviles
mobileMenu.addEventListener("click", () => {
  navLinks.classList.toggle("show");
  mobileMenu.classList.toggle("active");
});

// Función para cerrar el menú cuando se cambia a pantallas grandes
// handleResize() se ejecuta cuando cambia el tamaño de la ventana
// -> handleResize() ->
function handleResize() {
  if (window.innerWidth > 768) { // Si el ancho de la ventana es mayor a 768px
    navLinks.classList.remove("show");  // Oculta el menú
    mobileMenu.classList.remove("active");  // Asegura que el botón vuelva a su estado inicial
  }
}

// Escucha cambios en el tamaño de la ventana
window.addEventListener("resize", handleResize);
