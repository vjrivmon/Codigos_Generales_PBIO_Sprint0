// Obtener el popup y elementos relacionados
const sensorInfoPopup = document.getElementById("sensor-info-popup");
const sensorTitle = document.getElementById("sensor-title");
const sensorChart = document.getElementById("sensor-chart");
const historyList = document.getElementById("history-list");
const closePopup = document.getElementById("close-popup");

// Función para generar datos aleatorios
function generateRandomData() {
    return Array.from({ length: 3 }, () => (Math.random() * 50 + 10).toFixed(2)); // Valores entre 10 y 60
}

// Función para crear una gráfica en el popup
function createChart(data) {
    const ctx = document.createElement("canvas");
    sensorChart.innerHTML = ""; // Limpia la gráfica anterior
    sensorChart.appendChild(ctx);

    new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["O3", "NO2", "SO3"],
            datasets: [{
                label: "Medidas (µg/m³)",
                data: data,
                backgroundColor: ["#4caf50", "#ff9800", "#2196f3"],
                borderWidth: 1,
            }],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
        },
    });
}

// Mostrar información del sensor en el popup
function showSensorInfo(macAddress) {
    const sensor = sensors.find(s => s.macAddress === macAddress);
    if (!sensor) return;

    // Actualizar contenido del popup
    sensorTitle.textContent = `Información de ${sensor.name} (${sensor.macAddress})`;
    createChart(generateRandomData());

    // Crear el historial dinámicamente
    historyList.innerHTML = `
        <li>Última actividad: ${formatDate(sensor.lastActive)}</li>
        <li>Estado: ${sensor.status === "active" ? "Activo" : "Inactivo"}</li>
        <li>Avería: ${sensor.issue || "Sin averías"}</li>
    `;

    // Mostrar el popup
    sensorInfoPopup.style.display = "flex";
}

// Cerrar el popup
closePopup.addEventListener("click", () => {
    sensorInfoPopup.style.display = "none";
});

// Asignar eventos de clic a las direcciones MAC
function assignPopupEvents() {
    document.querySelectorAll("#sensor-list tr td:first-child").forEach(cell => {
        const macAddress = cell.textContent.trim();
        cell.style.cursor = "pointer"; // Cambiar el cursor para indicar interactividad
        cell.addEventListener("click", () => showSensorInfo(macAddress));
    });
}

// Reasignar eventos tras renderizar la tabla
function displaySensors(sensors) {
    const tbody = document.getElementById("sensor-list");
    tbody.innerHTML = ""; // Limpia la tabla actual

    sensors.forEach(sensor => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${sensor.macAddress}</td>
            <td>${sensor.location}</td>
            <td>${formatDate(sensor.lastActive)}</td>
            <td>
                <span class="status ${sensor.status}">
                    ${sensor.status === "active" ? "Activo" : "Inactivo"}
                </span>
            </td>
            <td>${sensor.issue || "N/A"}</td>
        `;
        tbody.appendChild(row);
    });

    // Asignar eventos de clic
    assignPopupEvents();
}

// Inicializar la tabla y sus eventos
displaySensors(sensors);
