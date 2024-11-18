package com.example.biometria3a;


import android.os.Bundle;
import android.util.Log;
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

        // 设置日期选择框的数据
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

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDate = dates[position];
                fetchMediciones();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fetchMediciones();
    }

    private void fetchMediciones() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
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
           // Call<List<Medicion2>> call = RetrofitClient.getMedicionesBySensor(idSensor);
            Log.d("AirQualityActivity", "fetchMediciones: " + call.request().url());
            Log.d("AirQualityActivity", "fetchMediciones: " + allMediciones+    " " + idSensor);


            call.enqueue(new Callback<List<Medicion2>>() {
                @Override
                public void onResponse(Call<List<Medicion2>> call, Response<List<Medicion2>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (Medicion2 medicion : response.body()) {
                            Log.d("AirQualityActivity", "Comparando fecha: " + medicion.getFecha() + " con " + selectedDate);

                            if (medicion.getFecha().equals(selectedDate)) {
                                allMediciones.add(medicion);
                            }
                        }
                        showBarChart(allMediciones);
                    } else {
                        Toast.makeText(AirQualityActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Medicion2>> call, Throwable t) {
                    Toast.makeText(AirQualityActivity.this, "Fallo en la conexión", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void showBarChart(List<Medicion2> mediciones) {
        ArrayList<BarEntry> entriesO3 = new ArrayList<>();
        ArrayList<BarEntry> entriesNO2 = new ArrayList<>();
        ArrayList<BarEntry> entriesSO3 = new ArrayList<>();

        for (int i = 0; i < mediciones.size(); i++) {
            Medicion2 medicion = mediciones.get(i);
            // 转换 double 为 float
            entriesO3.add(new BarEntry(i, (float) medicion.getValorO3()));
            entriesNO2.add(new BarEntry(i, (float) medicion.getValorNO2()));
            entriesSO3.add(new BarEntry(i, (float) medicion.getValorSO3()));
        }

        BarDataSet dataSetO3 = new BarDataSet(entriesO3, "Valor O3");
        dataSetO3.setColor(Color.RED);

        BarDataSet dataSetNO2 = new BarDataSet(entriesNO2, "Valor NO2");
        dataSetNO2.setColor(Color.BLUE);

        BarDataSet dataSetSO3 = new BarDataSet(entriesSO3, "Valor SO3");
        dataSetSO3.setColor(Color.GREEN);

        BarData barData = new BarData(dataSetO3, dataSetNO2, dataSetSO3);
        barChart.setData(barData);
        barChart.invalidate();
    }

}

