package com.example.biometria3a;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnIniSesion;

    private TextView txtLogin;
    private ImageView imgTogglePassword;
    private boolean isPasswordVisible = false;

    private TextView xtOlvidarContrasena;


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

        xtOlvidarContrasena = findViewById(R.id.txtOlvidarContrasena);


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

        // Agregar esto en tu onCreate() después de los demás listeners
        xtOlvidarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar un cuadro de texto para ingresar el correo electrónico
                final EditText emailInput = new EditText(LoginActivity.this);
                emailInput.setHint("Introduce tu correo electrónico");

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Recuperar Contraseña")
                        .setMessage("Por favor, introduce tu correo electrónico para recuperar la contraseña.")
                        .setView(emailInput)
                        .setPositiveButton("Enviar código", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String email = emailInput.getText().toString().trim();

                                if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                                    Toast.makeText(LoginActivity.this, "Por favor, introduce un correo electrónico válido.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Generar y enviar el código de verificación
                                String verificationCode = generateVerificationCode();
                                sendVerificationCode(email, verificationCode);

                                // Mostrar pop-up para ingresar el código de verificación
                                showVerificationPopup(email, verificationCode);
                            }
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .setCancelable(false) // Evitar que se cierre sin interacción
                        .show();
            }
        });

    }
    // Método para verificar si el correo es válido
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private String generateVerificationCode() {
        // Generar un código aleatorio (por ejemplo, de 6 dígitos)
        int code = (int) (Math.random() * 1000000);
        return String.format("%06d", code);
    }
    private void sendVerificationCode(String email, String code) {
        String subject = "Código de Verificación para Recuperación de Contraseña";
        String message = "Tu código de verificación es: " + code;
        new MailSender(email, subject, message).execute(); // Llama a MailSender para enviar el correo
    }
    private void showVerificationPopup(String email, String generatedCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verificación de Correo");

        // EditText para que el usuario introduzca el código
        final EditText input = new EditText(this);
        input.setHint("Introduce el código");
        builder.setView(input);

        builder.setPositiveButton("Verificar", (dialog, which) -> {
            String enteredCode = input.getText().toString().trim();

            if (enteredCode.equals(generatedCode)) {
                Toast.makeText(LoginActivity.this, "Verificación exitosa.", Toast.LENGTH_SHORT).show();
                // Aquí puedes redirigir al usuario para cambiar la contraseña
                showResetPasswordDialog(email); // Método que muestra el diálogo para cambiar la contraseña
            } else {
                Toast.makeText(LoginActivity.this, "Código incorrecto, intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }
    private void showResetPasswordDialog(String email) {
        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setHint("Introduce tu nueva contraseña");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Contraseña")
                .setMessage("Introduce una nueva contraseña.")
                .setView(newPasswordInput)
                .setPositiveButton("Actualizar Contraseña", (dialog, which) -> {
                    String newPassword = newPasswordInput.getText().toString().trim();
                    if (TextUtils.isEmpty(newPassword)) {
                        Toast.makeText(LoginActivity.this, "Por favor, introduce una nueva contraseña.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Aquí puedes llamar a tu API para actualizar la contraseña del usuario
                    updatePassword(email, newPassword);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }
    private void updatePassword(String email, String newPassword) {
        // Aquí obtienes el ID del usuario que está almacenado (podrías almacenarlo en las SharedPreferences al hacer login)
        int userId = getUserIdFromSession();  // Método para obtener el ID del usuario almacenado

        if (userId == -1) {
            Toast.makeText(LoginActivity.this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea el objeto con los parámetros para la solicitud
        HashMap<String, String> params = new HashMap<>();
        params.put("correo", email);
        params.put("nueva_contrasena", newPassword);

        // Crear el servicio API usando Retrofit
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.changePassword(userId, params);

        // Hacer la solicitud al servidor
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Contraseña actualizada exitosamente.", Toast.LENGTH_SHORT).show();
                    goToLoginActivity();  // Redirige a la pantalla de inicio de sesión
                } else {
                    Toast.makeText(LoginActivity.this, "Error al actualizar la contraseña.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int getUserIdFromSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);  // Devuelve -1 si no se ha guardado el ID
    }



    private void goToLoginActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(intent);  // Iniciamos la actividad de inicio de sesión
        finish();  // Finaliza la actividad actual para evitar que el usuario regrese a la pantalla anterior
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
                        Log.d("LoginActivity", "ID de usuario: " + userId);
                        checkSensorAssignment(userId);
                        //String userRole = responseBody.get("rol").getAsString();
                        //Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso. Rol: " + userRole, Toast.LENGTH_SHORT).show();
                       Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso. ID Usuario: " + userId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        saveUserIdToSession(userId);

                        finish();
                    } else {
                        Log.e("API Error", "Error al iniciar sesión1. Código: " + response.code() + ", Mensaje: " + response.message());
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión111. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error al iniciar sesión3. Código: " + response.code() + ", Mensaje: " + response.message());
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

    private void checkSensorAssignment(int userId) {
        // Consultar si el usuario tiene un sensor asignado
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getSensorsByUser(userId).enqueue(new Callback<List<Sensor>>() {
            @Override
            public void onResponse(Call<List<Sensor>> call, Response<List<Sensor>> response) {
                if (response.isSuccessful()) {
                    List<Sensor> sensores = response.body();
                    Log.d("SensorResponse", "Sensores: " + (sensores != null ? sensores.size() : "null"));

                    if (sensores == null || sensores.isEmpty()) {
                        // El usuario no tiene sensor asignado, redirigir a la actividad QR
                        Log.d("SensorResponse", "No se encontró un sensor asignado.");
                        Intent intent = new Intent(LoginActivity.this, QrActivity.class);
                        startActivity(intent);
                    } else {
                        // El usuario tiene un sensor asignado, continuar con la aplicación normal
                        Log.d("SensorResponse", "Sensor asignado: " + sensores.size());
                        Toast.makeText(LoginActivity.this, "Sensor asignado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("SensorResponse", "Error en la respuesta del servidor: " + response.code() + " " + response.message());
                    Toast.makeText(LoginActivity.this, "Error al verificar sensor", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Sensor>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
