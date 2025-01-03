package com.example.biometria3a;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuHandler {

    private final Context context;

    public MenuHandler(Context context) {
        this.context = context;
    }

    // Método para mostrar el PopupMenu
    public void showPopupMenu(View view) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        // Manejar las acciones del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleMenuAction(item);
                return true;
            }
        });

        // Mostrar el menú
        popupMenu.show();
    }

    // Método para manejar las acciones del menú
    private void handleMenuAction(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Toast.makeText(context, "Sobre Nosotros", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, SobreNosotrosActivity.class));
        } else if (item.getItemId() == R.id.action_faq) {
            Toast.makeText(context, "FAQ", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, FAQActivity.class));
        } else if (item.getItemId() == R.id.action_packs) {
            Toast.makeText(context, "Packs", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, MainActivity.class));
        } else if (item.getItemId() == R.id.action_privacidad) {
            Toast.makeText(context, "Privacidad", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, PrivacidadAcitivity.class));
        } else if (item.getItemId() == R.id.action_grafica) {
            Toast.makeText(context, "Grafica", Toast.LENGTH_SHORT).show();
           context.startActivity(new Intent(context, AirQualityActivity.class));
        } else if (item.getItemId() == R.id.info_extra) {
            Toast.makeText(context, "Info Extra", Toast.LENGTH_SHORT).show();
            // If the context is an instance of AppCompatActivity (or any Activity)
           context.startActivity(new Intent(context, InfoExtraFragment.class));
        }else if (item.getItemId() == R.id.qr) {
            Toast.makeText(context, "QR", Toast.LENGTH_SHORT).show();
            // If the context is an instance of AppCompatActivity (or any Activity)
            context.startActivity(new Intent(context, QrActivity.class));
        }
        else if (item.getItemId() == R.id.map) {
            Toast.makeText(context, "mapa", Toast.LENGTH_SHORT).show();
            // If the context is an instance of AppCompatActivity (or any Activity)
            context.startActivity(new Intent(context,webmapAc.class));
        }
    }

}
