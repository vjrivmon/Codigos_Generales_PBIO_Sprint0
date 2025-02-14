package com.example.biometria3a;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VerificationFragment extends Fragment {

    private EditText emailEditText, codeEditText;
    private Button sendEmailButton, verifyButton;
    private VerificationManager verificationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载您自己的布局文件 activity_verification.xml
        View view = inflater.inflate(R.layout.activity_verification, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        codeEditText = view.findViewById(R.id.codeEditText);
        sendEmailButton = view.findViewById(R.id.sendEmailButton);
        verifyButton = view.findViewById(R.id.verifyButton);

        verificationManager = new VerificationManager();

        sendEmailButton.setOnClickListener(v -> sendVerificationEmail());
        verifyButton.setOnClickListener(v -> verifyCode());

        return view;
    }

    private void sendVerificationEmail() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, introduce una dirección de correo electrónico válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // 生成验证码
        String code = verificationManager.generateVerificationCode();
        String subject = "Verificación de cuenta";
        String message = "Tu código de verificación es: " + code;

        // 发送邮件
        MailSender mailSender = new MailSender(email, subject, message);
        mailSender.execute();

        Toast.makeText(getActivity(), "Se ha enviado el código de verificación, por favor revisa tu correo electrónico", Toast.LENGTH_SHORT).show();
    }

    private void verifyCode() {
        String inputCode = codeEditText.getText().toString().trim();

        if (verificationManager.verifyCode(inputCode)) {
            Toast.makeText(getActivity(), "Verificación exitosa, inicio de sesión exitoso！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error en el código de verificación, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
        }
    }
}

