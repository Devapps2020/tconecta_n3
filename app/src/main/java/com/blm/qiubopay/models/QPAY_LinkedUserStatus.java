package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_LinkedUserStatus implements Serializable {

    private String qpay_seed;
    private String qpay_link_status;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_link_status() {
        return qpay_link_status;
    }

    public void setQpay_link_status(String qpay_link_status) {
        this.qpay_link_status = qpay_link_status;
    }
}
