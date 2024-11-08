package com.example.biometria3a;

public class Medicion {
    private String hora;
    private double latitud;
    private double longitud;
    private int id_sensor;
    private double valorGas;
    private double valorTemperatura;

    // Constructor
    public Medicion(String hora, double latitud, double longitud, int id_sensor, double valorGas, double valorTemperatura) {
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.id_sensor = id_sensor;
        this.valorGas = valorGas;
        this.valorTemperatura = valorTemperatura;
    }

    // Getters y Setters
    public String getHora() { return hora; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public int getId_sensor() { return id_sensor; }
    public double getValorGas() { return valorGas; }
    public double getValorTemperatura() { return valorTemperatura; }

    public void setHora(String hora) { this.hora = hora; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public void setId_sensor(int id_sensor) { this.id_sensor = id_sensor; }
    public void setValorGas(double valorGas) { this.valorGas = valorGas; }
    public void setValorTemperatura(double valorTemperatura) { this.valorTemperatura = valorTemperatura; }
}
