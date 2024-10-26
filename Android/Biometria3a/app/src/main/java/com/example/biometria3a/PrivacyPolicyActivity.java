package com.example.biometria3a;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_policy);

        TextView txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setText("POLÍTICA DE PRIVACIDAD Y PROTECCIÓN DE DATOS\n\n" +
                "En VIMYP, nos comprometemos a proteger y respetar la privacidad de nuestros usuarios... " +
                // El resto del contenido aquí
                "Contacto\n\n" +
                "Si tienes alguna pregunta, inquietud o comentario sobre nuestra política de privacidad...");
    }
}

