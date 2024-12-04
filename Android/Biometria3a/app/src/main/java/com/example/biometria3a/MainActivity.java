package com.example.biometria3a;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.appcompat.widget.Toolbar;


// ------------------------------------------------------------------
// ------------------------------------------------------------------

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private static final String ETIQUETA_LOG2 = "<<<<";

    private static final String ETIQUETA_LOG3 = "zzzzfallozzzz";
    private static final String ETIQUETA_LOG4 = "1111";

    private ImageView menuIcon;
    public Button mandarPost;
    double valorMinor;
    double valorMajor;
    // --------------------------------------------------------------
    private static final int TIMEOUT_MS = 10000;
    private Handler handler = new Handler();
    private NotificationHalper notificationHelper;

    private static final int CODIGO_PETICION_PERMISOS = 11223344;
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;

    private ScanCallback callbackDelEscaneo;
    // --------------------------------------------------------------
    // --------------------------------------------------------------

    private static final int SENSOR_DANADO_THRESHOLD = 500;  // Umbral de valor para sensor dañado
    private static final int RANGO_MODERADO_MIN = 180;  // Umbral para calidad del aire moderada (mínimo)
    private static final int RANGO_MODERADO_MAX = 240;  // Umbral para calidad del aire moderada (máximo)
    private static final int RANGO_ALTO = 240;  // Umbral para calidad del aire alto

    private OzoneLevelReceiver ozoneLevelReceiver;

    private static final long INTERVALO_30_SEGUNDOS = 30 * 1000; // 30 segundos en milisegundos
    private Runnable enviarDatosRunnable;

    // --------------------------------------------------------------
    private TextView tvCurrentTime, tvTemperature, tvOzone;
   // private WebView webView;
    private Button btnUpdate;
    private static final int REQUEST_ENABLE_BT = 1;

    // Crear la instancia de BluetoothHelper
    private BluetoothHelper bluetoothHelper;

    private MenuHandler menuHandler;
    private Runnable timeoutRunnable;

    //---------------------MAPA-------------------------------

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    TextView  dis;


    //--------------RSSI------------------
    private static final int NUM_MUESTRAS = 10;  // Número de muestras para el promedio
    private double[] rssiValores = new double[NUM_MUESTRAS];  // Array para almacenar los valores RSSI
    private int indice = 0;  // Índice para el almacenamiento de valores RSSI



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicializar el helper de Bluetooth
        bluetoothHelper = new BluetoothHelper(this);


        //------------Potencia de luethoot -------------------
      //  TextView tvRssi = findViewById(R.id.dis);
        ImageView imgSignalStrength = findViewById(R.id.iv_signal);





        //------------Automatizar busqueda de mi dispositivo----------------
        // Llama automáticamente al método para iniciar la búsqueda
        // Inicializa el adaptador Bluetooth y el escáner
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e("Error", "Este dispositivo no soporta Bluetooth");
            return; // Termina si el dispositivo no soporta Bluetooth
        }

        // Verifica si Bluetooth está activado, de lo contrario solicita activarlo
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Inicializa el escáner de Bluetooth LE
        elEscanner = bluetoothAdapter.getBluetoothLeScanner();
        if (elEscanner == null) {
            Log.e("Error", "El escáner BLE no está disponible");
            return; // Termina si el escáner es nulo
        }
        String dispositivoBuscado = "ESTO-ES-UN-TEXTO";  // Cambia esto por el nombre del dispositivo que buscas

        buscarEsteDispositivoBTLE300(dispositivoBuscado);
        //---------------mapa----------------
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvOzone = findViewById(R.id.tv_ozone);
        btnUpdate = findViewById(R.id.btn_update);

        // Inicializa el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(MainActivity.this);
        }

        // Inicializa el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verifica permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        //webView = findViewById(R.id.webview_map);


       // loadVerificationFragment();

        // Inicializar el NotificationHelper para gestionar notificaciones
        notificationHelper = new NotificationHalper(this);

        // Configurar el Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Iniciar el envío automático cada 30 segundos
        iniciarEnvioAutomatico();

        // Solicita permisos de localización
        if (!CO2NotificationManager.checkLocationPermission(this)) {
            CO2NotificationManager.requestLocationPermission(this);
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Obtener referencia al ícono de usuario en el Toolbar
        ImageView userIcon = findViewById(R.id.user_icon);

        // Configurar OnClickListener para el ícono de usuario
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir EditProfileActivity
                Intent intent = new Intent(MainActivity.this, EditPerfilActivity.class);
                startActivity(intent);
            }
        });


        mandarPost = findViewById(R.id.mandarPost);
        mandarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton_enviar_pulsado_client(v);
            }
        });
        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");
        inicializarBlueTooth();
        Log.d(ETIQUETA_LOG, " onCreate(): termina ");


        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);

        // Inicializar el receiver
        ozoneLevelReceiver = new OzoneLevelReceiver();

        // Registrar el BroadcastReceiver para escuchar los eventos
        IntentFilter filter = new IntentFilter("com.example.biometria3a.OZONE_LEVEL_CHANGED");
        registerReceiver(ozoneLevelReceiver, filter);

        // Ejemplo de valor de CO2 (este valor se podría obtener de tu sensor)
        //int valorO3 = 280; // Este es solo un ejemplo, cámbialo por el valor real del sensor
        // double valorO3= valorMajor/10000;
        // Llamar al método para lanzar la notificación según el valor
       // lanzarNoti((int) valorO3);
      //  Log.d("MiEtiqueta", "El valor de O3 es: " + valorO3);

        Context context = this;
      //  sendOzoneLevelBroadcast(context, (int) valorO3); // Enviar el broadcast para mostrar la notificación





        updateTime();

        // 更新按钮点击事件
        btnUpdate.setOnClickListener(v -> {
                    // 更新传感器数据的逻辑
                    tvTemperature.setText("Temperatura: 26°C"); // 假设更新后的温度
                    tvOzone.setText("Ozono: 105 ppm"); // 假设更新后的Ozono数据
                    updateTime(); // 更新时间
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Habilitar el botón de ubicación si los permisos están concedidos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getUserLocation();
        } else {
            Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Obtén la ubicación actual
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        // Mueve la cámara al lugar actual
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                        // Añade un marcador en la ubicación actual
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Estás aquí"));
                    } else {
                        Toast.makeText(MainActivity.this, "No se pudo obtener tu ubicación", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    // 更新显示时间
    private void updateTime() {
        String currentTime = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm:ss", new Locale("es", "ES")).format(new Date());
        tvCurrentTime.setText("Hora actual: " + currentTime);
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void lanzarNoti(double valorO3) {
     /*   if (valorO3 >= SENSOR_DANADO_THRESHOLD) {
            // Si el valor de CO2 es mayor o igual a 500, el sensor puede estar dañado o haciendo lecturas erróneas
            CO2NotificationManager.showCO2AlertNotification(this, valorO3); // Llama a la clase CO2NotificationManager
        } else if (valorO3 >= RANGO_MODERADO_MIN && valorO3 <= RANGO_MODERADO_MAX) {
            // Si el valor de CO2 está entre 180 y 240, es un nivel moderado de calidad del aire
            CO2NotificationManager.showCO2AlertNotification(this, valorO3); // Llama a la clase CO2NotificationManager
        } else if (valorO3 > RANGO_MODERADO_MAX) {
            // Si el valor de CO2 es mayor a 240, es un nivel alto de calidad del aire
            CO2NotificationManager.showCO2AlertNotification(this, valorO3); // Llama a la clase CO2NotificationManager
        }

      */

        CO2NotificationManager.showCO2AlertNotification(this, valorO3);
    }

    // Método para enviar el Broadcast
    public void sendOzoneLevelBroadcast(Context context, int valorO3) {
        Intent intent = new Intent("com.example.biometria3a.OZONE_LEVEL_CHANGED"); // Acción personalizada
        intent.putExtra("co2_value", valorO3); // Agregar el valor del CO2 como un extra
        context.sendBroadcast(intent); // Enviar el broadcast
        // Verifica si el mensaje se envió correctamente
        Log.d("MainActivity", "Broadcast Sent with 03 value: " + valorO3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Asegurarse de que el BroadcastReceiver esté activo en primer plano
        if (ozoneLevelReceiver == null) {
            ozoneLevelReceiver = new OzoneLevelReceiver();
        }
        IntentFilter filter = new IntentFilter("com.example.biometria3a.OZONE_LEVEL_CHANGED");
        registerReceiver(ozoneLevelReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar el BroadcastReceiver cuando la actividad no esté visible
        unregisterReceiver(ozoneLevelReceiver);
    }



    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");
                mostrarInformacionDispositivoBTLE(resultado);


            }




            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): NO tengo permisos para escanear ");
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    CODIGO_PETICION_PERMISOS);
            return;
        }
        this.elEscanner.startScan(this.callbackDelEscaneo);


    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------

    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        TramaIBeacon tib = new TramaIBeacon(bytes);

        // Convertir los valores Major y Minor
        valorMajor = Utilidades.bytesToInt(tib.getMajor());
        int txPower = tib.getTxPower();
        // Calcular la distancia
        double distancia = calcularDistancia(rssi, txPower);
        // Mostrar la distancia promedio en el TextView "dis"
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView imageViewSignal = findViewById(R.id.iv_signal);
                TextView textViewDistancia = findViewById(R.id.dis); // Encuentra el TextView por su ID
                if (distancia < 2) {
                    textViewDistancia.setText("Estas al lado del sensor "+ String.format("%.2f", distancia) + " metros");
                    imageViewSignal.setImageResource(R.drawable.signal_3);
                } else if (distancia >= 2 && distancia <= 5) {
                    textViewDistancia.setText("Estas cerca del sensor "+ String.format("%.2f", distancia) + " metros");
                    imageViewSignal.setImageResource(R.drawable.signal_2);
                } else if (distancia > 5) {
                    textViewDistancia.setText("Estas lejos del sensor "+ String.format("%.2f", distancia) + " metros");
                    imageViewSignal.setImageResource(R.drawable.signal_1);
                } // Si la distancia es más de 5 metros y no se detecta señal, poner la imagen gris
                if (distancia > 5 && rssi == -100) { // Asumimos que RSSI -100 indica sin señal
                    imageViewSignal.setImageResource(R.drawable.gris_wwifi); // Imagen cuando no hay señal
                }
                //textViewDistancia.setText("Distancia: " + String.format("%.2f", distancia) + " metros");
            }
        });

        Log.d("DISTANCIASENSOR", "Distancia estimada entre el dispositivo y el sensor: " + distancia + " metros");

        //  valorMajor=50;
       lanzarNoti(valorMajor/10);
        // Usar Handler para esperar 10 segundos antes de mostrar la notificación

        Log.d(ETIQUETA_LOG, "Valor Major detectado: " + valorMajor);

        // Verificar si el valorMajor supera el umbral de 30
       /* if (valorMajor > 30) {
            enviarNotificacionAlerta();
        }
        */
        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.d(ETIQUETA_LOG, "  mostrarInformacionDispositivoBTLE(): NO tengo permisos para conectar ");
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    CODIGO_PETICION_PERMISOS);
            return;
        }
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());


        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        //TramaIBeacon tib = new TramaIBeacon(bytes);
        valorMinor = Utilidades.bytesToInt(tib.getMinor());
        valorMajor = Utilidades.bytesToInt(tib.getMajor());
        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");

        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

    } // ()

    private double calcularDistancia(int rssi, int txPower) {
        final double n = 2.0;  // Valor típico para interiores, puedes ajustar este valor según el entorno

        if (rssi == 0) {
            return -1.0;  // Si el RSSI es 0, no se puede calcular la distancia
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);  // Si la relación RSSI/TXPower es menor que 1, usamos esta fórmula
        } else {
            return  (0.89976) * Math.pow(ratio, 7.7095) + 0.111;  // Usamos otra fórmula si la relación es mayor
        }
    }

    private void mostrarDistancia(double distancia) {
        TextView myTextView = findViewById(R.id.dis);
        if (myTextView != null) {
            Log.d("MainActivity", "TextView encontrado, actualizando texto.");
            if (distancia < 2) {
                myTextView.setText("Estas al lado del sensor");
            } else if (distancia >= 2 && distancia <= 5) {
                myTextView.setText("Estas cerca del sensor");
            } else if (distancia > 5) {
                myTextView.setText("Estas lejos");
            }
        } else {
            Log.e("MainActivity", "El TextView no se encontró.");
        }
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------

    private double getMedicionsBeacon(ScanResult resultado) {
        byte[] bytes = resultado.getScanRecord().getBytes();
        TramaIBeacon tib = new TramaIBeacon(bytes);
        return Utilidades.bytesToInt(tib.getMinor());
    }

    private String obtenerInformacionDispositivoBTLE(ScanResult resultado) {
        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        StringBuilder info = new StringBuilder();
        info.append("Dirección = ").append(bluetoothDevice.getAddress()).append("\n");
        info.append("RSSI = ").append(rssi).append("\n");
        info.append("Bytes = ").append(Utilidades.bytesToHexString(bytes)).append("\n");

        TramaIBeacon tib = new TramaIBeacon(bytes);

        // Extraer valores de Major y Minor
        int major = Utilidades.bytesToInt(tib.getMajor());
        int minor = Utilidades.bytesToInt(tib.getMinor());

        // Añadir Major y Minor al string de información
        info.append("UUID = ").append(Utilidades.bytesToString(tib.getUUID())).append("\n");
        info.append("Major = ").append(major).append("\n");
        info.append("Minor = ").append(minor).append("\n");
        info.append("TxPower = ").append(tib.getTxPower()).append("\n");

        return info.toString();
    }// ()



    // --------------------------------------------------------------
    // --------------------------------------------------------------

    private void buscarEsteDispositivoBTLE300(final String dispositivoBuscado) {
        //Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");

        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía
        // Iniciar el temporizador para la notificación de tiempo de espera
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Si el temporizador se ejecuta, mostrar la notificación
                NotificationHalper notificationHelper = new NotificationHalper(MainActivity.this);
                notificationHelper.showNotification("Aviso", "Dispositivo no encontrado .");
            }
        }, TIMEOUT_MS);


        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                final List<Integer> rssiValues = new ArrayList<>();
                super.onScanResult(callbackType, resultado);
                //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                byte[] bytes = resultado.getScanRecord().getBytes();
                TramaIBeacon tib = new TramaIBeacon(bytes);


                int rssi = resultado.getRssi();
                // Añadimos el valor de RSSI a la lista

                if (Utilidades.bytesToString(tib.getUUID()).equals(dispositivoBuscado)) {
                    mostrarInformacionDispositivoBTLE(resultado);
                    // Reiniciar el temporizador al recibir datos
                    reiniciarTemporizador();
                    final String sensorDatos = obtenerInformacionDispositivoBTLE(resultado);

                    // --------------------------------------------------------------
                    // ---------------------------Valores Sensor TEXTVIEW -----------------------------------
                    // --------------------------------------------------------------

                    // Actualizar el TextView en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tvBluetoothName = findViewById(R.id.valoresSensor);
                            tvBluetoothName.setText("Valores: " + sensorDatos);

                            TextView tvTemperature = findViewById(R.id.tv_temperature);
                            double valorTemperatura = valorMinor / 100;
                            tvTemperature.setText("Temperatura: " +valorTemperatura + " °C");

                            TextView tvOzone = findViewById(R.id.tv_ozone);
                            double valorOzone = valorMajor / 10;
                            tvOzone.setText("Ozono: " +valorOzone + " ppm");
                        }
                        // Actualizar el TextView de la temperatura

                    });



                } else {
                    //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult(): no es el dispositivo buscado ");
                }
            }
            private void reiniciarTemporizador() {
                // Si el temporizador ya estaba corriendo, cancelarlo
                if (timeoutRunnable != null) {
                    handler.removeCallbacks(timeoutRunnable);
                }

                // Crear un nuevo Runnable para el temporizador
                timeoutRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Si no se han recibido datos en el tiempo especificado, mostrar una notificación
                        enviarNotificacionSinDatos();
                    }
                };

                // Iniciar el temporizador
                handler.postDelayed(timeoutRunnable, 10000);
            }

           /* private double calcularDistancia(int txPower, int rssi) {
                return Math.pow(10d, ((double) (txPower - rssi)) / (90 * 4));
            }

            */











            private int calcularNivelSenal(int rssi) {
                if (rssi >= -50) {
                    return 4; // Señal excelente
                } else if (rssi >= -60) {
                    return 3; // Señal buena
                } else if (rssi >= -70) {
                    return 2; // Señal regular
                } else if (rssi >= -80) {
                    return 1; // Señal débil
                } else {
                    return 0; // Sin señal o fuera de rango
                }
            }
            private void actualizarIconoSenal(int nivelDeSenal) {
                ImageView ivSignal = findViewById(R.id.iv_signal);

                switch (nivelDeSenal) {
                    case 3:
                        ivSignal.setImageResource(R.drawable.signal_3); // Imagen de 3 barras
                        break;
                    case 2:
                        ivSignal.setImageResource(R.drawable.signal_1); // Imagen de 2 barras
                        break;
                    case 1:
                        ivSignal.setImageResource(R.drawable.signal_1); // Imagen de 1 barra
                        break;
                    default:
                        ivSignal.setImageResource(R.drawable.gris_wwifi); // Sin señal
                        break;
                }
            }
            private void detectarDesconexion() {
                handler.postDelayed(() -> {
                    NotificationHalper notificationHalper = new NotificationHalper(MainActivity.this);
                    notificationHalper.showNotification("Desconexión", "El dispositivo se ha desconectado.");

                    runOnUiThread(() -> {
                        ImageView ivSignal = findViewById(R.id.iv_signal);
                        ivSignal.setImageResource(R.drawable.gris_wwifi); // Actualiza el icono a sin señal
                    });
                }, TIMEOUT_MS);
            }
            private void enviarNotificacionSinDatos() {
                NotificationHalper notificationHalper = new NotificationHalper(MainActivity.this);
                notificationHalper.showNotification("Alerta de Sensor", "No se han recibido datos del sensor. El sensor está apagado o no disponible.");

            }
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            }
        };
        ScanFilter sf = new ScanFilter.Builder().setDeviceName(dispositivoBuscado).build();

        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado);
        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado
        //      + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): NO tengo permisos para escanear ");
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    CODIGO_PETICION_PERMISOS);
            return;
        }
        this.elEscanner.startScan(this.callbackDelEscaneo);


        // -----------------------Obtener el nombre del dispositivo---------------------------------
        // Obtener el nombre del dispositivo Bluetooth
        String deviceName = dispositivoBuscado;
        if (deviceName == null) {
            deviceName = "Nombre no disponible";  // Si no tiene nombre, mostrar un mensaje por defecto
        }

        // Mostrar en el Log para depuración
        Log.d(ETIQUETA_LOG2, "....Nombre del dispositivo: " + deviceName);
        // Obtener el TextView por su ID y actualizar el texto en el hilo principal
        final String finalDeviceName = deviceName;  // Necesario para acceder dentro de runOnUiThread

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView tvBluetoothName = findViewById(R.id.nuestrodisp);
                tvBluetoothName.setText("Nombre del dispositivo: " + finalDeviceName);


                TextView tvBluetoothValores = findViewById(R.id.valoresSensor);
                tvBluetoothValores.setText(
                        "Valores del sensor: " + "\n" +
                                "Valor Major: " + valorMajor + "\n" +
                                "Valor Minor: " + valorMinor + "\n");

            }

        });
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {

        if (this.callbackDelEscaneo == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.d(ETIQUETA_LOG, "  detenerBusquedaDispositivosBTLE(): NO tengo permisos para escanear ");
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    CODIGO_PETICION_PERMISOS);
            return;
        }
        this.elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado");
        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) );

        this.buscarEsteDispositivoBTLE300("INNOVARESCRECER.");


    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------

    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.d(ETIQUETA_LOG, "  inicializarBlueTooth(): NO tengo permisos para conectar ");
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    CODIGO_PETICION_PERMISOS);
            return;
        }
        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled());

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState());

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");


        this.elEscanner = bta.getBluetoothLeScanner();

        if (this.elEscanner == null) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");

        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);

        } else {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // ()


    // Método para mostrar el PopupMenu
    // Método para mostrar el PopupMenu


    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    // --------------------------------------------------------------
    // --------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Permisos concedidos");
                    inicializarBlueTooth();  // Llamamos a la inicialización si se conceden los permisos
                } else {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Permisos NO concedidos");
                }
                return;
        }
    } // ()
    // --------------------------------------------------------------
    // --------------------------------------------------------------


    //-------------------------enviar el post --------------------
    // --------------------------------Enviar automatico 30seg ------------------------------
    private void iniciarEnvioAutomatico() {
        // Define la tarea de envío de datos
        enviarDatosRunnable = new Runnable() {
            @Override
            public void run() {
                boton_enviar_pulsado_client(null);  // Envía los datos
                handler.postDelayed(this, INTERVALO_30_SEGUNDOS);  // Repite cada 30 segundos
            }
        };
        handler.post(enviarDatosRunnable);  // Inicia la primera ejecución
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(enviarDatosRunnable);
    }

    public void boton_enviar_pulsado_client(View quien) {
        // Obtener fecha y hora actuales
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String horaActual = CO2NotificationManager.getCurrentTime();
        // Llama a obtener las coordenadas actuales

        double latitud = getLatitud();
        double longitud = getLongitud();
        // Mostrar las coordenadas en un Toast
        //Toast.makeText(this, "Latitud: " + latitud + " Longitud: " + longitud, Toast.LENGTH_SHORT).show();
        // Obtener el ID del sensor dinámicamente
        //String idSensor = bluetoothHelper.obtenerIdSensor(this);
       // Log.d("IDsensor", idSensor);


        // Valores de las mediciones (estos serían dinámicos en la práctica)
        double valorO3 =  valorMajor / 1000; // Ejemplo
        double valorTemperatura = valorMinor / 100;
        double valorNO2 = 75.30; // Ejemplo
        double valorSO3 = 10.40; // Ejemplo

       String idSensor = "00:1A:2B:3M:4D:5E";
       // double valorGas = valorMajor / 1000;
       // double valorGas = 200;
     //   double valorTemperatura = valorMinor / 100;
       // double valorTemperatura = 32;

        // Crea el objeto Medicion con los datos obtenidos
        // Crear el objeto Medicion con los nuevos datos
        Medicion medicion = new Medicion(fechaActual, horaActual, latitud, longitud, idSensor,
                valorO3, valorTemperatura, valorNO2, valorSO3);
        // Realiza la llamada para enviar la medición a la API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.enviarMedicion(medicion);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("clienterestandroid", "Medición enviada correctamente");
                    Toast.makeText(MainActivity.this, "Medición enviada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("clienterestandroid", "Error al enviar medición: " + response.code());
                    Toast.makeText(MainActivity.this, "Error al enviar medición", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("clienterestandroid", "Error de conexión: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Manejar la respuesta de solicitud de permisos

    private double getLatitud() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Verifica si los permisos de ubicación han sido otorgados
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return 0;  // Retorna 0 si no tienes permisos
        }

        // Verifica si el GPS está habilitado
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return 0;  // Retorna 0 si el GPS está deshabilitado
        }

        // Obtiene la última ubicación conocida
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Verifica si la ubicación es válida
        if (location != null) {
            return location.getLatitude();  // Retorna la latitud
        } else {
            return 0;  // Si no se puede obtener la ubicación, retorna 0
        }
    }

    private double getLongitud() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Verifica si los permisos de ubicación han sido otorgados
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return 0;  // Retorna 0 si no tienes permisos
        }

        // Verifica si el GPS está habilitado
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return 0;  // Retorna 0 si el GPS está deshabilitado
        }

        // Obtiene la última ubicación conocida
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Verifica si la ubicación es válida
        if (location != null) {
            return location.getLongitude();  // Retorna la longitud
        } else {
            return 0;  // Si no se puede obtener la ubicación, retorna 0
        }
    }


    // Updated method to send POST request
   /* public void boton_enviar_pulsado_client(View quien) {
        Log.d("clienterestandroid", "boton_enviar_pulsado_client");

        // LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  @SuppressLint("MissingPermission") Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // URL de destino
        // URL de destino correcta para enviar la medición

        String urlDestino = "http://172.20.10.5:8080/mediciones";

        // Crear un objeto JSON e introducir valores
        JSONObject postData = new JSONObject();
        try {

    */
            /*postData.put("Medicion", medida.getMedicion());
            postData.put("TipoSensor", medida.getTipoSensor());
            postData.put("Latitud", medida.getLatitud());
            postData.put("Longitud", medida.getLongitud());
            */
    /*
            valorMajor=valorMajor/1000;
            valorMinor=valorMinor/100;
            postData.put("hora", "23:00");
            postData.put("lugar", "Haskovo");
            postData.put("id_sensor", 101);
            postData.put("valorGas", valorMajor);
            postData.put("valorTemperatura", valorMinor);
            // Comprobamos si el valor es mayor a 10
            if (valorMajor >= 3) {
                // Si el valor es mayor o igual a 10, mostramos la notificación
                NotificationHalper notificationHelper = new NotificationHalper(MainActivity.this);
                notificationHelper.showNotification("Aviso", "Altos niveles de O3.");
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("clienterestandroid", "MAAAAAAAAAAAAAAAAAAAAAAAAAAL");
            return; // Exit if JSON creation fails
        }

        // Execute POST request in an AsyncTask
        new PostDataTask(urlDestino, postData).execute();
    }

     */

    private class PostDataTask extends AsyncTask<Void, Void, String> {
        private String urlString;
        private JSONObject jsonData;

        PostDataTask(String urlString, JSONObject jsonData) {
            this.urlString = urlString;
            this.jsonData = jsonData;
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                // Create URL and open connection
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setDoOutput(true);

                Log.d("clienterestandroid", "Enviando datos: " + jsonData.toString());

                // Write JSON data to output stream
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonData.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                int responseCode = urlConnection.getResponseCode();
                Log.d("clienterestandroid", "Código de respuesta: " + responseCode);

                // Read response from input stream
                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
            } catch (Exception e) {
                Log.d("clienterestandroid", "Error: " + e.getMessage());
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result);
                    String success = response.getString("success");
                    String message = response.getString("message");

                    if ("1".equals(success)) {
                        Log.d(ETIQUETA_LOG, "Datos guardados correctamente: " + message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(ETIQUETA_LOG, "Datos guardados incorrectamente: " + message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(ETIQUETA_LOG, "Datos guardados incorrectamente");
            }
        }
    }

 /*   private void loadVerificationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 加载 VerificationFragment
        VerificationFragment verificationFragment = new VerificationFragment();
        fragmentTransaction.replace(R.id.fragment_container, verificationFragment);
        fragmentTransaction.commit();

    }

  */
    // class

}
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------


