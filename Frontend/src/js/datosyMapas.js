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

// Función para obtener el valor de una cookie
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

// Función para obtener el id_sensor asociado al correo del usuario
async function obtenerIdSensor(correo) {
    const response = await fetch(`http://localhost:8080/obtenerSensorPorCorreo/${correo}`);
    const data = await response.json();
    return data.id_sensor;
}

// Función para obtener datos de mediciones desde la API
async function obtenerDatosMediciones(id_sensor) {
    const response = await fetch(`http://localhost:8080/mediciones/${id_sensor}`);
    const datos = await response.json();
    return datos;
}

// Función para generar el mapa de interpolación
async function generarMapaDeInterpolacion(id_sensor) {
    const datos = await obtenerDatosMediciones(id_sensor);

    // Crear una capa de interpolación
    const canvas = document.createElement('canvas');
    canvas.width = map.getSize().x;
    canvas.height = map.getSize().y;
    const ctx = canvas.getContext('2d');

    // Interpolar los datos
    const interpolatedData = interpolateData(datos);

    // Dibujar los datos interpolados en el canvas
    interpolatedData.forEach(point => {
        ctx.beginPath();
        ctx.arc(point.x, point.y, 50, 0, 2 * Math.PI, false); // Aumentar el radio para que los círculos choquen
        ctx.fillStyle = getColorForValue(point.value);
        ctx.globalAlpha = 0.03; // Cambiar este valor para la transparencia de la interpolación
        ctx.fill();
    });

    const imageUrl = canvas.toDataURL();
    const imageBounds = map.getBounds();
    const interpolacionLayer = L.imageOverlay(imageUrl, imageBounds);
    interpolacionLayer.addTo(map);

    return interpolacionLayer;
}

// Función para agregar chinchetas con información de los sensores
async function agregarChinchetas(id_sensor) {
    const datos = await obtenerDatosMediciones(id_sensor);

    const chinchetasLayer = L.layerGroup();

    const medicionesPorUbicacion = {};

    datos.forEach(d => {
        const latLng = [d.ubicacion.latitud, d.ubicacion.longitud];
        const key = `${latLng[0]},${latLng[1]}`;

        if (!medicionesPorUbicacion[key]) {
            medicionesPorUbicacion[key] = [];
        }

        medicionesPorUbicacion[key].push(d);
    });

    Object.keys(medicionesPorUbicacion).forEach(key => {
        const mediciones = medicionesPorUbicacion[key];
        const latLng = key.split(',').map(Number);
        const fechaHora = new Date(mediciones[0].fecha_hora).toLocaleString('es-ES', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        let popupContent = `<b>Sensor:</b> ${mediciones[0].id_sensor}<br><b>Fecha y Hora:</b> ${fechaHora}<br>`;

        const iconos = {
            'SO3': 'fas fa-cloud',
            'NO2': 'fas fa-cloud',
            'O3': 'fas fa-cloud',
            'Temperatura': 'fa-solid fa-temperature-low'
        };

        mediciones.forEach(m => {
            const unidad = m.tipo_medicion === 'Temperatura' ? 'ºC' : 'ppm';
            const color = getColorForValue(calcularCalidadAire(m));

            if (unidad === 'ppm') {
                popupContent += `<i class="${iconos[m.tipo_medicion]}"></i> ${m.tipo_medicion}: <span style="color:${color}; font-weight:bold;">${m.valor}</span> ${unidad}<br>`;
            } else {
                popupContent += `<i class="${iconos[m.tipo_medicion]}"></i> ${m.tipo_medicion}: ${m.valor} ${unidad}<br>`;
            }
        });

        popupContent += `
        <a href="infoExtra.html" 
            style="
                padding: 10px 20px; 
                border-radius: 33px; 
                text-decoration: none; 
                margin-left: 0; 
                margin-top: 1rem; 
                font-size: 1em; 
                font-weight: bold; 
                transition: background-color 0.3s, color 0.3s; 
                cursor: pointer; 
                background-color: rgb(57, 88, 134); 
                border: 2px solid rgb(57, 88, 134); 
                color: white; 
                width: 100%; 
                display: block; 
                text-align: center;"
            onmouseover="this.style.backgroundColor='white'; this.style.color='rgb(57, 88, 134)';"
            onmouseout="this.style.backgroundColor='rgb(57, 88, 134)'; this.style.color='white';">
            Más información
        </a>`;
    

        L.marker(latLng).addTo(chinchetasLayer).bindPopup(popupContent);
    });

    return chinchetasLayer;
}


function interpolateData(datos) {
    // Implementar la interpolación espacial (por ejemplo, IDW o Kriging)
    // Aquí se usa un ejemplo simple de interpolación
    const interpolatedData = [];
    for (let i = 0; i < datos.length; i++) {
        const latLng = new L.LatLng(datos[i].ubicacion.latitud, datos[i].ubicacion.longitud);
        const point = map.latLngToContainerPoint(latLng);
        interpolatedData.push({
            x: point.x,
            y: point.y,
            value: calcularCalidadAire(datos[i])
        });
    }
    return interpolatedData;
}

function calcularCalidadAire(medicion) {
    const umbrales = {
        'O3': { bueno: 120, moderado: 180, malo: 240 },
        'NO2': { bueno: 40, moderado: 200, malo: 200 },
        'SO3': { bueno: 125, moderado: 350, malo: 350 }
    };

    const tipo = medicion.tipo_medicion;
    const valor = medicion.valor;

    if (umbrales[tipo]) {
        if (valor > umbrales[tipo].malo) {
            return 100; // Malo
        } else if (valor > umbrales[tipo].moderado) {
            return 50; // Moderado
        } else {
            return 0; // Bueno
        }
    } else {
        return 0; // Si el tipo no está en los umbrales, se considera bueno por defecto
    }
}

function getColorForValue(value) {
    // Definir una escala de colores para los valores
    const colorScale = d3.scaleSequential(d3.interpolateRgbBasis(["#00FF00", "#FFFF00", "#FF0000"])).domain([0, 100]);
    return colorScale(value);
}

// Obtener el correo del usuario de la cookie y generar el mapa de interpolación y chinchetas
const correo = getCookie('correo');
if (correo) {
    obtenerIdSensor(correo).then(async id_sensor => {
        if (id_sensor) {
            const interpolacionLayer = await generarMapaDeInterpolacion(id_sensor);
            const chinchetasLayer = await agregarChinchetas(id_sensor);

            // Agregar control de capas
            const overlayLayers = {
                "Interpolación": interpolacionLayer,
                "Chinchetas": chinchetasLayer
            };

            L.control.layers(null, overlayLayers).addTo(map);
        } else {
            console.error('No se encontró el id_sensor para el correo proporcionado');
        }
    }).catch(error => {
        console.error('Error al obtener el id_sensor:', error);
    });
} else {
    console.error('No se encontró el correo en las cookies');
}