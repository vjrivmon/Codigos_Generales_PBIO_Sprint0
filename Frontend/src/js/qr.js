// Este código es para leer códigos QR con la cámara del dispositivo. Aquí hay una explicación de lo que hace cada parte:
// 1. Se crea un elemento de video y un elemento de canvas.
// 2. Se obtiene el canvas del HTML.
// 3. Se obtiene el botón de escanear QR.
// 4. Se inicializa la variable scanning en false.
// 5. Se crea una función para encender la cámara.
// 6. Se crea una función para dibujar en el canvas.
// 7. Se crea una función para escanear el código QR.
// 8. Se crea una función para apagar la cámara.
// 9. Se crea una función para activar el sonido.
// 10. Se crea un callback para cuando se lea el código QR.
// 11. Se añade un evento para mostrar la cámara sin el botón.


//crea elemento
const video = document.createElement("video");

//nuestro camvas
const canvasElement = document.getElementById("qr-canvas");
const canvas = canvasElement.getContext("2d");

//div donde llegara nuestro canvas
const btnScanQR = document.getElementById("btn-scan-qr");

//lectura desactivada
let scanning = false;

//funcion para encender la camara
const encenderCamara = () => {
  navigator.mediaDevices
    .getUserMedia({ video: { facingMode: "environment" } })
    .then(function (stream) {
      scanning = true;
      btnScanQR.hidden = true;
      canvasElement.hidden = false;
      video.setAttribute("playsinline", true); // required to tell iOS safari we don't want fullscreen
      video.srcObject = stream;
      video.play();
      tick();
      scan();
    });
};

//funciones para levantar las funiones de encendido de la camara
// tick()
function tick() {
  canvasElement.height = video.videoHeight;
  canvasElement.width = video.videoWidth;
  canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);

  scanning && requestAnimationFrame(tick);
}

// scan()
function scan() {
  try {
    qrcode.decode();
  } catch (e) {
    setTimeout(scan, 300);
  }
}

//apagara la camara
const cerrarCamara = () => {
  video.srcObject.getTracks().forEach((track) => {
    track.stop();
  });
  canvasElement.hidden = true;
  btnScanQR.hidden = false;
};

const activarSonido = () => {
  var audio = document.getElementById('audioScaner');
  audio.play();
}

//callback cuando termina de leer el codigo QR
qrcode.callback = (respuesta) => {
  if (respuesta) {
    //console.log(respuesta);
    Swal.fire(respuesta)
    activarSonido();
    //encenderCamara();    
    cerrarCamara();    

  }
};
//evento para mostrar la camara sin el boton 
window.addEventListener('load', (e) => {
  encenderCamara();
})






