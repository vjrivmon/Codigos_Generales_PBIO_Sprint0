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

// Función para generar averías aleatorias
function generateRandomFailures() {
    const failureOptions = [
        "Sin batería", 
        "Envío de datos aleatorios", 
        "Avería interna", 
        "Cortocircuito", 
        "Componentes internos fundidos"
    ];

    // Selecciona un número aleatorio de averías entre 1 y 3
    const numFailures = Math.floor(Math.random() * 3) + 1;
    const selectedFailures = [];

    // Elegir aleatoriamente las averías sin repetirse
    while (selectedFailures.length < numFailures) {
        const randomFailure = failureOptions[Math.floor(Math.random() * failureOptions.length)];
        if (!selectedFailures.includes(randomFailure)) {
            selectedFailures.push(randomFailure);
        }
    }

    return selectedFailures;
}


// Generar un nombre aleatorio con un apellido
function generarNombreCompleto() {
    const nombres = ["Antonio", "María", "José", "Francisco", "Laura", "Carmen", "Ana", "Juan", "Isabel", "Miguel"];
    const apellidos = ["García", "Martínez", "López", "Sánchez", "Pérez", "Gómez", "Fernández", "Rodríguez", "Hernández"];
    const nombre = nombres[Math.floor(Math.random() * nombres.length)];
    const apellido = apellidos[Math.floor(Math.random() * apellidos.length)];
    return `${nombre} ${apellido}`;
}


// Asignar nombres a los propietarios de sensores
function asignarPropietarios(sensores) {
    sensores.forEach(sensor => {
        if (!sensor.owner || sensor.owner === "undefined") {
            sensor.owner = generarNombreCompleto(); // Generar un nombre si está vacío o es "undefined"
        }
    });
}

// Llamar a la función después de definir los sensores
asignarPropietarios(sensors);



// Generar datos hasta la última actividad
function generateDataUntilLastActive(lastActiveTimestamp) {
    const now = new Date();
    const lastActive = new Date(lastActiveTimestamp);
    const data = {
        O3: [],
        NO2: [],
        SO3: [],
        labels: []
    };

    // Calcular la diferencia en horas entre la fecha actual y la última actividad
    const diffInHours = Math.min(8, Math.floor((now - lastActive) / (1000 * 60 * 60))); // Máximo 8 horas
    const lastValue = {
        O3: Math.random() * 50 + 10, // Valor inicial aleatorio entre 10 y 60
        NO2: Math.random() * 50 + 10,
        SO3: Math.random() * 50 + 10
    };

    for (let i = diffInHours; i >= 0; i--) {
        const timestamp = new Date(lastActive.getTime() - i * 60 * 60 * 1000); // Hora por hora hacia atrás
        data.labels.push(timestamp.toLocaleTimeString("es-ES", { hour: "2-digit", minute: "2-digit" }));

        // Simular datos decrecientes con ruido aleatorio
        const factor = 1 - i / (diffInHours || 1); // Ajusta proporción para evitar dividir por 0
        data.O3.push((lastValue.O3 * factor + Math.random() * 5).toFixed(2)); // Ruido aleatorio de ±5
        data.NO2.push((lastValue.NO2 * factor + Math.random() * 5).toFixed(2));
        data.SO3.push((lastValue.SO3 * factor + Math.random() * 5).toFixed(2));
    }

    return data;
}


// Crear el gráfico (última actividad o últimas 8 horas)
function createChart(sensor) {
    const isActive = sensor.status === "active";
    const lastActive = new Date(sensor.lastActive);

    // Generar datos según el estado del sensor
    const data = isActive ? generate8HourData() : generateDataUntilLastActive(lastActive);

    // Crear el gráfico
    const ctx = document.createElement("canvas");
    sensorChart.innerHTML = ""; // Limpiar gráfico anterior
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
    sensorTitle.textContent = `Información del sensor con MAC: ${sanitizedMacAddress}`;

     // Aquí usamos `true` o `false` dependiendo de qué gráfico queremos mostrar
     createChart(sensor, true);  // true para las últimas 8 horas, false para hasta la última actividad

    const oldFailures = generateRandomFailures();



    // Crear el historial dinámico
    historyList.innerHTML = `
        <li>Última actividad: ${formatDate(sensor.lastActive)}</li>
        <li>Estado: ${sensor.status === "active" ? "Activo" : "Inactivo"}</li>
        <li>Avería: ${sensor.issue || "Sin averías"}</li>
        <li>Antiguas Averías: ${oldFailures.join(", ") || "Ninguna"}</li>
        <li>Propietario: ${sensor.owner}</li>
        <li>Ubicación: ${sensor.location}</li>

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
asignarPropietarios(sensores); // Asignar propietarios a los sensores
