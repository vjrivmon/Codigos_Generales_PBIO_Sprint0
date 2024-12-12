package com.example.biometria3a;

// Importa las clases necesarias
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QrActivity extends AppCompatActivity {

        private EditText inputQr;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr);

            inputQr = findViewById(R.id.inputQr);
            Button scanButton = findViewById(R.id.scanButton);

            scanButton.setOnClickListener(v -> {
                ScanOptions options = new ScanOptions();
                options.setPrompt("Escanea un código QR");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                scanLauncher.launch(options);
            });


        }

        // Lanzar la actividad de escaneo
        private final ActivityResultLauncher<ScanOptions> scanLauncher =
                registerForActivityResult(new ScanContract(), result -> {
                    if (result.getContents() != null) {
                        // Establecer el número escaneado en el EditText
                        inputQr.setText(result.getContents());
                        // Llamar al método para asociar el sensor
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        String email = sharedPreferences.getString("userEmail", "");  // Valor por defecto es una cadena vacía si no se encuentra

                        asociarSensor(email,"00:1A:2B:3M:4D:5E", "Sensor de temperatura", "true");
                    }
                });




    // Método para asociar el sensor al usuario
    private void asociarSensor(String correo, String idSensor, String nombre, String funciona) {
        // Crear la solicitud para asociar el sensor
        AsociarSensorRequest request = new AsociarSensorRequest(correo, idSensor, nombre, funciona);

        // Usamos la instancia de Retrofit obtenida a través de ApiClient
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Realizar la llamada POST para asociar el sensor
        Call<Void> call = apiService.asociarSensorAUsuario(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QrActivity.this, "Sensor asociado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QrActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Elimina esta actividad de la pila
                    startActivity(intent);
                } else {
                    Toast.makeText(QrActivity.this, "Error al asociar el sensor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(QrActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    }

