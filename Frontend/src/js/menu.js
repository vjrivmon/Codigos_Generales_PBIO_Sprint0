const mobileMenu = document.getElementById("mobile-menu");
const navLinks = document.querySelector(".nav-links");

// Evento para abrir/cerrar el menú en móviles
mobileMenu.addEventListener("click", () => {
  navLinks.classList.toggle("show");
  mobileMenu.classList.toggle("active");
});

// Función para cerrar el menú cuando se cambia a pantallas grandes
function handleResize() {
  if (window.innerWidth > 768) {
    navLinks.classList.remove("show");  // Oculta el menú
    mobileMenu.classList.remove("active");  // Asegura que el botón vuelva a su estado inicial
  }
}

// Escucha cambios en el tamaño de la ventana
window.addEventListener("resize", handleResize);
