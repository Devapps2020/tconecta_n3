package com.blm.qiubopay.models.pidelo;

public class OrderPayment {

    private String qpay_seed, tipo_pago, orden, identidad_empresa, referencia;
    private double total;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getIdentidad_empresa() {
        return identidad_empresa;
    }

    public void setIdentidad_empresa(String identidad_empresa) {
        this.identidad_empresa = identidad_empresa;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
