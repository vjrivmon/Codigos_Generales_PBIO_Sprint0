package com.example.biometria3a;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String telefono;
    private String correo;
    private String contrasena;

    // Constructor
    public Usuario(String nombre, String telefono, String correo, String contrasena) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasena = contrasena;
    }
    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
  /*  @Override
    public String toString() {
        return "Usuario{correo='" + correo + "', contrasena='" + contrasena + "'}";
    }*/


    @Override
    public String toString() {
        return "Usuario{nombre='"+ nombre + "', telefono='" + telefono + "',  correo='" + correo +"', contrasena='" + contrasena + "'}";

    }
}
