

// Reemplaza 'ID_SENSOR' con el valor del sensor que deseas consultar
const id_sensor = '00:1A:2B:3M:4D:5E';  // Ejemplo de ID de sensor

// Obtener los datos del backend
fetch(`http://localhost:8080/mediciones/00:1A:2B:3M:4D:5E`)  // Tu backend debería responder a esta ruta
  .then(response => response.json())
  .then(data => {
    console.log('Datos recibidos del backend:', data);  // Ver los datos recibidos

    // Verificar si los datos están vacíos
    if (data.length === 0) {
      console.log('No se encontraron datos para el sensor');
      return;
    }

    // Extraer las horas y los valores de gas de los datos
    const horas = data.map(item => item.hora.slice(0, 2));
    const valoresO3 = data.map(item => item.valorO3);
    const valoresNO2 = data.map(item => item.valorNO2);
    const valoresSO3 = data.map(item => item.valorSO3);

    // Verificar las arrays de horas y valoresGas
    console.log('Horas:', horas);
    console.log('Valores de O3:', valoresO3);
    console.log('Valores de NO2:', valoresNO2);
    console.log('Valores de SO3:', valoresSO3);

    // Calcular la media ponderada de los valores de O3
    const mediaPonderadaO3 = calcularMediaPonderada(valoresO3);
    console.log('Media Ponderada de Gas:', mediaPonderadaO3);

   // Calcular la media ponderada de los valores de NO2
   const mediaPonderadaNO2 = calcularMediaPonderada(valoresNO2);
   console.log('Media Ponderada de Gas:', mediaPonderadaNO2);

   // Calcular la media ponderada de los valores de SO3
   const mediaPonderadaSO3 = calcularMediaPonderada(valoresSO3);
   console.log('Media Ponderada de Gas:', mediaPonderadaSO3);

    // Actualizar los elementos <p> con los nuevos valores de las medias
    document.getElementById('media-O3').textContent = `Valor Medio O3: ${mediaPonderadaO3.toFixed(2)} ppm`;
    document.getElementById('media-NO2').textContent = `Valor Medio NO2: ${mediaPonderadaNO2.toFixed(2)} ppm`;
    document.getElementById('media-SO3').textContent = `Valor Medio SO3: ${mediaPonderadaSO3.toFixed(2)} ppm`;

    // Crear la gráfica con Chart.js
    const ctx = document.getElementById('graficaGas').getContext('2d');
    new Chart(ctx, {
      type: 'line',  // Tipo de gráfico (línea)
      data: {
        labels: horas,  // Etiquetas en el eje X (tiempo)
        datasets: [{
          label: 'Valor de O3 (ppm)',
          data: valoresO3,  // Datos a graficar
          borderColor: 'rgba(59, 59, 59, 1)',  // Color de la línea
          backgroundColor: 'rgba(59, 59, 59, 0.2)',  // Color de fondo de las áreas bajo la línea
          fill: false,  // Llenar el área debajo de la línea
          tension: 0.4  // Suavizado de la línea
        },
        {
          label: 'Valor de NO2 (ppm)',
          data: valoresNO2,  // Datos de temperatura
          borderColor: 'rgba(57, 88, 134, 1)',  // Color de la línea para temperatura
          backgroundColor: 'rgba(57, 88, 134, 0.2)',  // Color de fondo de las áreas bajo la línea para temperatura
          fill: false,  // Llenar el área debajo de la línea de temperatura
          tension: 0.4  // Suavizado de la línea de temperatura
        },
        {
          label: 'Valor de SO3 (ppm)',
          data: valoresSO3 ,  // Datos de temperatura
          borderColor: 'rgba(177, 201, 239, 1)',  // Color de la línea para temperatura
          backgroundColor: 'rgba(177, 201, 239, 0.2)',  // Color de fondo de las áreas bajo la línea para temperatura
          fill: false,  // Llenar el área debajo de la línea de temperatura
          tension: 0.4  // Suavizado de la línea de temperatura
        }]
      },
      options: {
        responsive: true,  // Hacer que el gráfico sea responsive
        scales: {
          x: {
            type: 'category',  // El eje X es categórico (tiempo)
            ticks: {
              // Configura los ticks para que las etiquetas del eje X sean verticales
              autoSkip: false,  // No omitir etiquetas
              maxRotation: 0,  // Rotar las etiquetas 90 grados (vertical)
              minRotation: 0  // Asegurar que las etiquetas estén verticales
            }
            
          },
          y: {
            beginAtZero: true  // Asegura que el eje Y comience en 0
           
          }
        }
      }
    });
  })
  .catch(error => {
    console.error('Error al obtener los datos:', error);
  });

  
  // Función para calcular la media ponderada
  function calcularMediaPonderada(valores) {
    const sumaValores = valores.reduce((acc, valor) => acc + valor, 0);
    const pesoTotal = valores.length; // Si todos los valores tienen el mismo peso
  
    return sumaValores / pesoTotal;  // Media ponderada simple
    }
    var now = new Date();
    var options = { year: 'numeric', month: 'long', day: 'numeric' };  // Solo la fecha
    var formattedDate = now.toLocaleDateString('es-ES', options);
    document.getElementById('current-date').textContent = 'Fecha: ' + formattedDate;
    