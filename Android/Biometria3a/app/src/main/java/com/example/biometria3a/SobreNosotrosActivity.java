package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SobreNosotrosActivity extends AppCompatActivity {


    private ImageView menuIcon;
    private MenuHandler menuHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sobre_nosotros); // Asegúrate de que este es el nombre correcto de tu layout

        TextView valuesContent = findViewById(R.id.values_content);

        // Llenar el TextView con el contenido de los valores
        String valuesText = "• Innovación: Siempre buscamos nuevas formas de mejorar y ofrecer lo mejor a nuestros clientes.\n\n" +
                "• Calidad: Nos esforzamos en garantizar que cada uno de nuestros productos cumpla con los más altos estándares.\n\n" +
                "• Compromiso: Nos apasiona lo que hacemos y nos comprometemos a brindar lo mejor en cada paso.\n\n" +
                "• Trabajo en equipo: Creemos que juntos podemos lograr más, y la colaboración es fundamental en nuestra empresa.";

        valuesContent.setText(valuesText);

        // Configurar el Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
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
                Intent intent = new Intent(SobreNosotrosActivity.this, EditPerfilActivity.class);
                startActivity(intent);
            }
        });
        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(SobreNosotrosActivity.this, Mapa_Activity .class);
                startActivity(intent);
            }
        });

        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);
    }

    // Método para mostrar el PopupMenu

    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    // --------------------------------------------------------------
    // --------------------------------------------------------------

}
