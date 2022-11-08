package com.blm.qiubopay.models.soporte;

import java.io.Serializable;

public class QPAY_ErrorReport implements Serializable {
    private String qpay_seed;
    private String qpay_phone;
    private String qpay_error_id;
    private String qpay_description;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_phone() {
        return qpay_phone;
    }

    public void setQpay_phone(String qpay_phone) {
        this.qpay_phone = qpay_phone;
    }

    public String getQpay_error_id() {
        return qpay_error_id;
    }

    public void setQpay_error_id(String qpay_error_id) {
        this.qpay_error_id = qpay_error_id;
    }

    public String getQpay_description() {
        return qpay_description;
    }

    public void setQpay_description(String qpay_description) {
        this.qpay_description = qpay_description;
    }
}
