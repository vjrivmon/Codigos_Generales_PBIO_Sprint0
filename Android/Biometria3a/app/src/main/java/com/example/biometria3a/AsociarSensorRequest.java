package com.example.biometria3a;

public class AsociarSensorRequest {
    private String correo;
    private String id_sensor;
    private String nombre;
    private String funciona;

    // Constructor
    public AsociarSensorRequest(String correo, String id_sensor, String nombre, String funciona) {
        this.correo = correo;
        this.id_sensor = id_sensor;
        this.nombre = nombre;
        this.funciona = funciona;
    }

    // Getters y setters
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getId_sensor() { return id_sensor; }
    public void setId_sensor(String id_sensor) { this.id_sensor = id_sensor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFunciona() { return funciona; }
    public void setFunciona(String funciona) { this.funciona = funciona; }
}

