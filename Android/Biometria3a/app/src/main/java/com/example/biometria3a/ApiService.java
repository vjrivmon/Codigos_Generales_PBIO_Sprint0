package com.example.biometria3a;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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


    @GET("obtenerSensorPorCorreo/{correo}")
    Call<SensorResponse> obtenerSensorPorCorreo(@Path("correo") String correo);
    @POST("asociar_dispositivo")
    Call<Void> asociarSensorAUsuario(@Body AsociarSensorRequest request);

    @GET("senusu/{id_usuario}")
    Call<List<Sensor>> getSensorsByUser(@Path("id_usuario") int userId);



    @PUT("usuarios/{id_usuario}/contrase%C3%B1a")
    Call<Void> updatePassword(@Path("id_usuario") int userId, @Body HashMap<String, String> body);


    @POST("recuperarContrasena")
    Call<Void> recuperarContrasena(@Body HashMap<String, String> body);


    @GET("/mediciones") // 根据后端的 API 路径，确保和后端定义一致
    Call<List<Medicion2>> getMediciones();

    @GET("/mediciones")
    Call<List<Medicion2>> getMedicionesByDate(@Query("fecha") String fecha);


    @GET("/mediciones/{id_medicion}")
    Call<List<Medicion2>> getMedicionesById(@Path("id_medicion") int idMedicion);
    @GET("mediciones/{id}")
    Call<Medicion2> getMedicionById(@Path("id") int id);

    @GET("/") // 根路径
    Call<List<Medicion>> getAllMediciones();

}




