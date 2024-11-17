package com.example.biometria3a;


public class Medicion2 {
    private int id;
    private String fecha;
    private String hora;
    private double latitud;
    private double longitud;
    private String id_sensor;
    private float valorO3;
    private float valorTemperatura;
    private float valorNO2;
    private float valorSO3;

    // Getter 方法
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public float getValorO3() { return valorO3; }
    public float getValorTemperatura() { return valorTemperatura; }
    public float getValorNO2() { return valorNO2; }
    public float getValorSO3() { return valorSO3; }
}

