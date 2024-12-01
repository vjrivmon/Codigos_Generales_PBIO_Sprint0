const now = new Date();
const sixHoursAgo = new Date(now.getTime() - 6 * 60 * 60 * 1000);
const eightHoursAgo = new Date(now.getTime() - 8 * 60 * 60 * 1000);
const twentyFourHoursAgo = new Date(now.getTime() - 24 * 60 * 60 * 1000);

const sensors = [
    { name: "Sensor 1", status: "inactive", issue: "Sin conexión", lastActive: "2024-11-14T12:18:31Z", macAddress: "00:14:22:01:23:45", latitude: 39.4699, longitude: -0.3763 },
    { name: "Sensor 3", status: "inactive", issue: "Batería baja", lastActive: new Date(new Date().getTime() - 6 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:47", latitude: 39.1200, longitude: -0.2734 },
    { name: "Sensor 5", status: "inactive", issue: "Sin conexión", lastActive: new Date(new Date().getTime() - 12 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:49", latitude: 39.4620, longitude: -0.3850 },
    { name: "Sensor 7", status: "inactive", issue: "Batería baja", lastActive: new Date(new Date().getTime() - 2 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:51", latitude: 39.1234, longitude: -0.2456 },
    { name: "Sensor 9", status: "inactive", issue: "Sin conexión", lastActive: new Date(new Date().getTime() - 24 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:53", latitude: 39.4850, longitude: -0.4110 },
    { name: "Sensor 2", status: "active", issue: "Toma datos aleatorios", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:46", latitude: 39.2974, longitude: -0.1900 },
    { name: "Sensor 4", status: "active", issue: " ", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:48", latitude: 39.4667, longitude: -0.3760 },
    { name: "Sensor 6", status: "active", issue: " ", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:50", latitude: 39.4800, longitude: -0.3700 },
    { name: "Sensor 8", status: "active", issue: " ", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:52", latitude: 39.5500, longitude: -0.4000 },
];


const sensorList = document.getElementById("sensor-list");
const filterStatus = document.getElementById("filter-status");
let currentSort = { column: null, order: null }; // Para rastrear columna y orden de ordenamiento

let currentSortColumn = "name";
let sortDirection = "asc";

// Mostrar sensores en la tabla
function displaySensors(sensors) {
    sensorList.innerHTML = ""; // Limpia la tabla
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
            <td>${sensor.issue}</td>
        `;
        sensorList.appendChild(row);
    });
}

// Formatear coordenadas geográficas
function formatCoordinates(lat, lon) {
    return `${Math.abs(lat).toFixed(4)}° ${lat >= 0 ? "N" : "S"}, ${Math.abs(lon).toFixed(4)}° ${lon >= 0 ? "E" : "W"}`;
}

// Añadir la propiedad `location` a cada sensor antes de mostrar la lista
sensors.forEach(sensor => {
    if (sensor.latitude !== undefined && sensor.longitude !== undefined) {
        sensor.location = formatCoordinates(sensor.latitude, sensor.longitude);
    } else {
        sensor.location = "Ubicación no disponible";
    }
});

// Formatear fecha a un formato legible
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString("es-ES", { 
        day: '2-digit', month: '2-digit', year: 'numeric', 
        hour: '2-digit', minute: '2-digit'
    });
}

// Filtra los sensores según el tipo seleccionado
function applyFilters() {
    let filteredSensors = [...sensors];
    
    // Filtrado por estado
    if (filterStatus.value === "active") {
        filteredSensors = filteredSensors.filter(sensor => sensor.status === "active");
    } else if (filterStatus.value === "inactive") {
        filteredSensors = filteredSensors.filter(sensor => sensor.status === "inactive");
    } else if (filterStatus.value === "averiados") {
        filteredSensors = filteredSensors.filter(sensor => sensor.issue && sensor.status === "inactive");
    } else if (filterStatus.value === "noaveriados") {
        filteredSensors = filteredSensors.filter(sensor => !sensor.issue && sensor.status === "inactive");
    }

    // Filtrado por tiempo de inactividad
    else if (filterStatus.value === "act1h") {
        filteredSensors = filteredSensors.filter(sensor => new Date(sensor.lastActive) > now - 1 * 60 * 60 * 1000);
    } else if (filterStatus.value === "act8h") {
        filteredSensors = filteredSensors.filter(sensor => new Date(sensor.lastActive) > eightHoursAgo);
    } else if (filterStatus.value === "act24h") {
        filteredSensors = filteredSensors.filter(sensor => new Date(sensor.lastActive) > twentyFourHoursAgo);
    } else if (filterStatus.value === "act24hmas") {
        filteredSensors = filteredSensors.filter(sensor => new Date(sensor.lastActive) <= twentyFourHoursAgo);
    }

    sortSensors(filteredSensors);
}

// Función para ordenar los sensores
function sortSensors(sensorData) {
    sensorData.sort((a, b) => {
        const valueA = a[currentSortColumn];
        const valueB = b[currentSortColumn];
        if (typeof valueA === "string") {
            return sortDirection === "asc" ? valueA.localeCompare(valueB) : valueB.localeCompare(valueA);
        }
        return sortDirection === "asc" ? valueA - valueB : valueB - valueA;
    });
    displaySensors(sensorData);
}

document.querySelectorAll(".sortable").forEach(header => {
    header.addEventListener("click", () => {
        currentSortColumn = header.dataset.column;
        sortDirection = sortDirection === "asc" ? "desc" : "asc";
        applyFilters();
    });
});

// Actualizar la tabla cuando cambie el filtro
filterStatus.addEventListener("change", applyFilters);
applyFilters();

// Función para ordenar la tabla por columna
function sortTable(column) {
    const newOrder = sortDirection === "asc" ? "desc" : "asc";
    sortDirection = newOrder;
    currentSortColumn = column;
    applyFilters();
    updateSortIndicators(column, newOrder);
}

// Función para actualizar las flechas de ordenamiento
function updateSortIndicators(column, order) {
    const icons = document.querySelectorAll(".sort-icons");
    icons.forEach(icon => {
        const col = icon.getAttribute("data-column");
        icon.querySelector(".asc").classList.remove("active");
        icon.querySelector(".desc").classList.remove("active");
        if (col === column) {
            icon.querySelector(`.${order}`).classList.add("active");
        }
    });
}

// Mostrar sensores al cargar
displaySensors(sensors);
