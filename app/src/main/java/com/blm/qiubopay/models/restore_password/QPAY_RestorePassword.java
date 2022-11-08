package com.blm.qiubopay.models.restore_password;

import java.io.Serializable;

public class QPAY_RestorePassword implements Serializable {
    private String qpay_mail;
    private String qpay_cellphone;
    private String qpay_merchant_postal_code;
    private String qpay_password;
    private String qpay_new_password;

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_cellphone() {
        return qpay_cellphone;
    }

    public void setQpay_cellphone(String qpay_cellphone) {
        this.qpay_cellphone = qpay_cellphone;
    }

    public String getQpay_merchant_postal_code() {
        return qpay_merchant_postal_code;
    }

    public void setQpay_merchant_postal_code(String qpay_merchant_postal_code) {
        this.qpay_merchant_postal_code = qpay_merchant_postal_code;
    }

    public String getQpay_password() {
        return qpay_password;
    }

    public void setQpay_password(String qpay_password) {
        this.qpay_password = qpay_password;
    }

    public String getQpay_new_password() {
        return qpay_new_password;
    }

    public void setQpay_new_password(String qpay_new_password) {
        this.qpay_new_password = qpay_new_password;
    }
}
