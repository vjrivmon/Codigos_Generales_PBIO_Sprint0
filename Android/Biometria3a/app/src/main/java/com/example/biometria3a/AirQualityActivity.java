package com.example.biometria3a;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirQualityActivity extends AppCompatActivity {

    private BarChart barChart;
    private Spinner dateSpinner;
    private String selectedDate = "21/11/2024";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);

        barChart = findViewById(R.id.barChart);
        dateSpinner = findViewById(R.id.dateSpinner);

        // 初始化日期选择框
        String[] dates = {
                "18/11/2024",
                "19/11/2024",
                "20/11/2024",
                "21/11/2024",
                "22/11/2024"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);

        // 日期选择监听器
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDate = dates[position];
                fetchMediciones(); // 每次选择日期时重新获取数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 不做任何操作
            }
        });

        fetchMediciones(); // 初始调用获取数据
    }

    private void fetchMediciones() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class); // 使用 ApiClient 创建 Retrofit 实例
        String[] sensorIds = {
                "00:1A:2B:3M:4D:5E",
                "11:2B:2X:3L:4K:5F",
                "22:3C:2T:3U:4H:5G",
                "33:4A:2R:3Q:4G:5H",
                "44:5C:2V:3S:4F:6I"
        };

        List<Medicion2> allMediciones = new ArrayList<>();

        for (String idSensor : sensorIds) {
            Call<List<Medicion2>> call = apiService.getMedicionesBySensor(idSensor);

            call.enqueue(new Callback<List<Medicion2>>() {
                @Override
                public void onResponse(Call<List<Medicion2>> call, Response<List<Medicion2>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (Medicion2 medicion : response.body()) {
                            if (medicion.getFecha().equals(selectedDate)) {
                                allMediciones.add(medicion);
                            }
                        }
                        showBarChart(allMediciones);
                        updateAirQuality(allMediciones);
                    } else {
                        Toast.makeText(AirQualityActivity.this, "Error al obtener datos: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Medicion2>> call, Throwable t) {
                    Toast.makeText(AirQualityActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showBarChart(List<Medicion2> mediciones) {
        ArrayList<BarEntry> entriesO3 = new ArrayList<>();
        ArrayList<BarEntry> entriesNO2 = new ArrayList<>();
        ArrayList<BarEntry> entriesSO3 = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();

        if (mediciones.isEmpty()) {
            Toast.makeText(this, "No hay datos disponibles para la fecha seleccionada", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据选择的日期设置 X 轴标签
        if (selectedDate.equals("21/11/2024")) {
            xLabels.add("10:00");
            xLabels.add("11:00");
            xLabels.add("12:00");
        } else if (selectedDate.equals("22/11/2024")) {
            xLabels.add("13:00");
            xLabels.add("14:00");
            xLabels.add("15:00");
        }

        for (int i = 0; i < mediciones.size(); i++) {
            Medicion2 medicion = mediciones.get(i);
            entriesO3.add(new BarEntry(i, medicion.getValorO3()));
            entriesNO2.add(new BarEntry(i, medicion.getValorNO2()));
            entriesSO3.add(new BarEntry(i, medicion.getValorSO3()));
        }

        BarDataSet dataSetO3 = new BarDataSet(entriesO3, "Valor O3");
        dataSetO3.setColor(Color.RED);

        BarDataSet dataSetNO2 = new BarDataSet(entriesNO2, "Valor NO2");
        dataSetNO2.setColor(Color.BLUE);

        BarDataSet dataSetSO3 = new BarDataSet(entriesSO3, "Valor SO3");
        dataSetSO3.setColor(Color.GREEN);

        BarData barData = new BarData(dataSetO3, dataSetNO2, dataSetSO3);
        barData.setBarWidth(0.2f);

        float groupSpace = 0.4f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;

        barData.setBarWidth(barWidth);
        barChart.setData(barData);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(mediciones.size());
        barChart.groupBars(0, groupSpace, barSpace);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(xLabels.size());

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(6, true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisLineColor(Color.BLACK);

        leftAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) value) + " μg/m³";
            }
        });

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void updateAirQuality(List<Medicion2> mediciones) {
        float totalO3 = 0;
        float totalNO2 = 0;
        float totalSO3 = 0;
        int count = mediciones.size();

        if (count == 0) {
            Toast.makeText(this, "No hay datos disponibles para la fecha seleccionada", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Medicion2 medicion : mediciones) {
            totalO3 += medicion.getValorO3();
            totalNO2 += medicion.getValorNO2();
            totalSO3 += medicion.getValorSO3();
        }

        float avgO3 = totalO3 / count;
        float avgNO2 = totalNO2 / count;
        float avgSO3 = totalSO3 / count;

        String airQualityO3;
        if (avgO3 <= 120) {
            airQualityO3 = "O3: Buena";
        } else if (avgO3 <= 180) {
            airQualityO3 = "O3: Moderada";
        } else {
            airQualityO3 = "O3: Mala";
        }

        String airQualityNO2;
        if (avgNO2 <= 40) {
            airQualityNO2 = "NO2: Buena";
        } else if (avgNO2 <= 200) {
            airQualityNO2 = "NO2: Moderada";
        } else {
            airQualityNO2 = "NO2: Mala";
        }

        String airQualitySO3;
        if (avgSO3 <= 125) {
            airQualitySO3 = "SO3: Buena";
        } else if (avgSO3 <= 350) {
            airQualitySO3 = "SO3: Moderada";
        } else {
            airQualitySO3 = "SO3: Mala";
        }

        TextView airQualityTextView = findViewById(R.id.airQualityTextView);
        airQualityTextView.setTextColor(Color.BLACK);
        String airQualityText = String.format(
                "Fecha: %s\nPromedio O3: %.2f μg/m³ (%s)\nPromedio NO2: %.2f μg/m³ (%s)\nPromedio SO3: %.2f μg/m³ (%s)",
                selectedDate, avgO3, airQualityO3, avgNO2, airQualityNO2, avgSO3, airQualitySO3
        );
        airQualityTextView.setText(airQualityText);

        if (avgO3 > 180 || avgNO2 > 200 || avgSO3 > 350) {
            airQualityTextView.setBackgroundColor(Color.RED);
        } else if (avgO3 > 120 || avgNO2 > 40 || avgSO3 > 125) {
            airQualityTextView.setBackgroundColor(Color.YELLOW);
        } else {
            airQualityTextView.setBackgroundColor(Color.GREEN);
        }
    }
}
