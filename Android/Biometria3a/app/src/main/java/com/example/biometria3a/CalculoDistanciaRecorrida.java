package com.example.biometria3a;
import android.location.Location;

public class CalculoDistanciaRecorrida {

    private Location lastLocation = null;
    private float totalDistance = 0f;

    // Método para calcular la distancia recorrida
    public float calcularDistancia(Location currentLocation) {
        // Si ya existe una ubicación previa, calcula la distancia
        if (lastLocation != null) {
            float distance = lastLocation.distanceTo(currentLocation);
            totalDistance += distance;
        }
        // Actualiza la última ubicación
        lastLocation = currentLocation;
        return totalDistance;  // Retorna la distancia total recorrida
    }
}
