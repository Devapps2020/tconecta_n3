package com.blm.qiubopay.models.pidelo;

import java.util.List;

public class NewOrder {

    private String qpay_seed;
    private String identidad_empresa, moneda, distributor;
    private double total, reserva_pedido;
    private List<LineItem> detalle;

    public NewOrder(String distributor, String identidad_empresa, String moneda, double total_pedido, double monto_reserva, List<LineItem> detalle) {
        this.identidad_empresa = identidad_empresa;
        this.distributor = distributor;
        this.moneda = moneda;
        this.total = total_pedido;
        this.reserva_pedido = monto_reserva;
        this.detalle = detalle;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getIdentidad_empresa() {
        return identidad_empresa;
    }

    public void setIdentidad_empresa(String identidad_empresa) {
        this.identidad_empresa = identidad_empresa;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public double getTotal_pedido() {
        return total;
    }

    public void setTotal_pedido(double total_pedido) {
        this.total = total_pedido;
    }

    public double getMonto_reserva() {
        return reserva_pedido;
    }

    public void setMonto_reserva(double monto_reserva) {
        this.reserva_pedido = monto_reserva;
    }

    public List<LineItem> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<LineItem> detalle) {
        this.detalle = detalle;
    }
}
