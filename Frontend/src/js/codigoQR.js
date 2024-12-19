// 加载二维码图片
 document.getElementById("qrImage").src = "http://127.0.0.1:5000/generate_qr";

// 发送 PIN 码到后端
function submitPin() {
    const pin = document.getElementById("pinInput").value;
    const formattedPin = pin.match(/.{1,2}/g).join(':'); // Formatear el PIN con dos puntos

    const correo = document.cookie.split('; ').find(row => row.startsWith('correo')).split('=')[1]; // Obtener el correo del usuario de la cookie

    fetch('http://localhost:8080/asociar_dispositivo', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ correo: correo, id_sensor: formattedPin, nombre: 'Sensor', funciona: true })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            window.location.href = 'datosYMapa.html';
        } else {
            alert("Pin inválido o fallo de vinculación.");
        }
    })
    .catch(error => console.error("Error:", error));
}