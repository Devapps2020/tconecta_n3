package com.blm.qiubopay.models.pidelo;

public class LineItemOrder {

    private int cantidad;
    private Product producto;

    public LineItemOrder(int cantidad, Product producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }
}
