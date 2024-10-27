package com.example.biometria3a;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Mapa_Activity extends AppCompatActivity {

    private TextView tvCurrentTime, tvTemperature, tvOzone;
    private WebView webView;
    private Button btnUpdate;

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
    }

    // 更新显示时间
    private void updateTime() {
        String currentTime = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm:ss", new Locale("es", "ES")).format(new Date());
        tvCurrentTime.setText("Hora actual: " + currentTime);
    }
}