// Este código se encarga de obtener los datos de un sensor de gas de un backend y mostrarlos en una gráfica con Chart.js
// También calcula la media ponderada de los valores de gas y los muestra en la página web
// Requiere Chart.js y un backend que responda a la ruta '/mediciones/:idSensor' con los datos del sensor de gas en formato JSON
const id_sensor = '00:1A:2B:3M:4D:5E'; // Ejemplo de ID de sensor
const fechaSelector = document.getElementById('fecha-selector');
const graficaSelector = document.getElementById('gas-selector');
let mediciones = []; // Almacén local de todas las mediciones
let grafica; // Variable global para almacenar la instancia de la gráfica
const tipo = graficaSelector.value; // Tipo de gas seleccionado por defecto
console.log('Tipo seleccionado:', tipo);
const fecha = fechaSelector.value; // Fecha seleccionada por defecto
console.log('Fecha seleccionada:', fecha);

// Obtener todas las mediciones del sensor
function cargarMediciones() {
    fetch(`http://localhost:8080/mediciones/00:1A:2B:3M:4D:5E`)
        .then(response => response.json())
        .then(data => {
            console.log('Mediciones completas:', data);

            // Guardar las mediciones en una variable global
            mediciones = data;
             
            // Cargar datos iniciales para el 22/11/2024
            filtrarMediciones(fecha, tipo);
            

        })
        
}

// Filtrar mediciones por fecha seleccionada
function filtrarMediciones(fecha, tipo) {
    const datosFiltrados = [];

    for (const item of mediciones) {
      if (item.fecha_hora.startsWith(fecha)) {
        datosFiltrados.push(item);
      }
    }
    
    console.log(`Datos filtrados para la fecha ${fecha}:`, datosFiltrados);

    if (datosFiltrados.length === 0) {
        console.log('No se encontraron datos para la fecha seleccionada.');
        return;
    }
    const datosFiltradosO3 = [];
    const datosFiltradosNO2 = [];
    const datosFiltradosSO3 = [];
    const datosFiltradosTemperatura = [];
    
    

    for (const item of datosFiltrados) {
      if (item.tipo_medicion === 'O3') {
        datosFiltradosO3.push(item);
      } else if (item.tipo_medicion === 'NO2') {
        datosFiltradosNO2.push(item);
      } else if (item.tipo_medicion === 'SO3') {
        datosFiltradosSO3.push(item);
      } else if (item.tipo_medicion === 'Temperatura') {
        datosFiltradosTemperatura.push(item);
      }
    }
    // Mostrar los datos filtrados en la consola
    console.log(`Datos filtrados por O3 :`, datosFiltradosO3);
    console.log(`Datos filtrados por NO2 :`, datosFiltradosNO2);
    console.log(`Datos filtrados por SO3 :`, datosFiltradosSO3);
    console.log(`Datos filtrados por Temperatura :`, datosFiltradosTemperatura);

    const ultimosDatosFiltradosO3 = datosFiltradosO3.slice(-8);
    const ultimosDatosFiltradosNO2 = datosFiltradosNO2.slice(-8);
    const ultimosDatosFiltradosSO3 = datosFiltradosSO3.slice(-8);
    const ultimosDatosFiltradosTemperatura = datosFiltradosTemperatura.slice(-8);

    
    // Mostrar los datos filtrados en la consola de las ultimas 8 horas
    console.log(`Datos filtrados por O3 de las ultimas 8 horas:`, ultimosDatosFiltradosO3);
    console.log(`Datos filtrados por NO2 de las ultimas 8 horas:`, ultimosDatosFiltradosNO2);
    console.log(`Datos filtrados por SO3 de las ultimas 8 horas:`, ultimosDatosFiltradosSO3);
    console.log(`Datos filtrados por Temperatura de las ultimas 8 horas:`, ultimosDatosFiltradosTemperatura);

    // Extraer los caracteres desde el índice 5 al 7 de la propiedad fecha_hora para la gráfica
    const horas = ultimosDatosFiltradosO3.map(item => item.fecha_hora.slice(11, 13));
    console.log('Horas para la gráfica:', horas);

    const valoresO3 = datosFiltradosO3.map(item => item.valor);
    const valoresNO2 = datosFiltradosNO2.map(item => item.valor);
    const valoresSO3 = datosFiltradosSO3.map(item => item.valor);
    const valoresTemperatura = datosFiltradosTemperatura.map(item => item.valor);

    // Actualizar los valores medios
        document.getElementById('media-O3').innerHTML = `<i class="fas fa-cloud"></i>   O3: ${calcularMediaPonderada(valoresO3).toFixed(2)} ppm`;
    document.getElementById('media-NO2').innerHTML = `<i class="fas fa-cloud"></i>   NO2: ${calcularMediaPonderada(valoresNO2).toFixed(2)} ppm`;
    document.getElementById('media-SO3').innerHTML = `<i class="fas fa-cloud"></i>   SO3: ${calcularMediaPonderada(valoresSO3).toFixed(2)} ppm`;
    document.getElementById('media-Temperatura').innerHTML = `<i class="fa-solid fa-temperature-low"></i>   Temperatura: ${calcularMediaPonderada(valoresTemperatura).toFixed(2)} ºC`;
    actualizarGraficaPorTipo(tipo, horas, valoresO3, valoresNO2, valoresSO3, valoresTemperatura);
}



