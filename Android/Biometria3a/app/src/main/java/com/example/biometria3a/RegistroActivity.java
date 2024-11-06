package com.example.biometria3a;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnRegistrarse;
    private TextView  txtRegistrate;
    private CheckBox checkBoxPolitica;

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
       // btnRegistrarse.setOnClickListener(new View.OnClickListener() {

        btnRegistrarse.setOnClickListener(view -> {

            // Crear el objeto User
            String username = edtUsername.getText().toString().trim();
            String email =edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

                    // Validar los campos
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
            if (!checkBoxPolitica.isChecked()) {
                Toast.makeText(RegistroActivity.this, "Por favor, acepta la política de privacidad", Toast.LENGTH_SHORT).show();
                return;
            }



            //----------------------REGISTRO------------------------------

            Usuario newUser = new Usuario(email, password);

            // Llamar a la API para registrar el usuario
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Void> call = apiService.registerUser(newUser);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                        finish(); // Cerrar la actividad después del registro
                    } else {
                        Toast.makeText(RegistroActivity.this, "Error al registrar el usuario. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegistroActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

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




    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToLoginActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
    }

    // Método para iniciar la actividad "IniciaSesionActivity"
    private void goToMainActivity() {
        // Creamos un Intent para abrir la actividad de inicio de sesión
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);  // Iniciamos la nueva actividad
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
