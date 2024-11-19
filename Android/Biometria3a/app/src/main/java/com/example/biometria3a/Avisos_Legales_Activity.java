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

public class Avisos_Legales_Activity extends AppCompatActivity {


    private ImageView menuIcon;
    private MenuHandler menuHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos_legales);// Asegúrate de que este es el nombre correcto de tu layout


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
                Intent intent = new Intent(Avisos_Legales_Activity.this, EditPerfilActivity.class);
                startActivity(intent);
            }
        });
        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(Avisos_Legales_Activity.this, Mapa_Activity .class);
                startActivity(intent);
            }
        });
        // Encontrar el icono del menú en el Toolbar
        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);
    }



    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}
// --------------------------------------------------------------
// --------------------------------------------------------------
