package com.blm.qiubopay.models.pidelo;

import java.io.Serializable;

public class LineItem implements Serializable {

    private int cantidad;
    private String distribuidor;
    private String identidad_empresa;
    private double monto_reserva;
    private Product producto;

    public LineItem(int cantidad, String distribuidor, String identidad_empresa, double monto_reserva, Product producto) {
        this.cantidad = cantidad;
        this.distribuidor = distribuidor;
        this.identidad_empresa = identidad_empresa;
        this.monto_reserva = monto_reserva;
        this.producto = producto;
    }

    public double getMonto_reserva() {
        return monto_reserva;
    }

    public void setMonto_reserva(double monto_reserva) {
        this.monto_reserva = monto_reserva;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getIdentidad_empresa() {
        return identidad_empresa;
    }

    public void setIdentidad_empresa(String identidad_empresa) {
        this.identidad_empresa = identidad_empresa;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(String distribuidor) {
        this.distribuidor = distribuidor;
    }

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }
}
