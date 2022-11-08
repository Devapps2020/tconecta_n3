package com.blm.qiubopay.models.migration_kineto;

import java.io.Serializable;

public class QPAY_ConfirmUser implements Serializable {

    private String qpay_seed;
    private String qpay_mail;
    private String qpay_password;
    private Boolean qpay_activate;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_password() {
        return qpay_password;
    }

    public void setQpay_password(String qpay_password) {
        this.qpay_password = qpay_password;
    }

    public Boolean getQpay_activate() {
        return qpay_activate;
    }

    public void setQpay_activate(Boolean qpay_activate) {
        this.qpay_activate = qpay_activate;
    }
}
