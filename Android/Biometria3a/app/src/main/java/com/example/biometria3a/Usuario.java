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

    public Usuario(int id_usuario,String nombre, String telefono, String correo, String contrasena) {
       this.id_usuario=id_usuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasena = contrasena;
    }


    // C
    // Constructor sin parámetros (vacío)
    public Usuario() {
        // Puedes dejarlo vacío o inicializar valores predeterminados si lo deseas
    }
    // Getters y Setters
    // Getters y setters
    public int getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(int id_usuario) {
        this.id_usuario=id_usuario;
    }

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String correo) {
        this.nombre = nombre;
    }
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
