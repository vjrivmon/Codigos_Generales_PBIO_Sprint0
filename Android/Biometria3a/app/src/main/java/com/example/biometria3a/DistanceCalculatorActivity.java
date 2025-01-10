package com.example.biometria3a;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
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

public class DistanceCalculatorActivity extends AppCompatActivity {

    private TextView distanceTextView;

    // Coordenadas del punto de inicio (Paseig de la Universitat 8)
    private static final double LAT_START = 38.9943;
    private static final double LON_START = -0.1642;

    // Coordenadas del punto de destino (UPV Campus Gandia)
    private static final double LAT_END = 38.9954;
    private static final double LON_END = -0.1662;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_calculator);

        distanceTextView = findViewById(R.id.distanceTextView);

        // Calcular la distancia entre las dos ubicaciones
        double distance = calculateDistance(LAT_START, LON_START, LAT_END, LON_END);

        // Mostrar la distancia en el TextView
        distanceTextView.setText(String.format("Distancia recorrida: %.2f metros", distance));
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
}
