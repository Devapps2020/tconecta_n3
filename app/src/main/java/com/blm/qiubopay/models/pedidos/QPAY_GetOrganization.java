package com.blm.qiubopay.models.pedidos;

import java.io.Serializable;

public class QPAY_GetOrganization implements Serializable {

    private String qpay_seed;


    // Getter Methods

    public String getQpay_seed(String qpay_seed) {
        return this.qpay_seed;
    }

    // Setter Methods

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

}