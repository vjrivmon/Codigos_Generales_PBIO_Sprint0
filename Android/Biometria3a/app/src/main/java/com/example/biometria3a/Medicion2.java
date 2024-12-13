package com.example.biometria3a;



import com.google.gson.annotations.SerializedName;

public class Medicion2 {
        private int id_medicion;
        private String fecha_hora;
        private String tipo;
        private double valor;
        private Ubicacion ubicacion;

        // 默认构造函数
        public Medicion2() {}

        // 新增匹配的构造函数
        public Medicion2(int id_medicion, String fecha_hora, String tipo, double valor) {
                this.id_medicion = id_medicion;
                this.fecha_hora = fecha_hora;
                this.tipo = tipo;
                this.valor = valor;
        }

        // Getter 和 Setter 方法
        public int getId_medicion() {
                return id_medicion;
        }

        public void setId_medicion(int id_medicion) {
                this.id_medicion = id_medicion;
        }

        public String getFecha_hora() {
                return fecha_hora;
        }

        public void setFecha_hora(String fecha_hora) {
                this.fecha_hora = fecha_hora;
        }

        public String getTipo() {
                return tipo;
        }

        public void setTipo(String tipo) {
                this.tipo = tipo;
        }

        public double getValor() {
                return valor;
        }

        public void setValor(double valor) {
                this.valor = valor;
        }

        public Ubicacion getUbicacion() {
                return ubicacion;
        }

        public void setUbicacion(Ubicacion ubicacion) {
                this.ubicacion = ubicacion;
        }

        @Override
        public String toString() {
                return "Medicion2{" +
                        "id_medicion=" + id_medicion +
                        ", fecha_hora='" + fecha_hora + '\'' +
                        ", tipo='" + tipo + '\'' +
                        ", valor=" + valor +
                        ", ubicacion=" + ubicacion +
                        '}';
        }
}
