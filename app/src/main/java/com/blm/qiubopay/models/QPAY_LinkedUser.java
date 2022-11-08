package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_LinkedUser implements Serializable {

    private String qpay_seed;
    private Integer qpay_id;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public Integer getQpay_id() {
        return qpay_id;
    }

    public void setQpay_id(Integer qpay_id) {
        this.qpay_id = qpay_id;
    }
}
