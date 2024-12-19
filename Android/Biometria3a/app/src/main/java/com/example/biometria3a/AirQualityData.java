package com.example.biometria3a;


public class AirQualityData {
    private int id_medicion;
    private String id_sensor;
    private String fecha_hora;
    private Ubicacion ubicacion;
    private String tipo_medicion;
    private double valor;

    // 构造函数
    public AirQualityData(double latitud, double longitud, String tipo_medicion, double valor) {
        this.ubicacion = new Ubicacion(latitud, longitud);
        this.tipo_medicion = tipo_medicion;
        this.valor = valor;
    }

    // Getter 和 Setter 方法
    public int getIdMedicion() {
        return id_medicion;
    }

    public void setIdMedicion(int id_medicion) {
        this.id_medicion = id_medicion;
    }

    public String getIdSensor() {
        return id_sensor;
    }

    public void setIdSensor(String id_sensor) {
        this.id_sensor = id_sensor;
    }

    public String getFechaHora() {
        return fecha_hora;
    }

    public void setFechaHora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipoMedicion() {
        return tipo_medicion;
    }

    public void setTipoMedicion(String tipo_medicion) {
        this.tipo_medicion = tipo_medicion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    // 新增方法：获取纬度
    public double getLat() {
        return ubicacion.getLatitud();
    }

    // 新增方法：获取经度
    public double getLng() {
        return ubicacion.getLongitud();
    }

    // 嵌套类 Ubicacion
    public static class Ubicacion {
        private double latitud;
        private double longitud;

        // 构造函数
        public Ubicacion(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        // Getter 和 Setter 方法
        public double getLatitud() {
            return latitud;
        }

        public void setLatitud(double latitud) {
            this.latitud = latitud;
        }

        public double getLongitud() {
            return longitud;
        }

        public void setLongitud(double longitud) {
            this.longitud = longitud;
        }
    }
}
