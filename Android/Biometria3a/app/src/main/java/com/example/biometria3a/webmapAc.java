package com.example.biometria3a;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 import android.widget.ImageView;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;


public class webmapAc extends AppCompatActivity {
    private MenuHandler menuHandler;
    private ImageView menuIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webmap);

        WebView webView = findViewById(R.id.webView);

        // Configuración del WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Habilitar JavaScript si es necesario

        // Para que el WebView cargue el contenido dentro de la app en lugar de abrir un navegador
        webView.setWebViewClient(new WebViewClient());


        // Configurar el Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView userIcon = findViewById(R.id.user_icon);

        // Configurar OnClickListener para el ícono de usuario
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir EditProfileActivity
                Intent intent = new Intent(webmapAc.this, EditPerfilActivity.class);
                startActivity(intent);
            }
        });
        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(webmapAc.this, MainActivity .class);
                startActivity(intent);
            }
        });

        // Cargar la URL de tu sitio web
        webView.loadUrl("file:///android_asset/heatmap_acci.html");
        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);

    }
}

