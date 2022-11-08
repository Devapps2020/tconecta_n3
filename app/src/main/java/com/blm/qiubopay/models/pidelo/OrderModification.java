package com.blm.qiubopay.models.pidelo;

public class OrderModification {

    private String qpay_seed, orden, identidad_empresa;
    private boolean acept_order;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public boolean isAcept_order() {
        return acept_order;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public boolean getAcept_order() {
        return acept_order;
    }

    public void setAcept_order(boolean acept_order) {
        this.acept_order = acept_order;
    }

    public String getIdentidad_empresa() {
        return identidad_empresa;
    }

    public void setIdentidad_empresa(String identidad_empresa) {
        this.identidad_empresa = identidad_empresa;
    }
}
