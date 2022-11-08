package com.blm.qiubopay.models.fiado;

import java.io.Serializable;

public class QPAY_Recibo_Request implements Serializable {

    private String fiado_mail;
    private String fiado_cellphone;
    private String qpay_seed;
    private String fiado_payment_id;

    public String getFiado_mail() {
        return fiado_mail;
    }

    public void setFiado_mail(String fiado_mail) {
        this.fiado_mail = fiado_mail;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getFiado_payment_id() {
        return fiado_payment_id;
    }

    public void setFiado_payment_id(String fiado_payment_id) {
        this.fiado_payment_id = fiado_payment_id;
    }

    public String getFiado_cellphone() {
        return fiado_cellphone;
    }

    public void setFiado_cellphone(String fiado_cellphone) {
        this.fiado_cellphone = fiado_cellphone;
    }
}
