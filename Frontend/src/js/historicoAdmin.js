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


// Generar datos de ejemplo para las últimas 8 horas
function generate8HourData() {
    const now = new Date();
    const data = {
        O3: [],
        NO2: [],
        SO3: [],
        labels: []
    };

    for (let i = 7; i >= 0; i--) {
        const timestamp = new Date(now.getTime() - i * 60 * 60 * 1000); // Cada hora
        data.labels.push(timestamp.toLocaleTimeString("es-ES", { hour: "2-digit", minute: "2-digit" }));
        data.O3.push((Math.random() * 50 + 10).toFixed(2)); // Valores entre 10 y 60
        data.NO2.push((Math.random() * 50 + 10).toFixed(2));
        data.SO3.push((Math.random() * 50 + 10).toFixed(2));
    }

    return data;
}


// Crear el gráfico con datos de las últimas 8 horas
function create8HourChart(data) {
    const ctx = document.createElement("canvas");
    sensorChart.innerHTML = ""; // Limpia el gráfico anterior
    sensorChart.appendChild(ctx);

    new Chart(ctx, {
        type: "line",
        data: {
            labels: data.labels,
            datasets: [
                {
                    label: "O3",
                    data: data.O3,
                    borderColor: "#4caf50",
                    backgroundColor: "rgba(76, 175, 80, 0.2)",
                    fill: true,
                    tension: 0.3
                },
                {
                    label: "NO2",
                    data: data.NO2,
                    borderColor: "#ff9800",
                    backgroundColor: "rgba(255, 152, 0, 0.2)",
                    fill: true,
                    tension: 0.3
                },
                {
                    label: "SO3",
                    data: data.SO3,
                    borderColor: "#2196f3",
                    backgroundColor: "rgba(33, 150, 243, 0.2)",
                    fill: true,
                    tension: 0.3
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true, position: "top" },
                tooltip: { mode: "index", intersect: false }
            },
            scales: {
                x: {
                    title: { display: true, text: "Hora" }
                },
                y: {
                    title: { display: true, text: "Concentración (µg/m³)" },
                    beginAtZero: true
                }
            },
            interaction: {
                mode: "index",
                intersect: false
            }
        }
    });
}

// Mostrar información del sensor en el popup
function showSensorInfo(macAddress) {
    const sensor = sensors.find(s => s.macAddress === macAddress);
    if (!sensor) return;

      // Asegurarse de eliminar el carácter ℹ️ del título, si está presente
    const sanitizedMacAddress = macAddress.replace(/ℹ️/g, ""); 

    // Actualizar título del popup con la dirección MAC limpia
    sensorTitle.textContent = `Información del sensor ${sanitizedMacAddress}`;

   // Crear gráfico con datos de las últimas 8 horas
    const data = generate8HourData();
    create8HourChart(data);

    // Crear el historial dinámico
    historyList.innerHTML = `
        <li>Última actividad: ${formatDate(sensor.lastActive)}</li>
        <li>Estado: ${sensor.status === "active" ? "Activo" : "Inactivo"}</li>
        <li>Avería: ${sensor.issue || "Sin averías"}</li>
    `;

    // Mostrar popup
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
