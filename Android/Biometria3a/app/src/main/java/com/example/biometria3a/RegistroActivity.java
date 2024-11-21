package com.example.biometria3a;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword,edtPhone,edtConfirmPassword;
    private Button btnRegistrarse;
    private TextView  txtRegistrate;
    private CheckBox checkBoxPolitica,cbUppercase, cbNumber, cbSpecialChar, cbLength;
    private ProgressBar progressBar;  // Barra de progreso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Enlazar los elementos del layout
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtTelefono);
        btnRegistrarse = findViewById(R.id.btnIniSesion);
        txtRegistrate = findViewById(R.id.txtRegistrate);
        edtConfirmPassword = findViewById(R.id.edtconfirmPassword);

        // Enlazar los CheckBoxes de requisitos de contraseña
        cbUppercase = findViewById(R.id.cbUppercase);
        cbNumber = findViewById(R.id.cbNumber);
        cbSpecialChar = findViewById(R.id.cbSpecialChar);
        cbLength = findViewById(R.id.cbLength);

        // Enlazar la ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Establecer un TextWatcher para verificar la contraseña mientras el usuario escribe
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Obtener la contraseña ingresada por el usuario
                String password = editable.toString();

                // Variables para verificar cada uno de los requisitos
                boolean hasUppercase = false;
                boolean hasLowercase = false;
                boolean hasNumber = false;
                boolean hasSpecialChar = false;
                boolean hasMinLength = false;

                // Verificar si la contraseña tiene mayúsculas, minúsculas, números, caracteres especiales y longitud
                for (char c : password.toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        hasUppercase = true;
                    }
                    if (Character.isLowerCase(c)) {
                        hasLowercase = true;
                    }
                    if (Character.isDigit(c)) {
                        hasNumber = true;
                    }
                    if ("!@#$%^&*".contains(String.valueOf(c))) {
                        hasSpecialChar = true;
                    }
                }

                // Verificar longitud mínima
                if (password.length() >= 8) {
                    hasMinLength = true;
                }

                // Actualizar los CheckBox según si se cumple cada requisito
                cbUppercase.setChecked(hasUppercase && hasLowercase); // Requiere al menos una mayúscula y una minúscula
                cbNumber.setChecked(hasNumber);
                cbSpecialChar.setChecked(hasSpecialChar);
                cbLength.setChecked(hasMinLength);
                // Calcular el progreso
                int progress = 0;
                if (hasUppercase && hasLowercase) progress++;
                if (hasNumber) progress++;
                if (hasSpecialChar) progress++;
                if (hasMinLength) progress++;

                // Actualizar la barra de progreso
                progressBar.setProgress(progress);
            }

        });

        // Configurar el botón de registro
       // btnRegistrarse.setOnClickListener(new View.OnClickListener() {

        btnRegistrarse.setOnClickListener(view -> {

            // Crear el objeto User
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String phone = edtPhone.getText().toString().trim();

            String confirmPassword = edtConfirmPassword.getText().toString();

            // Imprimir los valores en el Logcat para ver qué se está enviando
            Log.d("RegistroActivity", "Nombre: " + username);
            Log.d("RegistroActivity", "Telefono: " + phone);
            Log.d("RegistroActivity", "Correo: " + email);
            Log.d("RegistroActivity", "Contraseña: " + password);
            if (!isValidPhoneNumber(phone)) {
                Toast.makeText(RegistroActivity.this, "Número de teléfono no válido", Toast.LENGTH_SHORT).show();
                return;
            }
                    // Validar los campos
                    if (TextUtils.isEmpty(username)) {
                        Toast.makeText(RegistroActivity.this, "Por favor, introduce tu nombre de usuario", Toast.LENGTH_SHORT).show();
                        return;
                    }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(RegistroActivity.this, "Por favor, introduce tu número de teléfono", Toast.LENGTH_SHORT).show();
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
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(RegistroActivity.this, "Por favor, confirma tu contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
            // Validar que las contraseñas coincidan
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden, por favor verifica", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar los requisitos de la contraseña
            if (!cbUppercase.isChecked() || !cbNumber.isChecked() || !cbSpecialChar.isChecked() || !cbLength.isChecked()) {
                Toast.makeText(RegistroActivity.this, "Por favor, cumple con todos los requisitos de la contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkBoxPolitica.isChecked()) {
                Toast.makeText(RegistroActivity.this, "Por favor, acepta la política de privacidad", Toast.LENGTH_SHORT).show();
                return;

            }
            // Generar y enviar código de verificación
            String verificationCode = generateVerificationCode();
            sendVerificationCode(email, verificationCode);

            // Mostrar pop-up para verificar el código
            showVerificationPopup(email, verificationCode);


            //----------------------REGISTRO------------------------------

          /*  Usuario newUser = new Usuario(username, phone, email, password);

            // Llamar a la API para registrar el usuario
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Void> call = apiService.registerUser(newUser);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();

                        //Intent intent = new Intent(RegistroActivity.this, VerificationActivity.class);
                       // startActivity(intent);

                        finish(); // Cerrar la actividad después del registro
                    } else {
                        // Mostrar más detalles sobre el error
                        String errorMessage = "Error al registrar el usuario. Intenta de nuevo.";
                        if (response.code() == 400) {
                            errorMessage = "Solicitud incorrecta (400). Revisa los datos enviados.";
                        } else if (response.code() == 500) {
                            errorMessage = "Error del servidor (500). Intenta más tarde.";
                        }

                        // Imprimir el código de estado y el cuerpo de la respuesta para depuración
                        Log.e("RegistroError", "Código de error: " + response.code());
                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("RegistroError", "Cuerpo del error: " + errorBody);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(RegistroActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegistroActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


           */
        });

        // Asignamos el OnClickListener al TextView "Inicia sesión"
        txtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Método que se ejecuta al hacer clic
                goToLoginActivity();  // Llamamos al método que lleva a la actividad de inicio de sesión
            }
        });


        checkBoxPolitica = findViewById(R.id.checkBoxPolitica);
        TextView txtPoliticaEnlace = findViewById(R.id.txtPoliticaEnlace);

        // Desactivar el CheckBox al inicio
        checkBoxPolitica.setEnabled(false);
        // Configurar el listener en el TextView
        txtPoliticaEnlace.setOnClickListener(v -> {
            // Crear el AlertDialog
            new AlertDialog.Builder(RegistroActivity.this)
                    .setTitle("Política de Privacidad y Protección de Datos")
                    .setMessage("POLÍTICA DE PRIVACIDAD Y PROTECCIÓN DE DATOS\n\n" +
                            "En VIMYP, nos comprometemos a proteger y respetar la privacidad de nuestros usuarios, " +
                            "cumpliendo con las exigencias establecidas en la Ley Orgánica 15/1999 de 13 de diciembre, " +
                            "sobre Protección de Datos de Carácter Personal, su normativa de desarrollo y las disposiciones " +
                            "de la Ley 34/2002 de 11 de julio, de Servicios de la Sociedad de la Información. Esta política " +
                            "de privacidad detalla cómo recopilamos, utilizamos, almacenamos y protegemos tu información personal, " +
                            "así como los derechos que tienes respecto a ella.\n\n" +
                            "Recopilación de Información\n" +
                            "Podemos recopilar diversos tipos de información personal cuando interactúas con nuestro sitio web o utilizas " +
                            "nuestros servicios, incluyendo, pero no limitado a:\n\n" +
                            "• Datos personales: Nombre, apellidos, dirección de correo electrónico y otros datos de contacto como dirección " +
                            "postal y número de teléfono, necesarios para identificarte y gestionar la relación comercial.\n" +
                            "• Datos de uso del sitio web: Información sobre tu actividad en el sitio, como páginas visitadas, tiempo de permanencia, " +
                            "clics, dirección IP, tipo de navegador, entre otros, que nos ayudan a mejorar la experiencia del usuario y optimizar " +
                            "nuestros servicios.\n\n" +
                            "Uso de la Información\n" +
                            "Utilizamos tu información personal para una variedad de propósitos, entre los que se incluyen:\n\n" +
                            "• Prestación de servicios: Gestionar la relación comercial y los servicios solicitados, incluyendo la tramitación " +
                            "de pedidos y la atención de consultas.\n" +
                            "• Mejora de la experiencia de usuario: Personalizar el contenido y las funciones del sitio web para ofrecer una " +
                            "experiencia más relevante y atractiva.\n" +
                            "• Envío de actualizaciones: Mantenerte informado sobre productos, servicios, promociones y cambios relevantes que " +
                            "puedan ser de tu interés, siempre y cuando cuentes con consentimiento previo.\n\n" +
                            "Protección de la Información\n" +
                            "Implementamos medidas técnicas, organizativas y de seguridad necesarias para garantizar la confidencialidad de tu " +
                            "información personal y protegerla de accesos no autorizados, alteración, pérdida o tratamiento indebido.\n\n" +
                            "Compartir Información\n" +
                            "En ningún caso compartiremos tu información personal con terceros, excepto en las siguientes circunstancias:\n" +
                            "• Obligaciones legales: Podemos compartir tu información si así lo exige la ley o en respuesta a solicitudes válidas " +
                            "de autoridades públicas.\n" +
                            "• Consentimiento explícito: Solo compartiremos tus datos con terceros si has dado tu consentimiento expreso para ello.\n\n" +
                            "Derechos del Usuario\n" +
                            "Como usuario, tienes derechos en relación con el tratamiento de tus datos personales. Puedes ejercer los siguientes " +
                            "derechos:\n\n" +
                            "• Acceso: Solicitar una copia de la información personal que tenemos sobre ti.\n" +
                            "• Rectificación: Corregir cualquier información inexacta o incompleta.\n\n" +
                            "Cambios en la Política\n" +
                            "Esta política de privacidad puede actualizarse periódicamente para reflejar cambios en nuestras prácticas de información.\n\n" +
                            "Contacto\n" +
                            "Si tienes alguna pregunta, no dudes en ponerte en contacto con nosotros.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Habilitar el CheckBox cuando se cierre el diálogo
                            checkBoxPolitica.setEnabled(true);
                        }
                    })
                    .setCancelable(false) // Evita que se cierre sin aceptar
                    .show();

        });

    }


    private String generateVerificationCode() {
        int randomCode = (int) (Math.random() * 900000) + 100000; // Genera un código de 6 dígitos
        return String.valueOf(randomCode);
    }

    private void sendVerificationCode(String email, String code) {
        String subject = "Código de Verificación";
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
                Toast.makeText(RegistroActivity.this, "Verificación exitosa.", Toast.LENGTH_SHORT).show();
                completeRegistration(); // Llama al método para registrar al usuario
            } else {
                Toast.makeText(RegistroActivity.this, "Código incorrecto, intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }
    private void completeRegistration() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        Usuario newUser = new Usuario(username, phone, email, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.registerUser(newUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                } else {
                    Toast.makeText(RegistroActivity.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToLoginActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
    }

    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToMainActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
    }
    // Método para validar el formato del teléfono
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Validación simple para asegurar que el número tiene solo dígitos (puedes ajustarlo a tu necesidad)
        return phoneNumber.matches("[0-9]{10}");  // Acepta solo números de 10 dígitos
    }

/*
    private void registrarUsuario(Usuario usuario) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.crearUsuario(usuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    // Aquí puedes redirigir a la actividad de inicio de sesión o principal
                } else {
                    Toast.makeText(RegistroActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

 */
}
