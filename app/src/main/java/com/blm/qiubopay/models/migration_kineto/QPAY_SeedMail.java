package com.blm.qiubopay.models.migration_kineto;

import java.io.Serializable;

public class QPAY_SeedMail implements Serializable {

    private String qpay_seed;
    private String qpay_mail;
    private String qpay_mail_secondary;
    private String qpay_password;

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

    public String getQpay_mail_secondary() {
        return qpay_mail_secondary;
    }

    public void setQpay_mail_secondary(String qpay_mail_secondary) {
        this.qpay_mail_secondary = qpay_mail_secondary;
    }

    public String getQpay_password() {
        return qpay_password;
    }

    public void setQpay_password(String qpay_password) {
        this.qpay_password = qpay_password;
    }
}