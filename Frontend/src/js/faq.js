document.querySelectorAll('.faq-question').forEach((question) => {
    question.addEventListener('click', () => {
        const answer = question.nextElementSibling;
        const arrow = question.querySelector('.arrow');

        // Toggle the display of the answer
        answer.style.display = answer.style.display === 'block' ? 'none' : 'block';

        // Change the arrow direction
        arrow.innerHTML = answer.style.display === 'block' ? '&#x25BC;' : '&#x25B6;'; // Cambia entre flecha hacia abajo y hacia la derecha
    });
});
