package com.blm.qiubopay.models.migration_kineto;

import java.io.Serializable;

public class QPAY_Url implements Serializable {

    private String qpay_url;

    private Boolean qpay_activate;

    public String getQpay_url() {
        return qpay_url;
    }

    public void setQpay_url(String qpay_url) {
        this.qpay_url = qpay_url;
    }

    public Boolean getQpay_activate() {
        return qpay_activate;
    }

    public void setQpay_activate(Boolean qpay_activate) {
        this.qpay_activate = qpay_activate;
    }
}
