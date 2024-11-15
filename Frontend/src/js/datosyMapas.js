// 初始化地图，设置瓦伦西亚的经纬度为中心
var map = L.map('map').setView([38.9960, -0.1657], 12); // 瓦伦西亚的经纬度

// 添加OpenStreetMap瓦片图层
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 18,
}).addTo(map);

// 在地图上添加传感器的标记
var sensorMarker = L.marker([38.9960, -0.1657]).addTo(map); // 传感器的位置
sensorMarker.bindPopup("<b>Ubicación del sensor</b><br>Sensor de calidad del aire").openPopup();

// 获取当前时间并格式化为西班牙语时间格式
function updateTime() {
    var now = new Date();
    var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' };
    var formattedTime = now.toLocaleDateString('es-ES', options);
    
    // 更新时间显示
    document.getElementById('current-time').textContent = 'Hora actual: ' + formattedTime;
}

// 页面加载时显示时间
updateTime();

// 每隔一分钟自动更新时间
setInterval(updateTime, 60000); // 60,000毫秒 = 1分钟
