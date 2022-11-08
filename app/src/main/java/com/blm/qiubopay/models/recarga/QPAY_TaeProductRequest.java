package com.blm.qiubopay.models.recarga;

import java.io.Serializable;

public class QPAY_TaeProductRequest implements Serializable {

    private String qpay_seed;
    private String qpay_carrier;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_carrier() {
        return qpay_carrier;
    }

    public void setQpay_carrier(String qpay_carrier) {
        this.qpay_carrier = qpay_carrier;
    }
}
