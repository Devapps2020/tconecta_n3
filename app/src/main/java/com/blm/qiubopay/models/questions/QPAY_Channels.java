package com.blm.qiubopay.models.questions;

import java.io.Serializable;

public class QPAY_Channels implements Serializable {

    private String qpay_seed;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

}