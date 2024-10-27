package com.example.biometria3a;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.18.157"; // Cambia esto a la URL de tu API de la ip del ordenador

    // Método para verificar usuario
    public static void verificarUsuario(String correo,  Context context,String contrasena, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/verificarUsuario?correo=" + correo + "&contrasena=" + contrasena;

        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener);

        queue.add(stringRequest);
    }

    // Método para agregar un usuario
    public static void agregarUsuario(String correo, Context context, String contrasena, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/agregarUsuario";

       // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                params.put("contrasena", contrasena);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // Método para consultar datos de usuario
    public static void consultarDatosUsuario(String id_usuario, Response.Listener<JSONObject> listener, Context context, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/ConsultarDatosUsuario/" + id_usuario;

        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener);

        queue.add(stringRequest);
    }

    // Método para consultar medida
    public static void consultarMedida(String id_sensor, Response.Listener<JSONObject> listener, Context context, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/ConsultarMedida/" + id_sensor;

       // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener);

        queue.add(stringRequest);
    }
    // Método para agregar una medición
    public static void agregarMedicion(String hora, Context context, double latitud, double longitud, double valorGas, double valorTemperatura, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/agregarMedicion";

        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("hora", hora);
                params.put("latitud", String.valueOf(latitud));
                params.put("longitud", String.valueOf(longitud));
                params.put("valorGas", String.valueOf(valorGas));
                params.put("valorTemperatura", String.valueOf(valorTemperatura));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // Método para consultar si hay alerta
    public static void consultarSiHayAlerta( Context context,String id_sensor, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/ConsultarSiHayAlerta/" + id_sensor;

        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener);

        queue.add(stringRequest);
    }

    // Método para eliminar un usuario
    public static void eliminarUsuario( Context context,String id_usuario, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/EliminarUsuario/" + id_usuario;

       // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener);

        queue.add(stringRequest);
    }

    // Método para consultar todas las tablas de la base de datos
    public static void consultarBaseDeDatos( Context context,Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/ConsultarBaseDeDatos";

       // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    listener.onResponse(jsonResponse);
                } catch (JSONException e) {
                    errorListener.onErrorResponse(new VolleyError(e));
                }
            }
        }, errorListener);

        queue.add(stringRequest);
    }
}
