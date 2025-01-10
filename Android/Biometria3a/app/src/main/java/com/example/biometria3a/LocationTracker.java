package com.example.biometria3a;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;

public class LocationTracker implements LocationListener {

    private final Context context;
    private LocationManager locationManager;
    private Location previousLocation;
    private float totalDistance = 0.0f;

    public LocationTracker(Context context) {
        this.context = context;
    }

    public void startTracking() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }

    public void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (previousLocation != null) {
            totalDistance += previousLocation.distanceTo(location); // Calcula la distancia entre dos ubicaciones
        }
        previousLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, android.os.Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}

