package com.example.biometria3a;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // 注意导入 Log
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;   // 用于坐标合并
import java.util.List;
import java.util.Map;

/**
 * HeatmapActivity: 显示从后端获取的 NO2 / SO3 / O3 / Temperatura 数据
 * 并在地图上切换图层 (热力图 + 圆形标记)
 *
 * 这里使用“方案 C”：对单条解析出错的数据直接跳过，而不是整体中断。
 */
public class HeatmapActivity extends AppCompatActivity implements OnMapReadyCallback {
    String ip = Config.SERVER_IP;
    private static final String TAG = "DEBUG_HMAP"; // 方便在 Logcat 中搜索

    // Google Map 实例
    private GoogleMap mMap;

    // 热力图相关
    private HeatmapTileProvider no2Provider, so2Provider, o3Provider, tempProvider;
    private TileOverlay no2Overlay, so2Overlay, o3Overlay, tempOverlay;

    // Spinner 组件
    private Spinner spinnerLayers;

    // 数据列表
    private final List<DataPoint> dataPoints = new ArrayList<>();

    // 圆形标记
    private final List<Circle> circleMarkers = new ArrayList<>();

    // Volley 请求队列
    private RequestQueue requestQueue;
    private ImageView menuIcon;
    private MenuHandler menuHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        // 绑定 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ImageView userIcon = findViewById(R.id.user_icon);

