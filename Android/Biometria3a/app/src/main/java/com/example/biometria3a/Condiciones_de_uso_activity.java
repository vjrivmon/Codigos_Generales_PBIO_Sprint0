package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Condiciones_de_uso_activity extends AppCompatActivity {


    private ImageView menuIcon;
    private MenuHandler menuHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiciones_de_uso);// Asegúrate de que este es el nombre correcto de tu layout


        // Configurar el Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView userIcon = findViewById(R.id.user_icon);

        // Configurar OnClickListener para el ícono de usuario
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir EditProfileActivity
                Intent intent = new Intent(Condiciones_de_uso_activity.this, EditPerfilActivity.class);
                startActivity(intent);
            }
        });
        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(Condiciones_de_uso_activity.this, Mapa_Activity .class);
                startActivity(intent);
            }
        });

        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);
    }

    // Método para mostrar el PopupMenu
    // Método para mostrar el PopupMenu


    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}
// --------------------------------------------------------------
// --------------------------------------------------------------
