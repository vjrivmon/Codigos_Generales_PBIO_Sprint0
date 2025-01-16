package com.example.biometria3a;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import android.location.Location;
/*
public class DistanceCalculatorActivity extends AppCompatActivity {

    private LocationTracker locationTracker;
    private TextView distanceTextView;
    private Button startButton, stopButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_calculator);

        distanceTextView = findViewById(R.id.distanceTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        locationTracker = new LocationTracker(this);

        startButton.setOnClickListener(v -> {
            locationTracker.startTracking();
        });

        stopButton.setOnClickListener(v -> {
            locationTracker.stopTracking();
            updateDistance();
        });
    }

    private void updateDistance() {
        float distance = locationTracker.getTotalDistance();
        distanceTextView.setText(String.format("Distancia recorrida: %.2f metros", distance));
    }
}


 */


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class DistanceCalculatorActivity extends AppCompatActivity {

    /*private TextView distanceTextView;

    // Coordenadas del punto de inicio (Paseig de la Universitat 8)
    private static final double LAT_START = 38.9943;
    private static final double LON_START = -0.1642;

    // Coordenadas del punto de destino (UPV Campus Gandia)
    private static final double LAT_END = 38.9954;
    private static final double LON_END = -0.1662;


     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private Location startLocation, endLocation;
    private TextView distanceTextView;
    private Button startButton, stopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_calculator);

        distanceTextView = findViewById(R.id.distanceTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Configurar los botones
        startButton.setOnClickListener(v -> startTracking());
        stopButton.setOnClickListener(v -> stopTracking());
        // Calcular la distancia entre las dos ubicaciones
       /* double distance = calculateDistance(LAT_START, LON_START, LAT_END, LON_END);

        // Mostrar la distancia en el TextView
        distanceTextView.setText(String.format("Distancia recorrida: %.2f metros", distance));

        */
    }

    /**
     * Calcula la distancia en metros entre dos ubicaciones usando la fórmula de Haversine.
     * @param latStart Latitud del punto de inicio
     * @param lonStart Longitud del punto de inicio
     * @param latEnd Latitud del punto de destino
     * @param lonEnd Longitud del punto de destino
     * @return Distancia en metros
     */
    public double calculateDistance(double latStart, double lonStart, double latEnd, double lonEnd) {
        final int EARTH_RADIUS = 6371000; // Radio de la Tierra en metros

        // Convertir las coordenadas a radianes
        double latStartRad = Math.toRadians(latStart);
        double lonStartRad = Math.toRadians(lonStart);
        double latEndRad = Math.toRadians(latEnd);
        double lonEndRad = Math.toRadians(lonEnd);

        // Diferencias de latitudes y longitudes
        double deltaLat = latEndRad - latStartRad;
        double deltaLon = lonEndRad - lonStartRad;

        // Aplicar la fórmula de Haversine
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latStartRad) * Math.cos(latEndRad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia en metros
        return EARTH_RADIUS * c;
    }
    // Método para iniciar el seguimiento (obtiene la ubicación inicial)
    // Método para iniciar el seguimiento (obtiene la ubicación inicial)
    private void startTracking() {
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    startLocation = location;
                    distanceTextView.setText("Ubicación inicial guardada.");
                } else {
                    distanceTextView.setText("No se pudo obtener la ubicación inicial.");
                }
            }).addOnFailureListener(e -> distanceTextView.setText("Error al obtener la ubicación inicial."));
        } else {
            requestPermissions();
        }
    }

    // Método para detener el seguimiento (obtiene la ubicación final y calcula la distancia)
    private void stopTracking() {
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    endLocation = location;
                    if (startLocation != null) {
                        // Calcular la distancia entre las ubicaciones
                        float distance = startLocation.distanceTo(endLocation);
                        distanceTextView.setText(String.format("Distancia recorrida: %.2f metros", distance));
                    } else {
                        distanceTextView.setText("No se ha registrado la ubicación inicial.");
                    }
                } else {
                    distanceTextView.setText("No se pudo obtener la ubicación final.");
                }
            }).addOnFailureListener(e -> distanceTextView.setText("Error al obtener la ubicación final."));
        } else {
            distanceTextView.setText("Permisos de ubicación no concedidos.");
            requestPermissions();
        }
    }

    // Verificar si los permisos están concedidos
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Solicitar permisos de ubicación
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Manejar la respuesta del usuario a los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                distanceTextView.setText("Permisos concedidos. Puedes iniciar el seguimiento.");
            } else {
                distanceTextView.setText("Permisos denegados. La aplicación no puede calcular la distancia.");
            }
        }
    }
}
