package com.example.biometria3a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class OzoneLevelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Obtener el valor de CO2
        int co2Value = intent.getIntExtra("co2_value", 0);

        // Asegúrate de que el BroadcastReceiver esté recibiendo el mensaje
        Log.d("OzoneLevelReceiver", "Received O3 Value: " + co2Value);

        // Mostrar la notificación
        CO2NotificationManager.showCO2AlertNotification(context, co2Value);
    }
}


