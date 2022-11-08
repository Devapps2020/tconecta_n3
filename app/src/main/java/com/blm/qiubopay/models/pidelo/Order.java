package com.blm.qiubopay.models.pidelo;

import java.util.List;

public class Order {

    private String qpay_seed, orden, fecha_entrega, fecha_entregado, estado, distribuidor, identidad_empresa, telefono, direccion, observaciones;
    private CreationDate fecha_creacion;
    private double total, saldo_pendiente, reserva_pedido;
    private List<LineItemOrder> detalle;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public void setReserva_pedido(double reserva_pedido) {
        this.reserva_pedido = reserva_pedido;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String getFecha_entregado() {
        return fecha_entregado;
    }

    public void setFecha_entregado(String fecha_entregado) {
        this.fecha_entregado = fecha_entregado;
    }

    public CreationDate getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(CreationDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(String distribuidor) {
        this.distribuidor = distribuidor;
    }

    public String getIdentidad_empresa() {
        return identidad_empresa;
    }

    public void setIdentidad_empresa(String identidad_empresa) {
        this.identidad_empresa = identidad_empresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getTotal() {
        return (double)Math.round(total * 100) / 100;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSaldo_pendiente() {
        return (double)Math.round(saldo_pendiente * 100) / 100;
    }

    public void setSaldo_pendiente(double saldo_pendiente) {
        this.saldo_pendiente = saldo_pendiente;
    }

    public double getReserva_pedido() {
        return reserva_pedido;
    }

    public void setReserva_pedido(int reserva_pedido) {
        this.reserva_pedido = reserva_pedido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<LineItemOrder> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<LineItemOrder> detalle) {
        this.detalle = detalle;
    }
}
