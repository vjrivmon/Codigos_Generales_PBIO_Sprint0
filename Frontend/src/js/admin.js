/* Este código sirve para mostrar una lista de sensores en una tabla, con la posibilidad de filtrar y ordenar los datos. 
    Variables: 
    - sensors: contiene los datos de los sensores.
    - sensorList: referencia al elemento <tbody> de la tabla.
    - filterStatus: referencia al elemento <select> para filtrar por estado.
    - sortField: referencia al elemento <select> para ordenar por campo.
    - applyFiltersButton: referencia al botón "Aplicar filtros".
    - clearFiltersButton: referencia al botón "Borrar filtros".

    Funciones:
    - displaySensors: muestra los sensores en la tabla.
    - formatCoordinates: convierte las coordenadas de latitud y longitud a formato con N/S y E/W.
    - formatDate: formatea la fecha en formato "dd-mm-aaaa hh:mm:ss".
    - applyFilters: aplica los filtros y ordenamientos seleccionados.

    Eventos:
    - click en "Aplicar filtros": aplica los filtros seleccionados.
    - click en "Borrar filtros": borra los filtros y muestra los datos originales.
    - carga de la página: muestra todos los sensores en la tabla.


*/ 

// Datos de los sensores
const sensors = [
    { name: "Sensor 1", status: "active", lastActive: "2024-11-14T12:00:00Z", macAddress: "00:14:22:01:23:45", latitude: 39.4699, longitude: -0.3763 },
    { name: "Sensor 2", status: "inactive", lastActive: "2024-11-13T10:00:00Z", macAddress: "00:14:22:01:23:46", latitude: 39.2974, longitude: -0.1900 },
    { name: "Sensor 3", status: "inactive", lastActive: "2024-11-12T08:00:00Z", macAddress: "00:14:22:01:23:47", latitude: 39.1200, longitude: -0.2734 },
    { name: "Sensor 4", status: "active", lastActive: "2024-11-15T09:00:00Z", macAddress: "00:14:22:01:23:48", latitude: 39.4220, longitude: -0.2656 },
    { name: "Sensor 5", status: "inactive", lastActive: "2024-11-10T15:00:00Z", macAddress: "00:14:22:01:23:49", latitude: 39.0489, longitude: -0.1710 },
];

const sensorList = document.getElementById("sensor-list");
const filterStatus = document.getElementById("filter-status");
const sortField = document.getElementById("sort-field");
const applyFiltersButton = document.getElementById("apply-filters");
const clearFiltersButton = document.getElementById("clear-filters");

// Función para mostrar los sensores en la tabla
// Función para mostrar los sensores en la tabla

// Función para mostrar los sensores en la tabla
// sensors -> displaySensors() 
function displaySensors(sensors) {
    sensorList.innerHTML = "";
    sensors.forEach(sensor => {
        const row = document.createElement("tr");

        const nameCell = document.createElement("td");
        nameCell.textContent = sensor.name;
        row.appendChild(nameCell);

        const macCell = document.createElement("td");
        macCell.textContent = sensor.macAddress;
        row.appendChild(macCell);

        // Convertir las coordenadas de latitud y longitud
        const positionCell = document.createElement("td");
        positionCell.textContent = formatCoordinates(sensor.latitude, sensor.longitude); // Usamos la función formatCoordinates
        row.appendChild(positionCell);

        // Formatear la fecha y mostrarla
        const lastActiveCell = document.createElement("td");
        lastActiveCell.textContent = formatDate(sensor.lastActive); // Usamos la función formatDate
        row.appendChild(lastActiveCell);

        // Estado de los sensores con clases dinámicas
        const statusCell = document.createElement("td");
        const statusSpan = document.createElement("span");
        statusSpan.classList.add("status"); // Clase común para estado
        // Aplica las clases 'active' o 'inactive' según el estado
        if (sensor.status === "active") {
            statusSpan.classList.add("active");
            statusSpan.textContent = "Activo";
        } else {
            statusSpan.classList.add("inactive");
            statusSpan.textContent = "Inactivo";
        }
        statusCell.appendChild(statusSpan);
        row.appendChild(statusCell);

        sensorList.appendChild(row);
    });
}

