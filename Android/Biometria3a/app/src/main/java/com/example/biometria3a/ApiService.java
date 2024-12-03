package com.example.biometria3a;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    // Método para registrar un nuevo usuario
    @POST("usuarios")
    Call<Void> registerUser(@Body Usuario user);  // Enviamos un objeto User

    // Método para consultar los datos de un usuario por correo y contraseña
    @GET("usuarios")
    Call<Usuario> getUserByCredentials(@Query("correo") String email, @Query("contrasena") String password);

    @POST("usuarios/verificar")
    Call<JsonObject> loginUser(@Body HashMap<String, String> credentials);


    @POST("mediciones")
    Call<Void> enviarMedicion(@Body Medicion medicion);

    @GET("usuarios/{id}")
    Call<List<Usuario>> getUserById(@Path("id") int userId);


    @PUT("usuarios/{id}")
    Call<Void> updateUser(@Path("id") int userId, @Body Usuario usuario);




    @GET("/mediciones/{id_sensor}")
    Call<List<Medicion2>> getMedicionesBySensor(@Path("id_sensor") String idSensor);
    @PUT("usuarios/{id}/cambiarContrasena")
    Call<Void> changePassword(@Path("id") int userId, @Body HashMap<String, String> request);

     /* @PUT("path/to/endpoint")
      Call<Void> changePassword(@Field("userId") int userId, @FieldMap HashMap<String, String> params);

      */



        // Aquí asumo que tienes una ruta para actualizar la contraseña
        @PUT("usuarios/actualizarContrasena")
        Call<Void> updatePassword(@Body HashMap<String, String> params);

}




