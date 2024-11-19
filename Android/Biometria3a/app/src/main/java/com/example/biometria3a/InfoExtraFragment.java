package com.example.biometria3a;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;

import android.view.View;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



public class InfoExtraFragment extends AppCompatActivity {

    // O3 部分
    private TextView ozonoText, recomendacionesOzonoText, precaucionOzonoText;
    private TextView ozonoTitle, recomendacionesOzonoTitle, precaucionOzonoTitle;

    // NO2 部分
    private TextView no2Text, recomendacionesNo2Text, precaucionNo2Text;
    private TextView no2Title, recomendacionesNo2Title, precaucionNo2Title;

    // SO3 部分
    private TextView so3Text, recomendacionesSo3Text, precaucionSo3Text;
    private TextView so3Title, recomendacionesSo3Title, precaucionSo3Title;
    private ImageView menuIcon;
    private MenuHandler menuHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_extra); // Inflamos el layout de la actividad

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);
        // Inicializar los elementos del layout
        initViews();

        // Configurar el logo para que inicie una nueva actividad (Mapa_Activity)
        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoExtraFragment.this, Mapa_Activity.class);
                startActivity(intent);
            }
        });

        // Hacer "documento oficial" un enlace clickeable
        TextView enlaceDocumento = findViewById(R.id.enlace_documento_oficial);
        String text = "Para más información sobre la normativa y recomendaciones sobre la calidad del aire, consulta el siguiente documento oficial.";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://www.boe.es/buscar/doc.php?id=DOUE-L-2008-80787";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        };

        int startIndex = text.indexOf("documento oficial");
        int endIndex = startIndex + "documento oficial".length();
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        enlaceDocumento.setText(spannableString);
        enlaceDocumento.setMovementMethod(LinkMovementMethod.getInstance());

        // Configurar la visibilidad de las secciones (toggle)
        setupToggleVisibilityWithArrow(ozonoTitle, ozonoText);
        setupToggleVisibilityWithArrow(recomendacionesOzonoTitle, recomendacionesOzonoText);
        setupToggleVisibilityWithArrow(precaucionOzonoTitle, precaucionOzonoText);

        setupToggleVisibilityWithArrow(no2Title, no2Text);
        setupToggleVisibilityWithArrow(recomendacionesNo2Title, recomendacionesNo2Text);
        setupToggleVisibilityWithArrow(precaucionNo2Title, precaucionNo2Text);

        setupToggleVisibilityWithArrow(so3Title, so3Text);
        setupToggleVisibilityWithArrow(recomendacionesSo3Title, recomendacionesSo3Text);
        setupToggleVisibilityWithArrow(precaucionSo3Title, precaucionSo3Text);
    }

    private void initViews() {
        // Inicializar los TextViews para cada sección (O3, NO2, SO3)
        ozonoText = findViewById(R.id.ozono_text);
        recomendacionesOzonoText = findViewById(R.id.recomendaciones_ozono_text);
        precaucionOzonoText = findViewById(R.id.precaucion_ozono_text);
        ozonoTitle = findViewById(R.id.ozono_title);
        recomendacionesOzonoTitle = findViewById(R.id.recomendaciones_ozono_title);
        precaucionOzonoTitle = findViewById(R.id.precaucion_ozono_title);

        no2Text = findViewById(R.id.no2_text);
        recomendacionesNo2Text = findViewById(R.id.recomendaciones_no2_text);
        precaucionNo2Text = findViewById(R.id.precaucion_no2_text);
        no2Title = findViewById(R.id.no2_title);
        recomendacionesNo2Title = findViewById(R.id.recomendaciones_no2_title);
        precaucionNo2Title = findViewById(R.id.precaucion_no2_title);

        so3Text = findViewById(R.id.so3_text);
        recomendacionesSo3Text = findViewById(R.id.recomendaciones_so3_text);
        precaucionSo3Text = findViewById(R.id.precaucion_so3_text);
        so3Title = findViewById(R.id.so3_title);
        recomendacionesSo3Title = findViewById(R.id.recomendaciones_so3_title);
        precaucionSo3Title = findViewById(R.id.precaucion_so3_title);
    }

    private void setupToggleVisibilityWithArrow(final TextView titleView, final TextView contentView) {
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentView.getVisibility() == View.GONE) {
                    contentView.setVisibility(View.VISIBLE);
                    titleView.setText(titleView.getText().toString().replace("▼", "▲"));
                } else {
                    contentView.setVisibility(View.GONE);
                    titleView.setText(titleView.getText().toString().replace("▲", "▼"));
                }
            }
        });
    }




}

