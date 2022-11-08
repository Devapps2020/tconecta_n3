package com.blm.qiubopay.models;

import java.io.Serializable;

public class LinkCode implements Serializable {

    private String qpay_link_code;

    public String getQpay_link_code() {
        return qpay_link_code;
    }

    public void setQpay_link_code(String qpay_link_code) {
        this.qpay_link_code = qpay_link_code;
    }
}
