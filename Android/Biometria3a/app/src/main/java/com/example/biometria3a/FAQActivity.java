package com.example.biometria3a;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FAQActivity  extends AppCompatActivity {

        private TextView answer1, answer2, answer3, answer4, answer5, answer6, answer7;
    private ImageView menuIcon;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_faq);

            // Find views by ID
            answer1 = findViewById(R.id.answer1);
            answer2 = findViewById(R.id.answer2);
            answer3 = findViewById(R.id.answer3);
            answer4 = findViewById(R.id.answer4);
            answer5 = findViewById(R.id.answer5);
            answer6 = findViewById(R.id.answer6);
            answer7 = findViewById(R.id.answer7);
            Button sensorPageButton = findViewById(R.id.sensor_page_button);

            // Initially hide all answers
            answer1.setVisibility(View.GONE);
            answer2.setVisibility(View.GONE);
            answer3.setVisibility(View.GONE);
            answer4.setVisibility(View.GONE);
            answer5.setVisibility(View.GONE);
            answer6.setVisibility(View.GONE);
            answer7.setVisibility(View.GONE);

            // Set OnClickListener for sensor page button
            sensorPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click to open a webpage
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://secure.www.apple.com.cn/shop/order/list"));
                    startActivity(browserIntent);
                }
            });

            // Toggle visibility for question 1
            TextView question1 = findViewById(R.id.question1);
            question1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer1.getVisibility() == View.GONE) {
                        answer1.setVisibility(View.VISIBLE);
                    } else {
                        answer1.setVisibility(View.GONE);
                    }
                }
            });

            // Toggle visibility for question 2
            TextView question2 = findViewById(R.id.question2);
            question2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer2.getVisibility() == View.GONE) {
                        answer2.setVisibility(View.VISIBLE);
                    } else {
                        answer2.setVisibility(View.GONE);
                    }
                }
            });

            // Toggle visibility for question 3
            TextView question3 = findViewById(R.id.question3);
            question3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer3.getVisibility() == View.GONE) {
                        answer3.setVisibility(View.VISIBLE);
                    } else {
                        answer3.setVisibility(View.GONE);
                    }
                }
            });

            // Toggle visibility for question 4
            TextView question4 = findViewById(R.id.question4);
            question4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer4.getVisibility() == View.GONE) {
                        answer4.setVisibility(View.VISIBLE);
                    } else {
                        answer4.setVisibility(View.GONE);
                    }
                }
            });

            // Toggle visibility for question 5
            TextView question5 = findViewById(R.id.question5);
            question5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer5.getVisibility() == View.GONE) {
                        answer5.setVisibility(View.VISIBLE);
                    } else {
                        answer5.setVisibility(View.GONE);
                    }
                }
            });

            // Toggle visibility for question 6
            TextView question6 = findViewById(R.id.question6);
            question6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer6.getVisibility() == View.GONE) {
                        answer6.setVisibility(View.VISIBLE);
                    } else {
                        answer6.setVisibility(View.GONE);
                    }
                }
            });

            // Toggle visibility for question 7
            TextView question7 = findViewById(R.id.question7);
            question7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer7.getVisibility() == View.GONE) {
                        answer7.setVisibility(View.VISIBLE);
                    } else {
                        answer7.setVisibility(View.GONE);
                    }
                }
            });




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
    private void showPopupMenu(View view) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(FAQActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        // Manejar las acciones del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Usar if-else en lugar de switch para evitar el error
                if (item.getItemId() == R.id.action_about) {
                    Toast.makeText(FAQActivity.this, "Sobre Nosotros", Toast.LENGTH_SHORT).show();
                    lanzarSobreNosotros();
                    return true;
                } else if (item.getItemId() == R.id.action_faq) {
                    Toast.makeText(FAQActivity.this, "FAQ", Toast.LENGTH_SHORT).show();
                    lanzarFAQ();
                    return true;
                } else if (item.getItemId() == R.id.action_packs) {
                    Toast.makeText(FAQActivity.this, "Packs", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.action_privacidad) {
                    Toast.makeText(FAQActivity.this, "Action Privaciodad", Toast.LENGTH_SHORT).show();
                    lanzarPrivacidad();
                    return true;
                }else {
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
    // --------------------------------------------------------------
    // ---------------------------------
    }


