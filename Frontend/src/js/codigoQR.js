 // 加载二维码图片
 document.getElementById("qrImage").src = "http://127.0.0.1:5000/generate_qr";

 // 发送 PIN 码到后端
 function submitPin() {
     const pin = document.getElementById("pinInput").value;
     fetch('http://127.0.0.1:5000/api/associate_device', {
         method: 'POST',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify({ pin: pin })
     })
     .then(response => response.json())
     .then(data => {
         if (data.success) {
             alert(data.message);
         } else {
             alert("Pin inválido o fallo de vinculación.");
         }
     })
     .catch(error => console.error("Error:", error));
 }