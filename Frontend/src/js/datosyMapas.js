// 初始化地图，设置瓦伦西亚的经纬度为中心
// Este código sirve para mostrar un mapa de OpenStreetMap en la página web y añadir un marcador en la ubicación del sensor de calidad del aire.
// También muestra la hora actual en el formato de fecha y hora en español.
// Se actualiza la hora cada minuto.

// Se crea un mapa de Leaflet y se establece la vista en Valencia, España, con un nivel de zoom de 12.
var map = L.map('map').setView([38.9960, -0.1657], 12); // 瓦伦西亚的经纬度

// 添加OpenStreetMap瓦片图层
// Esto permite mostrar un mapa de OpenStreetMap en la página web.
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 18,
}).addTo(map);

// 在地图上添加传感器的标记
// Esto añade un marcador en la ubicación del sensor de calidad del aire.
var sensorMarker = L.marker([38.9960, -0.1657]).addTo(map); // 传感器的位置
sensorMarker.bindPopup("<b>Ubicación del sensor</b><br>Sensor de calidad del aire").openPopup();  // Esto muestra un mensaje emergente en el marcador.

// 获取当前时间并格式化为西班牙语时间格式
// Esta función obtiene la hora actual y la formatea en el formato de fecha y hora en español.
// -> updateTime()
function updateTime() {
    var now = new Date();
    var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' };
    var formattedTime = now.toLocaleDateString('es-ES', options);
    
    // 更新时间显示
    document.getElementById('current-time').textContent = 'Hora actual: ' + formattedTime;  // Esto muestra la hora actual en la página web.
}

// 页面加载时显示时间
updateTime(); //Se llama a la función updateTime() para mostrar la hora actual en la página web.

// 每隔一分钟自动更新时间
setInterval(updateTime, 60000); // 60,000毫秒 = 1分钟 // Esta función actualiza la hora cada minuto.
