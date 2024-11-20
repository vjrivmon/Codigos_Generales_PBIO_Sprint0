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
const now = new Date();
const sixHoursAgo = new Date(now.getTime() - 6 * 60 * 60 * 1000);

const sensors = [
    { name: "Sensor 1", status: "inactive", lastActive: "2024-11-14T12:18:31Z", macAddress: "00:14:22:01:23:45", latitude: 39.4699, longitude: -0.3763 },
    { name: "Sensor 2", status: "active", lastActive: now.toISOString(), macAddress: "00:14:22:01:23:46", latitude: 39.2974, longitude: -0.1900 },
    { name: "Sensor 3", status: "inactive", lastActive: sixHoursAgo.toISOString(), macAddress: "00:14:22:01:23:47", latitude: 39.1200, longitude: -0.2734 },
    { name: "Sensor 4", status: "inactive", lastActive: "2024-11-15T09:15:56Z", macAddress: "00:14:22:01:23:48", latitude: 39.4220, longitude: -0.2656 },
    { name: "Sensor 5", status: "inactive", lastActive: "2024-11-10T15:35:23Z", macAddress: "00:14:22:01:23:49", latitude: 39.0489, longitude: -0.1710 },
    { name: "Sensor 6", status: "active", lastActive: now.toISOString(), macAddress: "00:14:22:01:23:48", latitude: 39.4220, longitude: -0.2656 }
];

const sensorList = document.getElementById("sensor-list");
const filterStatus = document.getElementById("filter-status");
const sortField = document.getElementById("sort-field");
const applyFiltersButton = document.getElementById("apply-filters");
const clearFiltersButton = document.getElementById("clear-filters");

// Función para mostrar los sensores en la tabla
function displaySensors(sensors) {
    sensorList.innerHTML = "";
    sensors.forEach(sensor => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${sensor.name}</td>
            <td>${sensor.macAddress}</td>
            <td>${formatCoordinates(sensor.latitude, sensor.longitude)}</td>
            <td>${formatDate(sensor.lastActive)}</td>
            <td>
                <span class="status ${sensor.status}">
                    ${sensor.status === "active" ? "Activo" : "Inactivo"}
                </span>
            </td>
        `;
        sensorList.appendChild(row);
    });
}

// Formatear coordenadas
function formatCoordinates(lat, lon) {
    return `${Math.abs(lat).toFixed(4)}° ${lat >= 0 ? "N" : "S"}, ${Math.abs(lon).toFixed(4)}° ${lon >= 0 ? "E" : "W"}`;
}

// Formatear fecha
function formatDate(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
}

// Aplicar filtros
function applyFilters() {
    let filteredSensors = [...sensors];

    // Filtrar por estado
    if (filterStatus.value !== "all") {
        filteredSensors = filteredSensors.filter(sensor => {
            if (filterStatus.value === "min24h") {
                return (new Date() - new Date(sensor.lastActive)) / (1000 * 3600) <= 24;
            } else if (filterStatus.value === "max24h") {
                return (new Date() - new Date(sensor.lastActive)) / (1000 * 3600) > 24;
            }
            return sensor.status === filterStatus.value;
        });
    }

    // Ordenar
    const sortValue = sortField.value;
    switch (sortValue) {
        case "nameAsc": filteredSensors.sort((a, b) => a.name.localeCompare(b.name)); break;
        case "nameDesc": filteredSensors.sort((a, b) => b.name.localeCompare(a.name)); break;
        case "macAddressAsc": filteredSensors.sort((a, b) => a.macAddress.localeCompare(b.macAddress)); break;
        case "macAddressDesc": filteredSensors.sort((a, b) => b.macAddress.localeCompare(a.macAddress)); break;
        case "statusAct": filteredSensors.sort((a, b) => a.status.localeCompare(b.status)); break;
        case "statusDesact": filteredSensors.sort((a, b) => b.status.localeCompare(a.status)); break;
        case "dateAsc": filteredSensors.sort((a, b) => new Date(a.lastActive) - new Date(b.lastActive)); break;
        case "dateDesc": filteredSensors.sort((a, b) => new Date(b.lastActive) - new Date(a.lastActive)); break;
    }

    displaySensors(filteredSensors);
}

// Eventos
applyFiltersButton.addEventListener("click", applyFilters);

// Mostrar sensores al cargar
displaySensors(sensors);



/* --------------- POPUP BORRAR CAMBIOS ---------------- */
/*-------------------------------- APARECER POPUP ---------------------------------*/
const popupBorrarFiltros = document.getElementById('popup-borrar-filtros');
const cancelBtnFiltros = document.getElementById('cancelBtn-filtros');
const confirmBtnFiltros = document.getElementById('confirmBtn-filtros'); // Referencia al contenedor del teléfono

// Mostrar el popup cuando se hace clic en "¿Has olvidado tu contraseña?"
clearFiltersButton.addEventListener('click', function() {
    popupBorrarFiltros.style.display = 'flex'; 
});

/*-------------------- CONFIRMAR --------------------------*/
confirmBtnFiltros.addEventListener('click', () => { 
    filterStatus.value = "all";
    sortField.value = "nameAsc";
    displaySensors(sensors);
    popupBorrarFiltros.style.display = 'none'; // Oculta el popup
});

/*----------------------- CANCELAR  ------------------------*/
cancelBtnFiltros.addEventListener('click', function() {
    popupBorrarFiltros.style.display = 'none'; // Oculta el popup
});
