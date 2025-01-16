package com.example.biometria3a;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AirQualityActivity extends AppCompatActivity {

    private BarChart barChart;
    private TextView airQualityTextView;
    private RequestQueue requestQueue;
    String ip = Config.SERVER_IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);

        barChart = findViewById(R.id.barChart);
        airQualityTextView = findViewById(R.id.airQualityTextView);

        // 初始化 Volley 请求队列
        requestQueue = Volley.newRequestQueue(this);

        // 初始化日期选择器
        Spinner dateSpinner = findViewById(R.id.dateSpinner);
        List<String> dates = new ArrayList<>();
        dates.add("2025-01-10");
        dates.add("2025-01-11");
        dates.add("2025-01-12");
        dates.add("2025-01-13");
        dates.add("2025-01-14");
        dates.add("2025-01-15");
        dates.add("2025-01-16");
        dates.add("2025-01-17");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);

        // 监听日期选择
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = dates.get(position);
                fetchDataFromAPI(ip, selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 默认加载第一个日期的数据
                fetchDataFromAPI(ip, dates.get(0));
            }
        });

        // 默认加载第一个日期的数据
        fetchDataFromAPI(ip, dates.get(0));
    }

    private void fetchDataFromAPI(String url, String selectedDate) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Medicion2> mediciones = parseJsonResponse(response, selectedDate);
                        showBarChart(mediciones);
                        updateAirQuality(mediciones);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AirQualityActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }


    private List<Medicion2> parseJsonResponse(JSONArray response, String selectedDate) {
        List<Medicion2> mediciones = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                String fechaHora = obj.getString("fecha_hora");

                // 检查日期是否匹配
                if (fechaHora.startsWith(selectedDate)) {
                    int id = obj.getInt("id_medicion");
                    String tipo = obj.optString("tipo_medicion", ""); // 使用 optString 防止字段缺失
                    double valor = obj.optDouble("valor", -1); // 使用 optDouble 并默认值为 -1

                    // 过滤无效数据
                    if (!tipo.isEmpty() && valor >= 0) {
                        mediciones.add(new Medicion2(id, fechaHora, tipo, valor));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
        }
        return mediciones;
    }


    private void showBarChart(List<Medicion2> mediciones) {
        if (mediciones.isEmpty()) {
            Toast.makeText(this, "No data available to display", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<BarEntry> entriesO3 = new ArrayList<>();
        ArrayList<BarEntry> entriesNO2 = new ArrayList<>();
        ArrayList<BarEntry> entriesSO3 = new ArrayList<>();
        ArrayList<BarEntry> entriesTemp = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();

        for (int i = 0; i < mediciones.size(); i++) {
            Medicion2 medicion = mediciones.get(i);

            // 提取时间部分，仅保留 HH:mm
            String fullDateTime = medicion.getFecha_hora();
            String timeOnly = fullDateTime.substring(11, 16);
            xLabels.add(timeOnly);

            switch (medicion.getTipo()) {
                case "O3":
                    entriesO3.add(new BarEntry(i, (float) medicion.getValor()));
                    break;
                case "NO2":
                    entriesNO2.add(new BarEntry(i, (float) medicion.getValor()));
                    break;
                case "SO3":
                    entriesSO3.add(new BarEntry(i, (float) medicion.getValor()));
                    break;
                case "Temperatura":
                    entriesTemp.add(new BarEntry(i, (float) medicion.getValor()));
                    break;
            }
        }

        // 打印调试数据
        System.out.println("Entries O3: " + entriesO3);
        System.out.println("Entries NO2: " + entriesNO2);
        System.out.println("Entries SO3: " + entriesSO3);
        System.out.println("Entries Temp: " + entriesTemp);

        BarDataSet dataSetO3 = new BarDataSet(entriesO3, "O3 (µg/m³)");
        dataSetO3.setColor(Color.BLUE);

        BarDataSet dataSetNO2 = new BarDataSet(entriesNO2, "NO2 (µg/m³)");
        dataSetNO2.setColor(Color.GREEN);

        BarDataSet dataSetSO3 = new BarDataSet(entriesSO3, "SO3 (µg/m³)");
        dataSetSO3.setColor(Color.YELLOW);

        BarDataSet dataSetTemp = new BarDataSet(entriesTemp, "Temperatura (°C)");
        dataSetTemp.setColor(Color.RED);

        BarData barData = new BarData(dataSetO3, dataSetNO2, dataSetSO3, dataSetTemp);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }


    private void updateAirQuality(List<Medicion2> mediciones) {
        float totalO3 = 0, totalNO2 = 0, totalSO3 = 0, totalTemp = 0;
        int countO3 = 0, countNO2 = 0, countSO3 = 0, countTemp = 0;

        for (Medicion2 medicion : mediciones) {
            switch (medicion.getTipo()) {
                case "O3":
                    totalO3 += medicion.getValor();
                    countO3++;
                    break;
                case "NO2":
                    totalNO2 += medicion.getValor();
                    countNO2++;
                    break;
                case "SO3":
                    totalSO3 += medicion.getValor();
                    countSO3++;
                    break;
                case "Temperatura":
                    totalTemp += medicion.getValor();
                    countTemp++;
                    break;
            }
        }

        float promedioO3 = countO3 > 0 ? totalO3 / countO3 : 0;
        float promedioNO2 = countNO2 > 0 ? totalNO2 / countNO2 : 0;
        float promedioSO3 = countSO3 > 0 ? totalSO3 / countSO3 : 0;
        float promedioTemp = countTemp > 0 ? totalTemp / countTemp : 0;

        // 获取检测等级
        String calidadO3 = evaluarCalidad(promedioO3, "O3");
        String calidadNO2 = evaluarCalidad(promedioNO2, "NO2");
        String calidadSO3 = evaluarCalidad(promedioSO3, "SO3");
        String calidadTemp = evaluarCalidad(promedioTemp, "Temperatura");


        int backgroundColor;
        if (calidadO3.equals("malo") || calidadNO2.equals("malo") || calidadSO3.equals("malo") || calidadTemp.equals("malo")) {
            backgroundColor = Color.RED;
        } else if (calidadO3.equals("moderado") || calidadNO2.equals("moderado") || calidadSO3.equals("moderado") || calidadTemp.equals("moderado")) {
            backgroundColor = Color.YELLOW;
        } else {
            backgroundColor = Color.GREEN;
        }
        // 更新空气质量文本信息
        String airQualityText = String.format(
                "Promedio O3: %.2f µg/m³ (%s)\nPromedio NO2: %.2f µg/m³ (%s)\nPromedio SO3: %.2f µg/m³ (%s)\nPromedio Temperatura: %.2f °C (%s)",
                promedioO3, calidadO3,
                promedioNO2, calidadNO2,
                promedioSO3, calidadSO3,
                promedioTemp, calidadTemp
        );
// 设置文本和背景颜色
        airQualityTextView.setText(airQualityText);
        airQualityTextView.setBackgroundColor(backgroundColor);
    }
    private String evaluarCalidad(float promedio, String tipo) {
        switch (tipo) {
            case "O3":
                if (promedio <= 120) return "bueno";
                else if (promedio <= 180) return "moderado";
                else return "malo";

            case "NO2":
                if (promedio <= 40) return "bueno";
                else if (promedio <= 200) return "moderado";
                else return "malo";

            case "SO3":
                if (promedio <= 125) return "bueno";
                else if (promedio <= 350) return "moderado";
                else return "malo";

            case "Temperatura":
                if (promedio <= 30) return "bueno";
                else if (promedio <= 35) return "moderado";
                else return "malo";

            default:
                return "desconocido"; // 处理未知类型
        }
    }

}


