// Este código se encarga de hacer que las preguntas de la sección FAQ sean desplegables y contraíbles al hacer click en ellas
// Selecciona todas las preguntas y les añade un event listener para que al hacer click en ellas se despliegue la respuesta

document.querySelectorAll('.faq-question').forEach((question) => {
    question.addEventListener('click', () => {
        const answer = question.nextElementSibling; // Selecciona la respuesta a la pregunta
        const arrow = question.querySelector('.arrow'); // Selecciona la flecha que indica si la respuesta está desplegada o no

        // Toggle the display of the answer
        answer.style.display = answer.style.display === 'block' ? 'none' : 'block';  // Si la respuesta está desplegada, la oculta, y si está oculta, la despliega
        // Change the arrow direction
        arrow.innerHTML = answer.style.display === 'block' ? '&#x25BC;' : '&#x25B6;'; // Cambia entre flecha hacia abajo y hacia la derecha
    });
});
