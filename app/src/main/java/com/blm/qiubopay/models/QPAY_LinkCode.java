package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_LinkCode implements Serializable {

    private String qpay_seed;
    private String qpay_link_code;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_link_code() {
        return qpay_link_code;
    }

    public void setQpay_link_code(String qpay_link_code) {
        this.qpay_link_code = qpay_link_code;
    }

}
