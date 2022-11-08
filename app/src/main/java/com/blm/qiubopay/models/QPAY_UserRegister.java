package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_UserRegister implements Serializable {

    private String activationUrl;

    public QPAY_UserRegister() {

    }

    public String getActivationUrl() {
        return activationUrl;
    }

    public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }
}
