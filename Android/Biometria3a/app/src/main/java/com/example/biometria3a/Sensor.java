package com.example.biometria3a;


public class Sensor {

    private String id_sensor;
    private String nombre;
    private boolean funciona;

    // Constructor
    public Sensor(String id_sensor, String nombre, boolean funciona) {
        this.id_sensor = id_sensor;
        this.nombre = nombre;
        this.funciona = funciona;
    }

    // Getters y setters
    public String getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(String id_sensor) {
        this.id_sensor = id_sensor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isFunciona() {
        return funciona;
    }

    public void setFunciona(boolean funciona) {
        this.funciona = funciona;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id_sensor='" + id_sensor + '\'' +
                ", nombre='" + nombre + '\'' +
                ", funciona=" + funciona +
                '}';
    }
}

