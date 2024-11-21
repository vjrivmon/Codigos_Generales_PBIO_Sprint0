package com.example.biometria3a;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.Manifest;
import android.widget.Toast;



public class CO2NotificationManager {

    private static final String CHANNEL_ID = "co2_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_PERMISSION_CODE = 100; // Código para la solicitud de permisos

    public static void showCO2AlertNotification(Context context, double co2Value) {
        // Verificar si se tiene el permiso de localización
        if (checkLocationPermission(context)) {
            // Si se tiene el permiso, proceder con la notificación
            String currentTime = getCurrentTime();
            String gpsCoordinates = getGpsCoordinates(context);

            // Establecer un mensaje dinámico basado en el valor de CO2
            String title = "Alerta de O3!";
            String contentText = "Nivel de 03: " + co2Value + " en " + currentTime + ". Coordenadas GPS: " + gpsCoordinates;

            // Definir el mensaje dependiendo del nivel de CO2
            String additionalInfo = "";
            /*if (co2Value > 500) {
                // Nivel peligroso
                contentText = "¡Nivel de 03 peligrosamente alto! " + contentText;
                additionalInfo = "¡El nivel de 03 ha superado los 500! Es peligroso para la salud.";
            }  else if (co2Value >= 240 && co2Value <= 500) {
            // Nivel moderado
            contentText = "Nivel moderado de ozono detectado.\n";
            additionalInfo = "La concentración de ozono se encuentra en un nivel moderado (entre 240 y 500 ppb).\n"+
            "Es recomendable tomar precauciones, especialmente si se pertenece a grupos sensibles, como niños o personas con problemas respiratorios.\n"+
            "Asegúrese de mantenerse informado sobre la calidad del aire y considere limitar la actividad al aire libre en este momento.\n"+
            "Fecha y hora: " + currentTime + ".\n" +
                    "Nivel de Ozono: " + co2Value + ".\n"+
                "Coordenadas GPS: " + gpsCoordinates+ ".\n" ;
    } else if (co2Value >= 180) {
                // Nivel bajo pero no ideal
                contentText = "Nivel de CO2 aceptable, pero mejorable.";
                additionalInfo = "El nivel de O3 es dentro del rango aceptable, pero aún se pueden tomar medidas.";
            } else {
                // Nivel bajo
                contentText = "Nivel de CO2 normal.";
                additionalInfo = "El nivel de 03 está dentro del rango saludable.";
            }


             */

            if (co2Value > 500) {
                // Nivel crítico, sensor dañado o valor anormalç
                contentText = "¡Valor de 03 peligrosamente alto.\n";
                additionalInfo = "¡El nivel de 03 ha superado los 500! Esto indica que el sensor podría estar dañado o el valor es anormalmente alto.\n" +
                        "Por favor, revisa el sensor o las condiciones de medición.";
            } else if (co2Value >= 240 && co2Value <= 500) {
                // Nivel moderado
                contentText = "Nivel Extremo de ozono detectado.\n";
                additionalInfo = "La concentración de ozono está en un nivel extremo (entre 240 y 500 ppb). Se recomienda tomar medidas preventivas para proteger la salud.\n" +
                        "Fecha y hora: " + currentTime + ".\n" +
                        "Nivel de Ozono: " + co2Value + "PPM .\n" +
                        "Coordenadas GPS: " + gpsCoordinates + ".\n" +
                        "Precaución: Manténgase alejado de áreas con alta exposición al ozono, especialmente si es sensible a este contaminante.";
            } else if (co2Value >= 121 && co2Value <= 180) {
                // Nivel bajo pero no ideal
                contentText = "Nivel Moderado de ozono";
                additionalInfo = "El nivel de ozono está en un nivel moderado (entre 121 y 180 ppb).Aunque no se considera peligroso, se recomienda monitorear la calidad del aire para asegurar un ambiente saludable. \n" +
                        "Fecha y hora: " + currentTime + ".\n" +
                        "Nivel de Ozono: " + co2Value + ".\n" +
                        "Coordenadas GPS: " + gpsCoordinates + ".\n" +
                        "Recomendación: Mantener las ventanas cerradas y evitar actividades al aire libre durante este nivel.";
            } else {
                // Nivel bajo
                contentText = "Nivel de CO2 normal.";
                additionalInfo = "El nivel de O3 está dentro del rango saludable.\n" +
                        "Fecha y hora: " + currentTime + ".\n" +
                        "Nivel de Ozono: " + co2Value + ".\n" +
                        "Coordenadas GPS: " + gpsCoordinates + ".\n" +
                        "Recomendación: Mantener las condiciones de monitoreo actual y seguir con las actividades normales.";
            }
            // Crear la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_rounded_logo)  // Cambia esto por tu ícono
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            // Agregar sonido a la notificación
            Uri alarmSound = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM);
            builder.setSound(alarmSound);

            // Agregar información adicional
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(additionalInfo));

            // Mostrar la notificación
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            createNotificationChannel(context);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            // Si no se tiene el permiso, solicitarlo
            requestLocationPermission((Activity) context);
        }
    }


    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal O3";
            String description = "Canal de notificación para alertas de CO3";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    // Verificar si se tiene el permiso de localización
    public static boolean checkLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Solicitar el permiso de localización
    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION_CODE);  // Código para manejar la respuesta de la solicitud
    }

    // Obtener la hora actual en formato yyyy-MM-dd HH:mm:ss
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Obtener las coordenadas GPS del dispositivo
    public static String getGpsCoordinates(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Verificar si se tienen los permisos de localización
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tienen permisos, retornar coordenadas por defecto
            return "Coordenadas no disponibles";
        }

        // Intentar obtener la última localización conocida
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastKnownLocation != null) {
            // Obtener las coordenadas en formato String
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();
            return String.format("%f, %f", latitude, longitude);
        } else {
            // Si no se puede obtener la localización, devolver coordenadas por defecto
            return "Coordenadas no disponibles";
        }
    }

    // Manejar la respuesta de la solicitud de permisos (en la Activity que llama a la función)
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Activity activity) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, se puede proceder con la notificación
                showCO2AlertNotification(activity, 500); // Aquí puedes pasar el valor de CO2 real
            } else {
                // Permiso denegado, mostrar mensaje
                Toast.makeText(activity, "Permiso de localización denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
