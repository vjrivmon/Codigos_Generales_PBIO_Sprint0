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
        PopupMenu popupMenu = new PopupMenu(SobreNosotrosActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        // Manejar las acciones del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Usar if-else en lugar de switch para evitar el error
                if (item.getItemId() == R.id.action_about) {
                    Toast.makeText(SobreNosotrosActivity.this, "Sobre Nosotros", Toast.LENGTH_SHORT).show();
                    lanzarSobreNosotros();
                    return true;
                } else if (item.getItemId() == R.id.action_faq) {
                    Toast.makeText(SobreNosotrosActivity.this, "FAQ", Toast.LENGTH_SHORT).show();
                    lanzarFAQ();
                    return true;
                } else if (item.getItemId() == R.id.action_packs) {
                    Toast.makeText(SobreNosotrosActivity.this, "Packs", Toast.LENGTH_SHORT).show();
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
    // --------------------------------------------------------------
    // --------------------------------------------------------------

}
