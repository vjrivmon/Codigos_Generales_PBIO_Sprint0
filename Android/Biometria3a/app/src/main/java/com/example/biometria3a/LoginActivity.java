package com.example.biometria3a;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

import androidx.biometric.BiometricPrompt;

public class LoginActivity extends AppCompatActivity  {

    private BiometricAuthActivity  biometricCTR;

    private EditText edtEmail, edtPassword;
    private Button btnIniSesion;
private ImageView btnBiom;
    private TextView txtLogin;
    private ImageView imgTogglePassword;
    private boolean isPasswordVisible = false;

    private TextView xtOlvidarContrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EdgeToEdge.enable(this);


        btnBiom = findViewById(R.id.btnGooglePlus);


        btnBiom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBA();
                //fakeLogin();
            }
        });
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

        btnBiom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBA();
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


        // 在 onCreate 方法中
        TextView txtOlvidarContrasena = findViewById(R.id.txtOlvidarContrasena);

        // 在 txtOlvidarContrasena 的点击事件中调用 showForgotPasswordDialog
        txtOlvidarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog(); // 先显示输入邮箱的弹窗
            }
        });



    }
    private void goToBA() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(LoginActivity.this, BiometricAuthActivity.class);
        startActivity(intent);  // Iniciamos la actividad de inicio de sesión
        finish();  // Finaliza la actividad actual para evitar que el usuario regrese a la pantalla anterior
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
        verificarSensorAsignado(email);
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
                        // Guardar el correo en SharedPreferences
                        saveUserEmailToSession(email);
                        //String userRole = responseBody.get("rol").getAsString();
                        //Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso. Rol: " + userRole, Toast.LENGTH_SHORT).show();
                       Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso. ID Usuario: " + userId, Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                       // startActivity(intent);
                        saveUserIdToSession(userId);

                        finish();
                    } else {
                        Log.e("API Error", "Error al iniciar sesión. Código: " + response.code() + ", Mensaje: " + response.message());
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión. Credenciales incorrectos ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión. Credenciales incorrectos ", Toast.LENGTH_SHORT).show();
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

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);

        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        Button btnSendCode = dialogView.findViewById(R.id.btnSendCode);

        AlertDialog dialog = builder.create();

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Por favor, ingrese su correo electrónico.", Toast.LENGTH_SHORT).show();
                } else {
                    sendVerificationCode(email); // 发送验证码
                    dialog.dismiss(); // 关闭当前的邮箱输入弹窗
                    showResetPasswordDialog(); // 显示重置密码的弹窗
                }
            }
        });

        dialog.show();
    }
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        EditText edtVerificationCode = dialogView.findViewById(R.id.edtVerificationCode);
        EditText edtNewPassword = dialogView.findViewById(R.id.edtNewPassword);
        Button btnResetPassword = dialogView.findViewById(R.id.btnResetPassword);

        AlertDialog dialog = builder.create();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = edtVerificationCode.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();

                // 获取保存的验证码
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                String savedCode = sharedPreferences.getString("verificationCode", "");

                if (TextUtils.isEmpty(enteredCode) || TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                } else if (enteredCode.equals(savedCode)) {
                    // 获取用户的邮箱
                    String email = sharedPreferences.getString("email", ""); // 假设你已在发送验证码时保存了邮箱
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(LoginActivity.this, "Email no encontrado. Vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
                    } else {
                        resetPassword(email, newPassword);
                        dialog.dismiss(); // 关闭重置密码弹窗
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Código de verificación incorrecto.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();
    }


    private void resetPassword(String email, String newPassword) {
        // 验证邮箱和新密码是否为空
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建请求体
        HashMap<String, String> body = new HashMap<>();
        body.put("correo", email); // 用户邮箱
        body.put("nuevaContrasena", newPassword); // 用户的新密码

        // 调用后端 API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.recuperarContrasena(body);

        Log.d("API_REQUEST", "URL: " + call.request().url());
        Log.d("API_REQUEST", "Body: " + body.toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Contraseña actualizada correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.e("API_ERROR", "Response code: " + response.code());
                        Log.e("API_ERROR", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response.code() == 404) {
                        Toast.makeText(LoginActivity.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error al actualizar la contraseña. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







    private void sendVerificationCode(String email) {
        // 固定验证码为 "123456"
        String verificationCode = "123456";
        String subject = "Código de Verificación";
        String message = "Tu código de verificación es: " + verificationCode;

        // 将验证码和邮箱保存到 SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("verificationCode", verificationCode); // 保存验证码
        editor.putString("email", email); // 保存邮箱
        editor.apply();

        // 使用 MailSender 类发送邮件
        new MailSender(email, subject, message).execute();

        Toast.makeText(this, "Código de verificación enviado a: " + email, Toast.LENGTH_SHORT).show();
    }

// 删除原来的随机生成方法，因为不再需要动态生成验证码。
// 如果还需要保留，可以将 generateVerificationCode() 的返回值直接改为 "123456"。



    private void saveUserIdToSession(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }


    private void saveUserEmailToSession(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userEmail", email);
        editor.apply();
    }
    public void verificarSensorAsignado(String correo) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<SensorResponse> call = apiService.obtenerSensorPorCorreo(correo);

        call.enqueue(new Callback<SensorResponse>() {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                if (response.isSuccessful()) {
                    SensorResponse sensorResponse = response.body();
                    if (sensorResponse != null && sensorResponse.getId_sensor() != null) {
                        // Si el usuario tiene un sensor asignado
                        Toast.makeText(getApplicationContext(), "Sensor asignado: " + sensorResponse.getId_sensor(), Toast.LENGTH_LONG).show();
                        // Redirigir a MainActivity si tiene un sensor asignado
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Cerrar LoginActivity
                    } else {
                        // Si no tiene un sensor asignado
                        Toast.makeText(getApplicationContext(), "No tienes un sensor asignado.", Toast.LENGTH_LONG).show();
                        // Redirigir a QRActivity si no tiene un sensor asignado
                        Intent intent = new Intent(LoginActivity.this, QrActivity.class);
                        startActivity(intent);
                        finish(); // Cerrar LoginActivity

                    }
                } else {
                    // Si hubo un error con la respuesta
                    Toast.makeText(getApplicationContext(), "No tienes un sensor asignado.Por favor escanea el QR", Toast.LENGTH_LONG).show();
                    // Redirigir a QRActivity si no tiene un sensor asignado
                    Intent intent = new Intent(LoginActivity.this, QrActivity.class);
                    startActivity(intent);
                    finish(); // Cerrar LoginActivity
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                // Si hay un fallo en la conexión o en la solicitud
                Toast.makeText(getApplicationContext(), "Fallo de conexión 333: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
