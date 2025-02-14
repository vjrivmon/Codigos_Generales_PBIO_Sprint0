package com.example.biometria3a;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {

    private EditText emailEditText, codeEditText;
    private Button sendEmailButton, verifyButton;
    private VerificationManager verificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);  // La vista con el formulario de verificación

        // Enlazar los elementos de la UI
        emailEditText = findViewById(R.id.emailEditText);
        codeEditText = findViewById(R.id.codeEditText);
        sendEmailButton = findViewById(R.id.sendEmailButton);
        verifyButton = findViewById(R.id.verifyButton);

        verificationManager = new VerificationManager();

        // Enviar el código de verificación
        sendEmailButton.setOnClickListener(v -> sendVerificationEmail());

        // Verificar el código ingresado
        verifyButton.setOnClickListener(v -> verifyCode());
    }

    private void sendVerificationEmail() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(VerificationActivity.this, "Por favor, introduce una dirección de correo electrónico válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generar el código de verificación
        String code = verificationManager.generateVerificationCode();
        String subject = "Verificación de cuenta";
        String message = "Tu código de verificación es: " + code;

        // Enviar el correo
        MailSender mailSender = new MailSender(email, subject, message);
        mailSender.execute();

        Toast.makeText(VerificationActivity.this, "Se ha enviado el código de verificación, por favor revisa tu correo electrónico", Toast.LENGTH_SHORT).show();
    }

    private void verifyCode() {
        String inputCode = codeEditText.getText().toString().trim();

        if (verificationManager.verifyCode(inputCode)) {
            Toast.makeText(VerificationActivity.this, "Verificación exitosa, inicio de sesión exitoso！", Toast.LENGTH_SHORT).show();
            // Aquí puedes redirigir al usuario a la siguiente actividad (p.ej. MainActivity)
        } else {
            Toast.makeText(VerificationActivity.this, "Error en el código de verificación, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
        }
    }
}
