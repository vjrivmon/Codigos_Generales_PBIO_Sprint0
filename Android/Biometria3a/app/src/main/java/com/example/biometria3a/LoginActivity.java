package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnIniSesion;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enlazar los elementos del layout
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnIniSesion = findViewById(R.id.btnIniSesion);
        txtLogin = findViewById(R.id.txtRegistrate);

        // Configurar el botón de inicio de sesión
        btnIniSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeLogin();
            }
        });

        // Asignamos el OnClickListener al TextView "Inicia sesión"
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Método que se ejecuta al hacer clic
                goToRegistroActivity();  // Llamamos al método que lleva a la actividad de inicio de sesión
            }
        });
    }

    // Lógica "fake" para simular el inicio de sesión
    private void fakeLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Por favor, introduce tu correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Por favor, introduce tu contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulación: Si el correo y la contraseña no están vacíos, se muestra un mensaje de éxito.
        if (email.equals("mimi") && password.equals("123456")) {
            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            // Inicia la actividad MainActivity
            Intent intent = new Intent(LoginActivity.this, Mapa_Activity.class);
            startActivity(intent);  // Inicia la nueva actividad
        } else {
            Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToRegistroActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
    }
}