// Función para convertir latitud y longitud a formato con N/S y E/W
// latitude, longitude -> formatCoordinates()
function formatCoordinates(latitude, longitude) {
    // Latitud
    const latDirection = latitude >= 0 ? 'N' : 'S';
    const latValue = Math.abs(latitude).toFixed(4); // Aseguramos 4 decimales

    // Longitud
    const lonDirection = longitude >= 0 ? 'E' : 'W';
    const lonValue = Math.abs(longitude).toFixed(4); // Aseguramos 4 decimales

    // Retorna las coordenadas con la dirección
    return `${latValue}° ${latDirection}, ${lonValue}° ${lonDirection}`;
}

// Función para formatear la fecha
// dateString -> formatDate()
function formatDate(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Los meses empiezan desde 0
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    // Formato: día-mes-año hora:minuto:segundo
    return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;  // Devuelve la fecha formateada de forma: dd-mm-aaaa hh:mm:ss
}


// Función para aplicar los filtros y ordenamientos
// applyFilters()
function applyFilters() {
    let filteredSensors = [...sensors]; // Usar la variable correcta `sensors`

    // Filtrar por actividad (activos/inactivos)
    if (filterStatus.value === "active") {
        filteredSensors = filteredSensors.filter(sensor => sensor.status === "active");
    } else if (filterStatus.value === "inactive") {
        filteredSensors = filteredSensors.filter(sensor => sensor.status === "inactive");
    } else if (filterStatus.value === "min24h") {
        filteredSensors = filteredSensors.filter(sensor => {
            const lastActiveDate = new Date(sensor.lastActive);
            const diffInHours = (new Date() - lastActiveDate) / (1000 * 3600);
            return diffInHours <= 24;
        });
    } else if (filterStatus.value === "max24h") {
        filteredSensors = filteredSensors.filter(sensor => {
            const lastActiveDate = new Date(sensor.lastActive);
            const diffInHours = (new Date() - lastActiveDate) / (1000 * 3600);
            return diffInHours > 24;
        });
    }

    // Ordenar según el campo seleccionado
    switch (sortValue) {
        case "nameAsc":
            filteredSensors.sort((a, b) => a.name.localeCompare(b.name));
            break;
        case "nameDesc":
            filteredSensors.sort((a, b) => b.name.localeCompare(a.name));
            break;
        case "macAddressAsc":
            filteredSensors.sort((a, b) => a.macAddress.localeCompare(b.macAddress));
            break;
        case "macAddressDesc":
            filteredSensors.sort((a, b) => b.macAddress.localeCompare(a.macAddress));
            break;
        case "statusAct":
            filteredSensors.sort((a, b) => (a.status === "active" ? -1 : 1));
            break;
        case "statusDesact":
            filteredSensors.sort((a, b) => (a.status === "inactive" ? -1 : 1));
            break;
        case "dateAsc":
            filteredSensors.sort((a, b) => new Date(a.lastActive) - new Date(b.lastActive));
            break;
        case "dateDesc":
            filteredSensors.sort((a, b) => new Date(b.lastActive) - new Date(a.lastActive));
            break;
        case "UbiAsc":
            filteredSensors.sort((a, b) => { // Ordenar por distancia al origen
                const distanceA = Math.sqrt(Math.pow(a.latitude, 2) + Math.pow(a.longitude, 2));
                const distanceB = Math.sqrt(Math.pow(b.latitude, 2) + Math.pow(b.longitude, 2));
                return distanceA - distanceB;
            });
            break;
        case "UbiDesc":
            filteredSensors.sort((a, b) => { // Ordenar por distancia al origen (inverso)
                const distanceA = Math.sqrt(Math.pow(a.latitude, 2) + Math.pow(a.longitude, 2));
                const distanceB = Math.sqrt(Math.pow(b.latitude, 2) + Math.pow(b.longitude, 2));
                return distanceB - distanceA;
            });
            break;
    }

    displaySensors(filteredSensors); // Muestra los sensores después de aplicar filtros y orden
}

// Aplicar filtros al hacer clic en "Aplicar filtros"
applyFiltersButton.addEventListener("click", applyFilters);

// Borrar filtros al hacer clic en "Borrar filtros"
clearFiltersButton.addEventListener("click", function() {
    filterStatus.value = "all";
    sortField.value = "nameAsc";
    displaySensors(sensors); // Mostrar datos originales
});

// Mostrar todos los sensores al cargar la página
displaySensors(sensors);