        // Configurar OnClickListener para el ícono de usuario
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir EditProfileActivity
                Intent intent = new Intent(HeatmapActivity.this, EditPerfilActivity.class);
                startActivity(intent);
            }
        });
        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(HeatmapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 初始化 Volley
        requestQueue = Volley.newRequestQueue(this);

        // 取得地图 Fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "mapFragment is null!");
        }

        // 绑定 Spinner
        spinnerLayers = findViewById(R.id.spinner_layers);
        // 假设你在 strings2.xml（或者 strings.xml） 中有 string-array = map_layers
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.map_layers,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLayers.setAdapter(adapter);

        // 监听 Spinner
        spinnerLayers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parentView,
                    View selectedItemView,
                    int position,
                    long id
            ) {
                String selectedLayer = (String) parentView.getItemAtPosition(position);
                Log.d(TAG, "Spinner selected: " + selectedLayer);
                switchLayer(selectedLayer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });
    }


    /**
     * GoogleMap 准备好时会调用此方法
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady() - Map is ready");
        mMap = googleMap;

        // 移动摄像头到指定位置(39.003, -0.162)，缩放等级14
        LatLng startPos = new LatLng(39.003, -0.162);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPos, 14f));

        // 从后端获取数据
       // Log.d(TAG, "Fetching data from: http://192.168.18.157:8080/");
        fetchDataFromAPI(ip);
    }

    /**
     * 发起 Volley 请求
     */
    private void fetchDataFromAPI(String url) {
        Log.d(TAG, "fetchDataFromAPI() start, url=" + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse() success, length=" + response.length());
                        // 打印前面几条示例
                        for (int i = 0; i < Math.min(response.length(), 10); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Log.d(TAG, "JSON item " + i + ": " + obj.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        parseJsonResponse(response);
                        if (dataPoints.isEmpty()) {
                            Log.w(TAG, "No dataPoints after parsing!");
                            Toast.makeText(HeatmapActivity.this,
                                    "Data is empty after parsing!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Parsed dataPoints size=" + dataPoints.size());
                            createHeatMapLayers();
                            showAllOverlays();
                            addCircleMarkers(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 如果请求失败
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                        Toast.makeText(HeatmapActivity.this,
                                "Error fetching data: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(request);
        Log.d(TAG, "fetchDataFromAPI() end - request added to queue");
    }

    /**
     * 解析后端返回的 JSON
     * 并合并同坐标 (lat, lng) 下的多种气体
     * 如果单条数据解析异常，则跳过该条，不中断整个循环。
     */
    private void parseJsonResponse(JSONArray jsonArray) {
        Log.d(TAG, "parseJsonResponse() start");
        Map<String, DataPoint> mergeMap = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);

                JSONObject ubic = obj.getJSONObject("ubicacion");
                double lat = ubic.optDouble("latitud", 39.0);
                double lng = ubic.optDouble("longitud", -0.16);
                String tipo = obj.optString("tipo_medicion", "");
                double valor = obj.optDouble("valor", 0);

                Log.d(TAG, "Item " + i + ": lat=" + lat + ", lng=" + lng +
                        ", tipo=" + tipo + ", valor=" + valor);

                // 生成 Key
                String key = lat + ":" + lng;
                if (!mergeMap.containsKey(key)) {
                    mergeMap.put(key, new DataPoint(lat, lng));
                }
                DataPoint dp = mergeMap.get(key);

                // 映射：SO3 -> dp.so2, NO2 -> dp.no2, O3 -> dp.o3, Temperatura -> dp.temp
                if (tipo.equalsIgnoreCase("SO3")) {
                    dp.so2 = (int) Math.round(valor);
                } else if (tipo.equalsIgnoreCase("NO2")) {
                    dp.no2 = (int) Math.round(valor);
                } else if (tipo.equalsIgnoreCase("O3")) {
                    dp.o3 = (int) Math.round(valor);
                } else if (tipo.equalsIgnoreCase("Temperatura")) {
                    dp.temp = (int) Math.round(valor);
                }

            } catch (JSONException ex) {
                // *方案C*：单条数据出错，则跳过
                Log.e(TAG, "Skipping invalid item i=" + i + ", reason=" + ex);
                continue;
            }
        }

        dataPoints.clear();
        dataPoints.addAll(mergeMap.values());
        Log.d(TAG, "parseJsonResponse() end, dataPoints.size=" + dataPoints.size());
    }

    /**
     * 创建热力图图层
     */
    private void createHeatMapLayers() {
        Log.d(TAG, "createHeatMapLayers() called");

        no2Provider = new HeatmapTileProvider.Builder()
                .weightedData(getWeightedLatLngList("NO2"))
                .radius(50)
                .gradient(createGradient(Color.GREEN, Color.YELLOW, Color.RED))
                .build();
        no2Overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(no2Provider));

        so2Provider = new HeatmapTileProvider.Builder()
                .weightedData(getWeightedLatLngList("SO2"))
                .radius(50)
                .gradient(createGradient(Color.GREEN, Color.YELLOW, Color.RED))
                .build();
        so2Overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(so2Provider));

        o3Provider = new HeatmapTileProvider.Builder()
                .weightedData(getWeightedLatLngList("O3"))
                .radius(50)
                .gradient(createGradient(Color.GREEN, Color.YELLOW, Color.RED))
                .build();
        o3Overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(o3Provider));

        tempProvider = new HeatmapTileProvider.Builder()
                .weightedData(getWeightedLatLngList("TEMP"))
                .radius(50)
                .gradient(createGradient(Color.GREEN, Color.YELLOW, Color.RED))
                .build();
        tempOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tempProvider));
    }

    /**
     * 构造 WeightedLatLng 列表
     */
    private List<com.google.maps.android.heatmaps.WeightedLatLng> getWeightedLatLngList(String filter) {
        Log.d(TAG, "getWeightedLatLngList('" + filter + "')");
        List<com.google.maps.android.heatmaps.WeightedLatLng> result = new ArrayList<>();

        for (DataPoint dp : dataPoints) {
            double value, maxValue;
            switch (filter) {
                case "NO2":
                    value = dp.no2;
                    maxValue = 220.0;
                    break;
                case "SO2":
                    value = dp.so2;
                    maxValue = 380.0;
                    break;
                case "O3":
                    value = dp.o3;
                    maxValue = 260.0;
                    break;
                case "TEMP":
                    // 让温度值 (大概区间 10~35) 做归一化
                    value = dp.temp + 10;
                    maxValue = 25.0;
                    break;
                default:
                    value = 0;
                    maxValue = 1;
                    break;
            }

            double intensity = value / maxValue;
            result.add(new com.google.maps.android.heatmaps.WeightedLatLng(
                    new LatLng(dp.lat, dp.lng),
                    intensity));
        }
        Log.d(TAG, "getWeightedLatLngList('" + filter + "'), result size=" + result.size());
        return result;
    }

    /**
     * 创建热力图渲染所需的渐变色
     */
    private Gradient createGradient(int colorStart, int colorMid, int colorEnd) {
        Log.d(TAG, "createGradient()");
        int[] colors = {colorStart, colorMid, colorEnd};
        float[] startPoints = {0.4f, 0.75f, 1.4f};
        return new Gradient(colors, startPoints);
    }

    /**
     * 添加圆形标记
     */
    private void addCircleMarkers(String filter) {
        Log.d(TAG, "addCircleMarkers('" + filter + "') start, dataPoints.size=" + dataPoints.size());

        // 移除之前的标记
        for (Circle c : circleMarkers) {
            c.remove();
        }
        circleMarkers.clear();

        for (DataPoint dp : dataPoints) {
            String popupText;
            int color;

            if ("NO2".equals(filter)) {
                popupText = "NO2: " + dp.no2 + " µg/m³ (" + getQualityLevel("NO2", dp.no2) + ")";
                color = getNO2Color(dp.no2);
            }
            else if ("SO2".equals(filter)) {
                popupText = "SO3: " + dp.so2 + " µg/m³ (" + getQualityLevel("SO2", dp.so2) + ")";
                color = getSO2Color(dp.so2);
            }
            else if ("O3".equals(filter)) {
                popupText = "O3: " + dp.o3 + " µg/m³ (" + getQualityLevel("O3", dp.o3) + ")";
                color = getO3Color(dp.o3);
            }
            else if ("TEMP".equals(filter)) {
                popupText = "Temp: " + dp.temp + " °C (" + getQualityLevel("TEMP", dp.temp) + ")";
                color = getTempColor(dp.temp);
            }
            else {
                popupText = "NO2: " + dp.no2 + " (" + getQualityLevel("NO2", dp.no2) + ")\n" +
                        "SO3: " + dp.so2 + " (" + getQualityLevel("SO2", dp.so2) + ")\n" +
                        "O3:  " + dp.o3 + " (" + getQualityLevel("O3", dp.o3) + ")\n" +
                        "Temp:" + dp.temp + " (" + getQualityLevel("TEMP", dp.temp) + ")";
                color = getMaxColor(dp);
            }

            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(dp.lat, dp.lng))
                    .radius(10)
                    .strokeColor(color)
                    .fillColor(adjustAlpha(color, 0.4f))
                    .clickable(true)
            );
            circle.setTag(popupText);
            circleMarkers.add(circle);
        }

        // 点击圆形时弹出 Toast
        mMap.setOnCircleClickListener(circle -> {
            Object tag = circle.getTag();
            if (tag != null) {
                Toast.makeText(HeatmapActivity.this, tag.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "addCircleMarkers('" + filter + "') end, circleMarkers.size=" + circleMarkers.size());
    }

    /**
     * 根据 NO2 值获取颜色
     */
    private int getNO2Color(int value) {
        if (value > 200) return Color.RED;
        if (value > 40)  return Color.YELLOW;
        return Color.GREEN;
    }

    private int getSO2Color(int value) {
        if (value > 350) return Color.RED;
        if (value > 125) return Color.YELLOW;
        return Color.GREEN;
    }

    private int getO3Color(int value) {
        if (value > 240) return Color.RED;
        if (value > 120) return Color.YELLOW;
        return Color.GREEN;
    }

    private int getTempColor(int value) {
        // 例如：>35 => red; >25 => yellow; else green
        if (value > 23) return Color.RED;
        if (value > 15) return Color.YELLOW;
        return Color.GREEN;
    }

    private int getMaxColor(DataPoint dp) {
        boolean isRed = (dp.no2 > 200 || dp.so2 > 350 || dp.o3 > 180 || dp.temp > 35);
        boolean isYellow = !isRed && (dp.no2 > 40 || dp.so2 > 125 || dp.o3 > 120 || dp.temp > 25);
        if (isRed)  return Color.RED;
        if (isYellow) return Color.YELLOW;
        return Color.GREEN;
    }

    private String getQualityLevel(String gas, int value) {
        if ("NO2".equals(gas)) {
            return value > 200 ? "Malo" : (value > 40 ? "Moderado" : "Bueno");
        } else if ("SO2".equals(gas)) {
            return value > 350 ? "Malo" : (value > 125 ? "Moderado" : "Bueno");
        } else if ("O3".equals(gas)) {
            return value > 180 ? "Malo" : (value > 120 ? "Moderado" : "Bueno");
        } else if ("TEMP".equals(gas)) {
            return value > 35 ? "Malo" : (value > 25 ? "Moderado" : "Bueno");
        }
        return "Desconocido";
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red   = Color.red(color);
        int green = Color.green(color);
        int blue  = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 显示所有图层
     */
    private void showAllOverlays() {
        Log.d(TAG, "showAllOverlays() called");
        if (no2Overlay != null)  no2Overlay.setVisible(true);
        if (so2Overlay != null)  so2Overlay.setVisible(true);
        if (o3Overlay != null)   o3Overlay.setVisible(true);
        if (tempOverlay != null) tempOverlay.setVisible(true);

        addCircleMarkers(null);
    }

    /**
     * 切换图层
     */
    private void switchLayer(String selectedLayer) {
        Log.d(TAG, "switchLayer('" + selectedLayer + "') called");

        if (no2Overlay != null)  no2Overlay.setVisible(false);
        if (so2Overlay != null)  so2Overlay.setVisible(false);
        if (o3Overlay != null)   o3Overlay.setVisible(false);
        if (tempOverlay != null) tempOverlay.setVisible(false);

        if ("Mostrar NO2".equals(selectedLayer) && no2Overlay != null) {
            no2Overlay.setVisible(true);
            addCircleMarkers("NO2");
        } else if ("Mostrar SO2".equals(selectedLayer) && so2Overlay != null) {
            so2Overlay.setVisible(true);
            addCircleMarkers("SO2");
        } else if ("Mostrar O3".equals(selectedLayer) && o3Overlay != null) {
            o3Overlay.setVisible(true);
            addCircleMarkers("O3");
        } else if ("Mostrar Temp".equals(selectedLayer) && tempOverlay != null) {
            tempOverlay.setVisible(true);
            addCircleMarkers("TEMP");
        } else {
            showAllOverlays();
        }
    }

    /**
     * 内部类：代表同一坐标下的 4 种数值
     */
    private static class DataPoint {
        double lat;
        double lng;
        int no2;  // NO2
        int so2;  // SO3
        int o3;   // O3
        int temp; // Temperatura

        public DataPoint(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
            this.no2 = 0;
            this.so2 = 0;
            this.o3 = 0;
            this.temp = 0;
        }
    }
}