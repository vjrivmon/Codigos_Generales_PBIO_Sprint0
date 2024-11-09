package com.example.biometria3a;


import java.util.Random;

public class VerificationManager {

    private String generatedCode;

    // 生成随机验证码
    public String generateVerificationCode() {
        generatedCode = String.valueOf(new Random().nextInt(999999));
        return generatedCode;
    }

    // 验证输入的验证码是否正确
    public boolean verifyCode(String inputCode) {
        return inputCode.equals(generatedCode);
    }
}

