package com.example.biometria3a;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import java.util.concurrent.Executor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import java.util.concurrent.Executor;

public class BiometricAuthActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_auth);

        // Verificar si el dispositivo soporta biometría
        BiometricManager biometricManager = BiometricManager.from(this);
        int biometricStatus = biometricManager.canAuthenticate();

        if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this, "El dispositivo soporta biometría.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "El dispositivo no soporta biometría.", Toast.LENGTH_SHORT).show();
        }

        // Executor para la autenticación
        Executor executor = ContextCompat.getMainExecutor(this);

        // Crear BiometricPrompt
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(BiometricAuthActivity.this, "¡Autenticación exitosa!", Toast.LENGTH_SHORT).show();
                // Redirige a otra actividad si es necesario
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(BiometricAuthActivity.this, "Autenticación fallida.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(BiometricAuthActivity.this, "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        // Información de la solicitud de autenticación
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Acceso biométrico")
                .setSubtitle("Usa tu rostro para autenticarte")
                .setDescription("Mira hacia la cámara para iniciar sesión")
                .setDeviceCredentialAllowed(true)  // Permite usar PIN si biometría falla
                .build();

        // Iniciar autenticación al hacer clic en el botón
        findViewById(R.id.btn_authenticate).setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }
}


