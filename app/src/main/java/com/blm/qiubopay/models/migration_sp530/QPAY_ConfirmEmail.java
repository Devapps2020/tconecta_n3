package com.blm.qiubopay.models.migration_sp530;

import java.io.Serializable;

public class QPAY_ConfirmEmail implements Serializable {

    private String qpay_seed;
    private String qpay_mail;
    private String qpay_new_mail;

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

    public String getQpay_new_mail() {
        return qpay_new_mail;
    }

    public void setQpay_new_mail(String qpay_new_mail) {
        this.qpay_new_mail = qpay_new_mail;
    }

}
