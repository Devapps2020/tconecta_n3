package com.blm.qiubopay.models.pidelo;

import java.io.Serializable;

public class FilingProduct implements Serializable {

    private String medida, codigo_medida;
    private int cantiad_caja, cantidad_volumen;
    private double precio, precio_volumen;

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getCodigo_medida() {
        return codigo_medida;
    }

    public void setCodigo_medida(String codigo_medida) {
        this.codigo_medida = codigo_medida;
    }

    public int getCantiad_caja() {
        return cantiad_caja;
    }

    public void setCantiad_caja(int cantiad_caja) {
        this.cantiad_caja = cantiad_caja;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad_volumen() {
        return cantidad_volumen;
    }

    public void setCantidad_volumen(int cantidad_volumen) {
        this.cantidad_volumen = cantidad_volumen;
    }

    public double getPrecio_volumen() {
        return precio_volumen;
    }

    public void setPrecio_volumen(int precio_volumen) {
        this.precio_volumen = precio_volumen;
    }
}
