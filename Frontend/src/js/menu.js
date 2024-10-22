const mobileMenu = document.getElementById("mobile-menu");
const navLinks = document.querySelector(".nav-links");

mobileMenu.addEventListener("click", () => {
  navLinks.classList.toggle("show");
  mobileMenu.classList.toggle("active");
});
