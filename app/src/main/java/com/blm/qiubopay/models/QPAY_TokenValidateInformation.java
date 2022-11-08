package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_TokenValidateInformation implements Serializable {

    private String qpay_seed;
    private String qpay_data;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_data() {
        return qpay_data;
    }

    public void setQpay_data(String qpay_data) {
        this.qpay_data = qpay_data;
    }
}
