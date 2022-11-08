package com.blm.qiubopay.models.pidelo;

public class CancelOrder {

    private String qpay_seed, identidad_empresa, motivo = "", orden;

    public CancelOrder(String identidad_empresa, String motivo, String pedido) {
        this.identidad_empresa = identidad_empresa;
        this.motivo = motivo;
        this.orden = pedido;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getIdentidad_empresa() {
        return identidad_empresa;
    }

    public void setIdentidad_empresa(String identidad_empresa) {
        this.identidad_empresa = identidad_empresa;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPedido() {
        return orden;
    }

    public void setPedido(String pedido) {
        this.orden = pedido;
    }
}
