package com.example.biometria3a;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                login();
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

    private void login() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        Log.d("LoginActivity", "Correo: " + email);
        Log.d("LoginActivity", "Contraseña: " + password);

        // Validar los campos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor, ingresa correo y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Usuario con las credenciales
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
      Call<Usuario> call = apiService.getUserByCredentials(email, password);



        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario usuario = response.body();
                    if (usuario != null) {
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, Mapa_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas, intenta de nuevo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostrar el código de error y el cuerpo de la respuesta para depurar
                    Log.e("LoginActivity", "Error en la respuesta del servidor. Código de error: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("LoginActivity", "Cuerpo de error: " + errorBody);
                            Toast.makeText(LoginActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // Error de conexión o de servidor
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
/*
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    Log.d("LoginActivity", "Usuario recibido: " + usuario);  // Imprime el objeto usuario
                } else {
                    Log.d("LoginActivity", "Respuesta de la API no exitosa o sin cuerpo: " + response);
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

 */
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
