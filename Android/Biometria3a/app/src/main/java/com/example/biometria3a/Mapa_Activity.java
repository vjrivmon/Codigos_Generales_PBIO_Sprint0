package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Mapa_Activity extends AppCompatActivity {

    private TextView tvCurrentTime, tvTemperature, tvOzone;
    private WebView webView;
    private Button btnUpdate;
    private ImageView menuIcon;
    private NotificationHalper notificationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvOzone = findViewById(R.id.tv_ozone);
        btnUpdate = findViewById(R.id.btn_update);
        webView = findViewById(R.id.webview_map);

        // WebView设置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用JavaScript
        webView.setWebViewClient(new WebViewClient()); // 在WebView中打开URL

        // 加载Google Maps的普通URL，显示瓦伦西亚的位置
        String googleMapsUrl = "https://www.google.com/maps?q=Valencia&z=12";
        webView.loadUrl(googleMapsUrl);

        // 显示当前时间
        updateTime();

        // 更新按钮点击事件
        btnUpdate.setOnClickListener(v -> {
            // 更新传感器数据的逻辑
            tvTemperature.setText("Temperatura: 26°C"); // 假设更新后的温度
            tvOzone.setText("Ozono: 105 ppm"); // 假设更新后的Ozono数据
            updateTime(); // 更新时间
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
                Intent intent = new Intent(Mapa_Activity.this, EditPerfilActivity.class);
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

        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(Mapa_Activity.this, Mapa_Activity .class);
                startActivity(intent);
            }
        });



        // Llama a la notificación al iniciar la actividad
        //NotificationHalper notificationHelper = new NotificationHalper(this);
       // notificationHelper.showNotification("Aviso", "Senosr no encontrado");
    }

    // 更新显示时间
    private void updateTime() {
        String currentTime = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm:ss", new Locale("es", "ES")).format(new Date());
        tvCurrentTime.setText("Hora actual: " + currentTime);
    }



    // Método para mostrar el PopupMenu
    // Método para mostrar el PopupMenu
    private void showPopupMenu(View view) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(Mapa_Activity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        // Manejar las acciones del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Usar if-else en lugar de switch para evitar el error
                if (item.getItemId() == R.id.action_about) {
                    Toast.makeText(Mapa_Activity.this, "Sobre Nosotros", Toast.LENGTH_SHORT).show();
                    lanzarSobreNosotros();
                    return true;
                } else if (item.getItemId() == R.id.action_faq) {
                    Toast.makeText(Mapa_Activity.this, "FAQ", Toast.LENGTH_SHORT).show();
                    lanzarFAQ();
                    return true;
                } else if (item.getItemId() == R.id.action_packs) {
                    Toast.makeText(Mapa_Activity.this, "Packs", Toast.LENGTH_SHORT).show();
                    lanzarPacks();
                    return true;
                } else if (item.getItemId() == R.id.action_privacidad) {
                    Toast.makeText(Mapa_Activity.this, "Action Privaciodad", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void lanzarPrivacidad() {
        // Acción o navegación para la opción de "Packs"
        Intent intent = new Intent(this, PrivacidadAcitivity.class);
        startActivity(intent);
    }
    // --------------------------------------------------------------
    // --------------------------------------------------------------

}