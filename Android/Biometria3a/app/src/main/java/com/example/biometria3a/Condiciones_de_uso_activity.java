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
        // Encontrar el icono del menú en el Toolbar
        menuIcon = findViewById(R.id.menu_icon);

        // Establecer el listener para abrir el PopupMenu al hacer clic en el icono
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    // Método para mostrar el PopupMenu
    // Método para mostrar el PopupMenu
    private void showPopupMenu(View view) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(Condiciones_de_uso_activity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        // Manejar las acciones del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Usar if-else en lugar de switch para evitar el error
                if (item.getItemId() == R.id.action_about) {
                    Toast.makeText(Condiciones_de_uso_activity.this, "Sobre Nosotros", Toast.LENGTH_SHORT).show();
                    lanzarSobreNosotros();
                    return true;
                } else if (item.getItemId() == R.id.action_faq) {
                    Toast.makeText(Condiciones_de_uso_activity.this, "FAQ", Toast.LENGTH_SHORT).show();
                    lanzarFAQ();
                    return true;
                } else if (item.getItemId() == R.id.action_packs) {
                    Toast.makeText(Condiciones_de_uso_activity.this, "Packs", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.action_privacidad) {
                    Toast.makeText(Condiciones_de_uso_activity.this, "Action Privaciodad", Toast.LENGTH_SHORT).show();
                    lanzarPrivacidad();
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Mostrar el menú
        popupMenu.show();
    }

    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void lanzarSobreNosotros() {
        Intent intent = new Intent(this, SobreNosotrosActivity.class);
        startActivity(intent);
    }

    private void lanzarFAQ() {
        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    private void lanzarPacks() {
        // Acción o navegación para la opción de "Packs"
        Intent intent = new Intent(this, PacksActivity.class);
        startActivity(intent);
    }


    private void lanzarPrivacidad() {
        // Acción o navegación para la opción de "Packs"
        Intent intent = new Intent(this, PrivacidadAcitivity.class);
        startActivity(intent);
    }
}
// --------------------------------------------------------------
// --------------------------------------------------------------
