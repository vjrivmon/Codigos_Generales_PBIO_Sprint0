# Sprint0 - Android Studio 

## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Descripcion

Este proyecto es una aplicación Android que escanea dispositivos Bluetooth Low Energy (BTLE), muestra información sobre ellos, y envía datos a un servidor mediante una solicitud HTTP POST. La aplicación está diseñada para trabajar con iBeacons y hacer peticiones REST a un servidor para almacenar datos de medición.

## Características
- Escaneo de dispositivos BTLE disponibles.
- Mostrar información detallada sobre los dispositivos detectados.
- Capacidad para buscar un dispositivo específico utilizando su UUID.
- Envío de datos de sensores a un servidor utilizando solicitudes HTTP POST.

## Requisitos
- Android Studio
- SDK de Android 21 (Lollipop) o superior
- Permisos de Bluetooth y ubicación en el dispositivo

## Uso
- Al iniciar la aplicación, se solicitarán los permisos necesarios.
- Presiona el botón Buscar Dispositivos para comenzar a escanear dispositivos BTLE.
- Si deseas buscar un dispositivo específico, debes insertarlo en el codigo en al funcion del boton poniendo la UUID.
- La información de las mediciones se mostrará en la pantalla.
- Para enviar datos al servidor, presiona el botón Enviar Datos. Los datos se enviarán a la URL especificada en el código.

## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles

## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Clases 

### Medidas
Esta clase representa la medida del sensor biométrico. Contiene información sobre la medición, el tipo de sensor y las coordenadas geográficas.
Métodos:
Getters y setters para cada atributo.
Constructor para inicializar la clase con medición, tipo de sensor, latitud y longitud.

    public Medidas(int medicion, int tipoSensor, double latitud, double longitud) {
        this.medicion = medicion;
        this.tipoSensor = tipoSensor;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    

### PeticionarioREST
Esta clase maneja las peticiones REST utilizando AsyncTask. Permite enviar datos a un servidor y recibir respuestas de manera asíncrona.

### TramaIBeacon
Esta clase representa un iBeacon y sus datos asociados. Maneja la decodificación de un arreglo de bytes para extraer información relevante como UUID, major, minor y el nivel de potencia de transmisión

### Utilidades
La clase Utilidades proporciona una serie de métodos estáticos que son útiles para realizar conversiones y manipulaciones de datos, especialmente relacionados con bytes y UUIDs. 

### Logica
La clase Logica maneja la lógica de la aplicación relacionada con el almacenamiento de mediciones. Se encarga de la funcionalidad principal de la aplicación relacionada con el almacenamiento de datos de medición a través de una API REST.

### MainActivity
La clase MainActivity es la actividad principal de una aplicación Android que se encarga de gestionar la interacción con dispositivos Bluetooth de baja energía (BTLE). Esta clase inicializa el adaptador Bluetooth, solicita permisos necesarios y permite escanear dispositivos BTLE para mostrar información relevante sobre ellos. Además, gestiona la conexión y el envío de datos a un servidor a través de solicitudes HTTP.

inicializarBlueTooth(): Esta función inicializa el adaptador Bluetooth y solicita los permisos necesarios para su uso. Si los permisos son concedidos, habilita el adaptador y obtiene el escáner de Bluetooth LE.

buscarTodosLosDispositivosBTLE(): Esta función inicia el escaneo de dispositivos BTLE y maneja los resultados mediante un callback. Si encuentra un dispositivo, llama a mostrarInformacionDispositivoBTLE() para mostrar su información en el log.

buscarEsteDispositivoBTLE300(final String dispositivoBuscado): Esta función se encarga de buscar un dispositivo Bluetooth LE específico basándose en su nombre. Utiliza un callback para manejar los resultados del escaneo, verificando si el dispositivo encontrado coincide con el nombre proporcionado. Si es así, muestra la información del dispositivo y actualiza la interfaz de usuario con los valores del sensor obtenido.

boton_enviar_pulsado_client(View quien): Esta función se ejecuta cuando el usuario pulsa un botón para enviar datos a un servidor mediante una solicitud HTTP POST. Prepara un objeto JSON con datos relevantes (como hora, lugar, ID del sensor y valores de gas y temperatura), y luego ejecuta una tarea en segundo plano (PostDataTask) para enviar esos datos al servidor

public void boton_enviar_pulsado_client(View quien) {
    String urlDestino = "http://192.168.18.157:8080/mediciones";
    JSONObject postData = new JSONObject();
    try {
        postData.put("hora", "23:00");
        postData.put("lugar", "Haskovo");
        postData.put("id_sensor", 101);
        postData.put("valorGas", 40.0);
        postData.put("valorTemperatura", 35.0);
    } catch (JSONException e) {
        e.printStackTrace();
        return; // Exit if JSON creation fails
    }
    new PostDataTask(urlDestino, postData).execute();
}


## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

### FAQs

### ¿Que es una UUID ?
UUID (Universally Unique Identifier) es un identificador estándar utilizado en sistemas de computación para identificar de manera única información sin necesidad de un sistema centralizado.La probabilidad de que dos UUID generados de forma independiente sean idénticos es extremadamente baja, lo que hace que sean ideales para usar como identificadores únicos en sistemas distribuidos.

### ¿Como se genera una UUID ?
 Un UUID se representa generalmente como una cadena de 32 dígitos hexadecimales, divididos en cinco grupos separados por guiones.

## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
