package com.example.biometria3a;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Método para registrar un nuevo usuario
    @POST("usuarios")
    Call<Void> registerUser(@Body Usuario user);  // Enviamos un objeto User

    // Método para consultar los datos de un usuario por correo y contraseña
    @GET("usuarios")
    Call<Usuario> getUserByCredentials(@Query("correo") String email, @Query("contrasena") String password);
}

