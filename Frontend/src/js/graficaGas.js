const id_sensor = '00:1A:2B:3M:4D:5E'; // Ejemplo de ID de sensor
const fechaSelector = document.getElementById('fecha-selector');
let mediciones = []; // Almacén local de todas las mediciones
let grafica; // Variable global para almacenar la instancia de la gráfica

// Obtener todas las mediciones del sensor
function cargarMediciones() {
    fetch(`http://localhost:8080/mediciones/00:1A:2B:3M:4D:5E`)
        .then(response => response.json())
        .then(data => {
            console.log('Mediciones completas:', data);

            // Guardar las mediciones en una variable global
            mediciones = data;

            // Cargar datos iniciales para el 22/11/2024
            filtrarMedicionesPorFecha('22/11/2024');
        })
        .catch(error => console.error('Error al obtener todas las mediciones:', error));
}

// Filtrar mediciones por fecha seleccionada
function filtrarMedicionesPorFecha(fecha) {
    const datosFiltrados = mediciones.filter(item => item.fecha === fecha);
    console.log(`Datos filtrados para la fecha ${fecha}:`, datosFiltrados);

    if (datosFiltrados.length === 0) {
        console.log('No se encontraron datos para la fecha seleccionada.');
        return;
    }

    // Extraer datos relevantes para la gráfica
    const horas = datosFiltrados.map(item => item.hora.slice(0, 2));
    const valoresO3 = datosFiltrados.map(item => item.valorO3);
    const valoresNO2 = datosFiltrados.map(item => item.valorNO2);
    const valoresSO3 = datosFiltrados.map(item => item.valorSO3);

    // Actualizar los valores medios
    document.getElementById('media-O3').textContent = `Valor Medio O3: ${calcularMediaPonderada(valoresO3).toFixed(2)} ppm`;
    document.getElementById('media-NO2').textContent = `Valor Medio NO2: ${calcularMediaPonderada(valoresNO2).toFixed(2)} ppm`;
    document.getElementById('media-SO3').textContent = `Valor Medio SO3: ${calcularMediaPonderada(valoresSO3).toFixed(2)} ppm`;

    // Actualizar la gráfica
    actualizarGrafica(horas, valoresO3, valoresNO2, valoresSO3);
}

// Actualiza la gráfica con nuevos datos
function actualizarGrafica(horas, valoresO3, valoresNO2, valoresSO3) {
    const ctx = document.getElementById('graficaGas').getContext('2d');

    // Destruir la gráfica anterior si ya existe
    if (grafica) {
        grafica.destroy();
    }

    // Crear una nueva gráfica
    grafica = new Chart(ctx, {
        type: 'line',
        data: {
            labels: horas,
            datasets: [
                {
                    label: 'Valor de O3 (ppm)',
                    data: valoresO3,
                    borderColor: 'rgba(59, 59, 59, 1)',
                    backgroundColor: 'rgba(59, 59, 59, 0.2)',
                    fill: false,
                    tension: 0.4,
                },
                {
                    label: 'Valor de NO2 (ppm)',
                    data: valoresNO2,
                    borderColor: 'rgba(57, 88, 134, 1)',
                    backgroundColor: 'rgba(57, 88, 134, 0.2)',
                    fill: false,
                    tension: 0.4,
                },
                {
                    label: 'Valor de SO3 (ppm)',
                    data: valoresSO3,
                    borderColor: 'rgba(177, 201, 239, 1)',
                    backgroundColor: 'rgba(177, 201, 239, 0.2)',
                    fill: false,
                    tension: 0.4,
                },
            ],
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    type: 'category',
                },
                y: {
                    beginAtZero: true,
                },
            },
        },
    });
}

// Calcular la media ponderada
function calcularMediaPonderada(valores) {
    const sumaValores = valores.reduce((acc, valor) => acc + valor, 0);
    return sumaValores / valores.length;
}

// Manejar el cambio de fecha en el selector
fechaSelector.addEventListener('change', () => {
    const fechaSeleccionada = fechaSelector.value;
    filtrarMedicionesPorFecha(fechaSeleccionada);
});

// Cargar todas las mediciones al iniciar
cargarMediciones();
