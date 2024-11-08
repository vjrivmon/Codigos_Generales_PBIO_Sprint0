package com.example.biometria3a;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import androidx.appcompat.widget.Toolbar;


// ------------------------------------------------------------------
// ------------------------------------------------------------------



import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private static final String ETIQUETA_LOG2 = "<<<<";

    private static final String ETIQUETA_LOG3 = "zzzzfallozzzz";
    private static final String ETIQUETA_LOG4 = "1111";

    private ImageView menuIcon;
    public Button mandarPost;

    private TextView textViewDispositivos; // Declarar el TextView
    private StringBuilder dispositivosEncontrados; // Para almacenar los dispositivos encontrados
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa el TextView
        textViewDispositivos = findViewById(R.id.dispositivoBtle);
        dispositivosEncontrados = new StringBuilder();

        // Inicializar el NotificationHelper para gestionar notificaciones
        notificationHelper = new NotificationHalper(this);

        // Configurar el Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
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


        mandarPost=findViewById(R.id.mandarPost);
        mandarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton_enviar_pulsado_client(v);
            }
        });
        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");
        inicializarBlueTooth();
        Log.d(ETIQUETA_LOG, " onCreate(): termina ");



        // Encontrar el icono del menú en el Toolbar
        menuIcon = findViewById(R.id.menu_icon);

        // Establecer el listener para abrir el PopupMenu al hacer clic en el icono
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });


        // Inicializar el receiver
        ozoneLevelReceiver = new OzoneLevelReceiver();

        // Registrar el BroadcastReceiver para escuchar los eventos
        IntentFilter filter = new IntentFilter("com.example.biometria3a.OZONE_LEVEL_CHANGED");
        registerReceiver(ozoneLevelReceiver, filter);

        // Ejemplo de valor de CO2 (este valor se podría obtener de tu sensor)
        int valorO3 = 280; // Este es solo un ejemplo, cámbialo por el valor real del sensor

        // Llamar al método para lanzar la notificación según el valor
        lanzarNoti(valorO3);


        Context context = this;
        sendOzoneLevelBroadcast(context, valorO3); // Enviar el broadcast para mostrar la notificación
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void lanzarNoti(int valorO3) {
        if (valorO3 >= SENSOR_DANADO_THRESHOLD) {
            // Si el valor de CO2 es mayor o igual a 500, el sensor puede estar dañado o haciendo lecturas erróneas
            CO2NotificationManager.showCO2AlertNotification(this, valorO3); // Llama a la clase CO2NotificationManager
        } else if (valorO3>= RANGO_MODERADO_MIN &&valorO3 <= RANGO_MODERADO_MAX) {
            // Si el valor de CO2 está entre 180 y 240, es un nivel moderado de calidad del aire
            CO2NotificationManager.showCO2AlertNotification(this, valorO3); // Llama a la clase CO2NotificationManager
        } else if (valorO3 > RANGO_MODERADO_MAX) {
            // Si el valor de CO2 es mayor a 240, es un nivel alto de calidad del aire
            CO2NotificationManager.showCO2AlertNotification(this, valorO3); // Llama a la clase CO2NotificationManager
        }
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

    private void enviarNotificacionAlerta() {
        NotificationHalper notificationHelper = new NotificationHalper(MainActivity.this);
        notificationHelper.showNotification("Alerta", "¡Niveles de O3 muy altos! Valor Major: " + valorMajor);
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
      //  valorMajor=50;

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

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

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




    // --------------------------------------------------------------
    // --------------------------------------------------------------

    private double getMedicionsBeacon (ScanResult resultado) {
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
                super.onScanResult(callbackType, resultado);
                //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                byte[] bytes = resultado.getScanRecord().getBytes();
                TramaIBeacon tib = new TramaIBeacon(bytes);
                if (Utilidades.bytesToString(tib.getUUID()).equals(dispositivoBuscado)) {
                    mostrarInformacionDispositivoBTLE(resultado);
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
                        }
                    });


            }


                else {
                    //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult(): no es el dispositivo buscado ");
                }
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
    private void showPopupMenu(View view) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        // Manejar las acciones del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Usar if-else en lugar de switch para evitar el error
                if (item.getItemId() == R.id.action_about) {
                    Toast.makeText(MainActivity.this, "Sobre Nosotros", Toast.LENGTH_SHORT).show();
                    lanzarSobreNosotros();
                    return true;
                } else if (item.getItemId() == R.id.action_faq) {
                    Toast.makeText(MainActivity.this, "FAQ", Toast.LENGTH_SHORT).show();
                    lanzarFAQ();
                    return true;
                } else if (item.getItemId() == R.id.action_packs) {
                    Toast.makeText(MainActivity.this, "Packs", Toast.LENGTH_SHORT).show();
                    return true;
            } else if (item.getItemId() == R.id.action_privacidad) {
                Toast.makeText(MainActivity.this, "Action Privaciodad", Toast.LENGTH_SHORT).show();
                lanzarPrivacidad();
                return true;
            }else {
                    return false;
                }
            }
        });

        // Mostrar el menú
        popupMenu.show();
    }
    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void lanzarSobreNosotros() {
        Intent intent = new Intent(this, SobreNosotrosActivity.class);
        startActivity(intent);
    }

    private void lanzarFAQ() {
        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    private void lanzarPacks() {
        // Acción o navegación para la opción de "Packs"
        Intent intent = new Intent(this, PacksActivity.class);
        startActivity(intent);
    }

    private void lanzarPrivacidad() {
        // Acción o navegación para la opción de "Packs"
        Intent intent = new Intent(this, PrivacidadAcitivity.class);
        startActivity(intent);
    }

    private void lanzarMapa() {
        // Acción o navegación para la opción de "Packs"
        Intent intent = new Intent(this, Mapa_Activity.class);
        startActivity(intent);
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
        // Llama a obtener la hora actual
        String horaActual = CO2NotificationManager.getCurrentTime();

        // Llama a obtener las coordenadas actuales

        double latitud = getLatitud();
        double longitud = getLongitud();
        // Mostrar las coordenadas en un Toast
        Toast.makeText(this, "Latitud: " + latitud + " Longitud: " + longitud, Toast.LENGTH_SHORT).show();



        int idSensor = 101;
        //double valorGas = valorMajor / 1000;
        double valorGas = 200;
        //double valorTemperatura = valorMinor / 100;
        double valorTemperatura = 32;

        // Crea el objeto Medicion con los datos obtenidos
        Medicion medicion = new Medicion(horaActual, latitud, longitud, idSensor, valorGas, valorTemperatura);

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



}
 // class


// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------


