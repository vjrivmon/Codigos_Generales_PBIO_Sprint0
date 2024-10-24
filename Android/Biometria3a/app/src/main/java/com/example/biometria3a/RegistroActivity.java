package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnRegistrarse;
    private TextView  txtRegistrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Enlazar los elementos del layout
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegistrarse = findViewById(R.id.btnIniSesion);
        txtRegistrate = findViewById(R.id.txtRegistrate);

        // Configurar el botón de registro
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeRegister();
            }
        });

        // Asignamos el OnClickListener al TextView "Inicia sesión"
        txtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Método que se ejecuta al hacer clic
                goToLoginActivity();  // Llamamos al método que lleva a la actividad de inicio de sesión
            }
        });
    }

    // Lógica "fake" para simular el registro
    private void fakeRegister() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(RegistroActivity.this, "Por favor, introduce tu nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegistroActivity.this, "Por favor, introduce tu correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegistroActivity.this, "Por favor, introduce tu contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulación: Si todos los campos están llenos, se muestra un mensaje de éxito.
        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
    }

    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToLoginActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
    }
}
