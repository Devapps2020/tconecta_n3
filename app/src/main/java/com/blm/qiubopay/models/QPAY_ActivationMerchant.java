package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_ActivationMerchant implements Serializable {
    private String qpay_seed;
    private String qpay_security_code;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_security_code() {
        return qpay_security_code;
    }

    public void setQpay_security_code(String qpay_security_code) {
        this.qpay_security_code = qpay_security_code;
    }
}
