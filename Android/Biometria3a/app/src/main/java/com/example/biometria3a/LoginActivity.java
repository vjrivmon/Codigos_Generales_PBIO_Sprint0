package com.example.biometria3a;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnIniSesion;
    private TextView txtLogin;
    private ImageView imgTogglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enlazar los elementos del layout
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnIniSesion = findViewById(R.id.btnIniSesion);
        txtLogin = findViewById(R.id.txtRegistrate);
        imgTogglePassword = findViewById(R.id.imgTogglePassword);

        imgTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alternar visibilidad de la contraseña
                if (isPasswordVisible) {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imgTogglePassword.setImageResource(R.drawable.eye); // Cambia al icono de ojo cerrado
                } else {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imgTogglePassword.setImageResource(R.drawable.eyeclosed); // Cambia al icono de ojo abierto
                }
                isPasswordVisible = !isPasswordVisible;

                // Mover el cursor al final del texto
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });

        // Configurar el botón de inicio de sesión
        btnIniSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                //fakeLogin();
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

        // Validar los campos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor, ingresa correo y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el servicio API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Crear un mapa con las credenciales
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("correo", email);
        credentials.put("contrasena", password);

        // Hacer la solicitud al servidor
        Call<JsonObject> call = apiService.loginUser(credentials);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean success = responseBody.get("success").getAsBoolean();


                    if (success) {
                        int userId = responseBody.get("id_usuario").getAsInt();
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso. ID Usuario: " + userId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        saveUserIdToSession(userId);

                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas, intenta de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
      //  });

   // }

 /*   @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                // Error de conexión o de servidor
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

  */
           // }
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
    /*private void fakeLogin() {
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
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);  // Inicia la nueva actividad
        } else {
            Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
*/
    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToRegistroActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
    }

    private void saveUserIdToSession(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }

}
