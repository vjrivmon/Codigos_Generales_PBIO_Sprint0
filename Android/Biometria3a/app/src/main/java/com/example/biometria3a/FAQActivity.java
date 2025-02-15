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
    private MenuHandler menuHandler;
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

            ImageView userIcon = findViewById(R.id.user_icon);

            // Configurar OnClickListener para el ícono de usuario
            userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear un Intent para abrir EditProfileActivity
                    Intent intent = new Intent(FAQActivity.this, EditPerfilActivity.class);
                    startActivity(intent);
                }
            });

            ImageView logo = findViewById(R.id.logo);
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Iniciar la actividad del mapa
                    Intent intent = new Intent(FAQActivity.this, Mapa_Activity .class);
                    startActivity(intent);
                }
            });

            menuHandler = new MenuHandler(this);
            menuIcon = findViewById(R.id.menu_icon);
            menuIcon.setOnClickListener(menuHandler::showPopupMenu);

        }


    // Inflar el menú cuando se crea la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    // --------------------------------------------------------------
    // ---------------------------------
    }


