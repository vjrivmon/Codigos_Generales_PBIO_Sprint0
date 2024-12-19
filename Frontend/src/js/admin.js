const now = new Date();
const sixHoursAgo = new Date(now.getTime() - 6 * 60 * 60 * 1000);
const eightHoursAgo = new Date(now.getTime() - 8 * 60 * 60 * 1000);
const twentyFourHoursAgo = new Date(now.getTime() - 24 * 60 * 60 * 1000);

const sensors = [
    { name: "Sensor 1", status: "inactive", issue: "Sin conexión", lastActive: "2024-11-14T12:18:31Z", macAddress: "00:14:22:01:23:45 ℹ️", latitude: 39.4699, longitude: -0.3763 },
    { name: "Sensor 3", status: "inactive", issue: "Batería baja", lastActive: new Date(new Date().getTime() - 6 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:47 ℹ️", latitude: 39.1200, longitude: -0.2734 },
    { name: "Sensor 5", status: "inactive", issue: "Sin conexión", lastActive: new Date(new Date().getTime() - 12 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:49 ℹ️", latitude: 39.4620, longitude: -0.3850 },
    { name: "Sensor 7", status: "inactive", issue: "Batería baja", lastActive: new Date(new Date().getTime() - 2 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:51 ℹ️", latitude: 39.1234, longitude: -0.2456 },
    { name: "Sensor 9", status: "inactive", issue: "Sin conexión", lastActive: new Date(new Date().getTime() - 24 * 60 * 60 * 1000).toISOString(), macAddress: "00:14:22:01:23:53 ℹ️", latitude: 39.4850, longitude: -0.4110 },
    { name: "Sensor 2", status: "active", issue: "Toma datos aleatorios", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:46 ℹ️", latitude: 39.2974, longitude: -0.1900 },
    { name: "Sensor 4", status: "active", issue: "Ninguna", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:48 ℹ️", latitude: 39.4667, longitude: -0.3760 },
    { name: "Sensor 6", status: "active", issue: "Ninguna", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:50 ℹ️", latitude: 39.4800, longitude: -0.3700 },
    { name: "Sensor 8", status: "active", issue: "Ninguna", lastActive: new Date().toISOString(), macAddress: "00:14:22:01:23:52 ℹ️", latitude: 39.5500, longitude: -0.4000 },
];



const sensorList = document.getElementById("sensor-list");
const filterStatus = document.getElementById("filter-status");
let currentSort = { column: null, order: null }; // Para rastrear columna y orden de ordenamiento

let currentSortColumn = "name";
let sortDirection = "asc";

// Mostrar sensores en la tabla
/*function displaySensors(sensors) {
    const tbody = document.getElementById("sensor-list");
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
}*/


function displaySensors(sensors) {
    const sensorList = document.getElementById("sensor-list");
    sensorList.innerHTML = ""; // Limpia el contenido actual de la tabla

    sensors.forEach(sensor => {
        const row = document.createElement("tr");

        // Dirección MAC
        const macCell = document.createElement("td");
        const macLink = document.createElement("a");
        macLink.textContent = sensor.macAddress;
        macLink.style.color = "blue";
        macLink.style.textDecoration = "underline";
        macLink.style.cursor = "pointer";
        macCell.appendChild(macLink);
        row.appendChild(macCell);

        // Ubicación
        const locationCell = document.createElement("td");
        locationCell.textContent = sensor.location || "Ubicación no disponible";
        row.appendChild(locationCell);

        // Última Actividad
        const lastActiveCell = document.createElement("td");
        lastActiveCell.textContent = formatDate(sensor.lastActive);
        row.appendChild(lastActiveCell);

        // Estado
        const statusCell = document.createElement("td");
        const statusSpan = document.createElement("span");
        statusSpan.className = `status ${sensor.status}`;
        statusSpan.textContent = sensor.status === "active" ? "Activo" : "Inactivo";
        statusCell.appendChild(statusSpan);
        row.appendChild(statusCell);

        // Avería
        const issueCell = document.createElement("td");
        issueCell.textContent = sensor.issue.trim() || "Ninguna";
        row.appendChild(issueCell);

        // Añadir la fila a la tabla
        sensorList.appendChild(row);
    });
}



document.addEventListener("DOMContentLoaded", () => {
    displaySensors(sensors);
});

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
        filteredSensors = filteredSensors.filter(sensor => sensor.issue !== "Ninguna");
    } 

   else if (filterStatus.value === "act8h") {
    // Inactivos en las últimas 8 horas
    filteredSensors = filteredSensors.filter(sensor => 
        sensor.status === "inactive" && new Date(sensor.lastActive) >= eightHoursAgo
    );
} else if (filterStatus.value === "act24h") {
    // Inactivos en las últimas 24 horas
    filteredSensors = filteredSensors.filter(sensor => 
        sensor.status === "inactive" && new Date(sensor.lastActive) >= twentyFourHoursAgo
    );
}
    else if (filterStatus.value === "act24hmas") {
         // Inactivos hace más de 24 horas
    filteredSensors = filteredSensors.filter(sensor => 
        sensor.status === "inactive" && new Date(sensor.lastActive) > twentyFourHoursAgo
    );
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
function sortTable(column, direction) {
    sortDirection = direction; // Actualizamos la dirección según lo que se hizo clic
    currentSortColumn = column;
    applyFilters(); // Filtramos y ordenamos los datos
    updateSortIndicators(column, direction);
}

// Función para actualizar las flechas de ordenamiento
function updateSortIndicators(column, direction) {
    const icons = document.querySelectorAll(".sort-icons"); // Seleccionamos todos los íconos de las columnas
    icons.forEach(icon => {
        const col = icon.getAttribute("data-column");
        const ascIcon = icon.querySelector(".asc");
        const descIcon = icon.querySelector(".desc");

        // Reiniciamos las clases activas
        ascIcon.classList.remove("active");
        descIcon.classList.remove("active");

        // Activamos solo la flecha correcta para la columna actual
        if (col === column) {
            if (direction === "asc") {
                ascIcon.classList.add("active");
            } else if (direction === "desc") {
                descIcon.classList.add("active");
            }
        }
    });
}


// Configurar los eventos de clic para las columnas con ordenamiento
document.querySelectorAll("th span.sort-icons").forEach(header => {
    header.addEventListener("click", event => {
        const column = header.getAttribute("data-column");
        const isAscActive = header.querySelector(".asc").classList.contains("active");

        // Alternamos entre 'asc' y 'desc' según el estado actual
        const direction = isAscActive ? "desc" : "asc";
        sortTable(column, direction);
    });
});





let sortDirections = {};

function toggleSortDirection(column) {
    // Si no existe, se inicializa como 'asc'
    if (!sortDirections[column]) {
        sortDirections[column] = 'asc';
    } else {
        // Alternar entre 'asc' y 'desc'
        sortDirections[column] = sortDirections[column] === 'asc' ? 'desc' : 'asc';
    }
    return sortDirections[column];
}




// Mostrar sensores al cargar
displaySensors(sensors);
