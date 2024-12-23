// Script adaptado para mostrar un heatmap basado en heatmap_acci.html
// Include Leaflet library
const L = window.L;

// Configuración del mapa utilizando Leaflet con heatLayer
var map = L.map('map', {
    center: [39.005541, -0.16747],
    zoom: 13,
    zoomControl: true,
    preferCanvas: false
});

// Añadir capa de mapa base (CARTO Light)
L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors © <a href="https://carto.com/attributions">CARTO</a>',
    maxZoom: 20,
    subdomains: 'abcd'
}).addTo(map);

// Función para obtener datos de mediciones desde la API
async function obtenerDatosMediciones(id_sensor) {
    const response = await fetch(`http://localhost:8080/mediciones/${id_sensor}`);
    const datos = await response.json();
    return datos;
}

// Función para generar el mapa de calor
async function generarMapaDeCalor(id_sensor) {
    const datos = await obtenerDatosMediciones(id_sensor);
    const heatData = datos.map(d => [d.ubicacion.latitud, d.ubicacion.longitud, d.valor]);

    const heatLayer = L.heatLayer(heatData, {
        radius: 20,
        blur: 15,
        maxZoom: 17,
        gradient: {
            0.2: 'blue',
            0.4: 'lime',
            0.6: 'yellow',
            0.8: 'orange',
            1.0: 'red'
        }
    }).addTo(map);
}

// Llamar a la función para generar el mapa de calor con un ID de sensor específico
const id_sensor = '00:1A:2B:3M:4D:5E'; // Reemplaza con el ID del sensor que desees
generarMapaDeCalor(id_sensor);