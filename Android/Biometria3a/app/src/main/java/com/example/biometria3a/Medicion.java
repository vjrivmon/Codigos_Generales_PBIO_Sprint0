package com.example.biometria3a;


public class Medicion {
    private String fecha; // Nuevo campo
    private String hora;
    private double latitud;
    private double longitud;
    private String id_sensor;
    private double valorO3; // Nuevo campo
    private double valorTemperatura;
    private double valorNO2; // Nuevo campo
    private double valorSO3; // Nuevo campo

    // Constructor
    public Medicion(String fecha, String hora, double latitud, double longitud, String id_sensor,
                    double valorO3, double valorTemperatura, double valorNO2, double valorSO3) {
        this.fecha = fecha;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.id_sensor = id_sensor;
        this.valorO3 = valorO3;
        this.valorTemperatura = valorTemperatura;
        this.valorNO2 = valorNO2;
        this.valorSO3 = valorSO3;
    }

    // Getters y Setters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public String getId_sensor() { return id_sensor; }
    public void setId_sensor(String id_sensor) { this.id_sensor = id_sensor; }

    public double getValorO3() { return valorO3; }
    public void setValorO3(double valorO3) { this.valorO3 = valorO3; }

    public double getValorTemperatura() { return valorTemperatura; }
    public void setValorTemperatura(double valorTemperatura) { this.valorTemperatura = valorTemperatura; }

    public double getValorNO2() { return valorNO2; }
    public void setValorNO2(double valorNO2) { this.valorNO2 = valorNO2; }

    public double getValorSO3() { return valorSO3; }
    public void setValorSO3(double valorSO3) { this.valorSO3 = valorSO3; }


}

