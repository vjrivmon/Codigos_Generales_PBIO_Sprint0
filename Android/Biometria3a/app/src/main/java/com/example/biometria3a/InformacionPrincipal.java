package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class InformacionPrincipal extends AppCompatActivity {

    private LinearLayout dotsLayout;  // Contenedor de los punto
    private ViewPager2 viewPager;     // ViewPager para los carruseles
    private CarouselAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacionprincipal);

        Button btnSkip = findViewById(R.id.btnSkip);

        // Configurar el botón Skip

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<ItemCarosel> carouselItems = new ArrayList<>();
        //carouselItems.add(new ItemCarosel(R.drawable.imagen1, "Tu aliado en la lucha contra la contaminación: el sensor monitorea la calidad del aire en tiempo real"));
        carouselItems.add(new ItemCarosel(R.drawable.imagen1, "Respira Tranquilo",
                "Monitorea la Calidad del Aire en Tiempo Real" ));
        carouselItems.add(new ItemCarosel(R.drawable.imagen2, "Conoce tu Entorno",
                "Mapa de Calidad del Aire" ));
        carouselItems.add(new ItemCarosel(R.drawable.imagen3, "Salud y Bienestar",
                "Consultar la calidad del aire protege tu salud y la de tu familia." ));
        carouselItems.add(new ItemCarosel(R.drawable.imagen4, "¿Tienes cuenta?", ""));

        CarouselAdapter adapter = new CarouselAdapter(carouselItems);
        viewPager.setAdapter(adapter);

        dotsLayout = findViewById(R.id.dotsLayout);
        // Configurar los puntos indicadores
        setupDots(carouselItems.size());

        // Cambiar puntos cuando se desliza entre imágenes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDots(position);
            }
        });

        btnSkip.setOnClickListener(v -> {
            // Mover al último elemento del carrusel
            viewPager.setCurrentItem(carouselItems.size() - 1, true);
        });

    }

    private void setupDots(int count) {
        for (int i = 0; i < count; i++) {
            View dot = LayoutInflater.from(this).inflate(R.layout.item_dot, dotsLayout, false);
            dotsLayout.addView(dot);
        }
        updateDots(0); // Iniciar con el primer punto activo
    }

    private void updateDots(int position) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            View dot = dotsLayout.getChildAt(i);
            if (i == position) {
                dot.setBackgroundColor(getResources().getColor(R.color.colorAccent)); // Punto activo (azul)
            } else {
                dot.setBackgroundColor(getResources().getColor(R.color.colorAzulclaro)); // Punto inactivo (gris)
            }
        }
    }
}
