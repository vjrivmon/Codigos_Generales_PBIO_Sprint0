// Este código implementa una funcionalidad de cookies para mostrar un cuadro de cookies en la parte inferior de la página web.
// El cuadro de cookies se muestra una vez que el usuario visita el sitio web y no ha aceptado las cookies. El cuadro de cookies
// contiene un mensaje y dos botones: uno para aceptar las cookies y otro para rechazarlas. Si el usuario acepta las cookies, se
// establece una cookie con un nombre y un valor específicos. Si el usuario rechaza las cookies, el cuadro de cookies se oculta y no
// se establece ninguna cookie. El cuadro de cookies se oculta automáticamente si el usuario acepta o rechaza las cookies.

const cookieBox = document.querySelector(".wrapper"),
    buttons = document.querySelectorAll(".button");

const executeCodes = () => {
    // si la cookie contiene "codinglab", no se muestra el cuadro de cookies
    if (document.cookie.includes("codinglab")) return; 
    cookieBox.classList.add("show"); // muestra el cuadro de cookies

    //este foreach hace que cada botón tenga un evento de tipo clic, oculta el cuadro de cookies
    buttons.forEach((button) => {
        button.addEventListener("click", () => {
            cookieBox.classList.remove("show"); 

            // si le da clic en aceptar, se establece una cookie con el nombre "cookieBy" y el valor "codinglab" durante 30 días
            if (button.id == "acceptBtn") {
                //set cookies for 1 month. 60 = 1 min, 60 = 1 hours, 24 = 1 day, 30 = 30 days
                document.cookie = "cookieBy= codinglab; max-age=" + 60 * 60 * 24 * 30;
            }
        });
    });
};

// se ejecuta la función executeCodes cuando la página se carga completamente
window.addEventListener("load", executeCodes);
