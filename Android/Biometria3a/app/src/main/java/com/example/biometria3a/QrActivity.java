package com.example.biometria3a;

// Importa las clases necesarias
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

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
                    }
                });
    }

