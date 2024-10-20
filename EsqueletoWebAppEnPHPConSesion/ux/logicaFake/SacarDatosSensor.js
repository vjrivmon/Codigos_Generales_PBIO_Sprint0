// logicaFake/SacarValoresSensor.js

function obtenerDatosDelServidor() {
    // Aquí hacemos la solicitud al servidor REST en localhost:8080
    //  http://192.168.18.157:8080/mediciones
    fetch('http://192.168.0.18:8080/mediciones', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })

        .then(response => {
            // Verifica si la respuesta es exitosa
            if (!response.ok) {
                throw new Error('Error en la solicitud: ' + response.status);
            }
            return response.json(); // Convierte la respuesta a JSON
        })
        .then(data => {
            console.log("Datos recibidos del servidor:", data); // Muestra los datos en la consola

            // Asegúrate de que los datos tienen las propiedades esperadas
            let salida = `
            <strong>Datos del Servidor:</strong><br>
            Hora: ${data.hora || 'Desconocida'}<br>
            Lugar: ${data.lugar || 'Desconocido'}<br>
            ID del sensor: ${data.id_sensor || 'N/A'}<br>
            Valor Gas: ${data.valorGas || 'N/A'}<br>
            Valor Temperatura: ${data.valorTemperatura || 'N/A'}<br>
        `;
            document.getElementById("salida").innerHTML = salida;
        })
        .catch(error => {
            console.error('Error al obtener los datos:', error);
            alert('Error al comunicarse con el servidor: ' + error.message);
        });
}