// Calcular la media ponderada
function calcularMediaPonderada(valores) {
    const sumaValores = valores.reduce((acc, valor) => acc + valor, 0);
    return sumaValores / valores.length;
}


// Manejar el cambio de fecha en el selector
fechaSelector.addEventListener('change', () => {
    const fecha2 = fechaSelector.value;
    console.log('Fecha seleccionada:', fecha);
    filtrarMediciones(fecha2,tipo);
});

// Manejar el cambio de tipo de gas en el selector
graficaSelector.addEventListener('change', () => {
    const tipo2 = graficaSelector.value;
    console.log('Tipo seleccionado:', tipo);
    filtrarMediciones(fecha, tipo2);
});

// Actualizar la gráfica según el tipo seleccionado
function actualizarGraficaPorTipo(tipo, horas, valoresO3, valoresNO2, valoresSO3, valoresTemperatura) {
    const ctx = document.getElementById('graficaGas').getContext('2d');
    // Destruir la gráfica anterior si ya existe
    if (grafica) {
        grafica.destroy();
    }
    var gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, 'green'); // Color en el valor mínimo (0)
    gradient.addColorStop(1, 'red');   // Color en el valor máximo
    
    switch (tipo) {
        case 'O3':
            // Crear una nueva gráfica
            grafica = new Chart(ctx, {
            type: 'line',
            data: {
                labels: horas,
                datasets: [
                    {
                        label: 'Valor de O3 (ppm)',
                        data: valoresO3,
                        borderColor: 'rgba(59, 59, 59, 1)',
                        backgroundColor: 'rgba(59, 59, 59, 0.2)',                        
                        fill: false,
                        tension: 0.4,
                    },
                ],
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        type: 'category',
                    },
                    y: {
                        beginAtZero: true,
                        max: 250, // Aquí defines el valor máximo del eje Y
                    },
                },
                plugins: {
                    title: {
                        display: true,
                        text: 'Gráfica con los datos recopilados por los sensores en las últimas 8 horas',
                        color: '#395886',
                        font: {
                            size: 40,
                        },
                        margin:{
                            bottom: 16
                        },
                        align: 'center' 

                    },
                    
                }
                
            },
            });
            break;
        case 'NO2':
            // Crear una nueva gráfica
            grafica = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: horas,
                    datasets: [
                        {
                            label: 'Valor de NO2 (ppm)',
                            data: valoresNO2,
                            borderColor: 'rgba(59, 59, 59, 1)',
                            backgroundColor: 'rgba(59, 59, 59, 0.2)',                            
                            fill: false,
                            tension: 0.4,
                        },
                    ],
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            type: 'category',
                        },
                        y: {
                            beginAtZero: true,
                            max: 200, // Aquí defines el valor máximo del eje Y
                        },
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Gráfica con los datos recopilados por los sensores en las últimas 8 horas',
                            color: '#395886',
                            font: {
                                size: 40,
                            },
                            margin:{
                                bottom: 16
                            },
                            align: 'center' 

                        },
                        
                    }
           
                },
                });
            break;
        case 'SO3':
            // Crear una nueva gráfica
            grafica = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: horas,
                    datasets: [
                        {
                            label: 'Valor de SO3 (ppm)',
                            data: valoresSO3,
                            borderColor: 'rgba(59, 59, 59, 1)',
                            backgroundColor: 'rgba(59, 59, 59, 0.2)',                            
                            fill: false,
                            tension: 0.4,
                        },
                    ],
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            type: 'category',
                        },
                        y: {
                            beginAtZero: true,
                            max: 350, // Aquí defines el valor máximo del eje Y
                        },
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Gráfica con los datos recopilados por los sensores en las últimas 8 horas',
                            color: '#395886',
                            font: {
                                size: 40,
                            },
                            margin:{
                                bottom: 16
                            },
                            align: 'center' 

                        },
                        
                    }
               
                },
                });
            break;
        case 'Temperatura':
            // Crear una nueva gráfica
            grafica = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: horas,
                    datasets: [
                        {
                            label: 'Valor de Temperatura (ºC)',
                            data: valoresTemperatura,
                            borderColor: 'rgba(59, 59, 59, 1)',
                            backgroundColor: 'rgba(59, 59, 59, 0.2)',
                            fill: false,
                            tension: 0.4,
                        },
                    ],
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            type: 'category',
                        },
                        y: {
                            min: 20, // Aquí defines el valor mínimo del eje Y
                            max: 40 // Aquí defines el valor máximo del eje Y 
                        },
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Gráfica con los datos recopilados por los sensores en las últimas 8 horas',
                            color: '#395886',
                            font: {
                                size: 40,
                            },
                            margin:{
                                bottom: 16
                            },
                            align: 'center' 

                        },
                        
                    }
                },
            });
            break;
        case 'Todos':
            grafica = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: horas,
                    datasets: [
                        {
                            label: 'Valor de O3 (ppm)',
                            data: valoresO3,
                            borderColor: 'rgba(59, 59, 59, 1)',
                            backgroundColor: 'rgba(59, 59, 59, 0.2)',
                            fill: false,
                            tension: 0.4,
                        },
                        {
                            label: 'Valor de NO2 (ppm)',
                            data: valoresNO2,
                            borderColor: 'rgba(57, 88, 134, 1)',
                            backgroundColor: 'rgba(57, 88, 134, 0.2)',
                            fill: false,
                            tension: 0.4,
                        },
                        {
                            label: 'Valor de SO3 (ppm)',
                            data: valoresSO3,
                            borderColor: 'rgba(177, 201, 239, 1)',
                            backgroundColor: 'rgba(177, 201, 239, 0.2)',
                            fill: false,
                            tension: 0.4,
                        },
                        {
                            label: 'Valor de Temperatura (ºC)',
                            data: valoresTemperatura,
                            borderColor: 'rgba(178, 253, 178, 1)',
                            backgroundColor: 'rgba(178, 253, 178, 0.2)',
                            fill: false,
                            tension: 0.4,
                        },
                    ],
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            type: 'category',
                        },
                        y: {
                            beginAtZero: true,
                        },
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Gráfica con los datos recopilados por los sensores en las últimas 8 horas',
                            color: '#395886',
                            font: {
                                size: 40,
                            },
                            margin:{
                                bottom: 16
                            },
                            align: 'center' 

                        }
                        
                    }
                },
            });
            return;
    }

    
}



// Cargar todas las mediciones al iniciar
cargarMediciones();
