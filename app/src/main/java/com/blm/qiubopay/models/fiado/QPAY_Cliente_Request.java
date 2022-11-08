package com.blm.qiubopay.models.fiado;

import java.io.Serializable;

public class QPAY_Cliente_Request implements Serializable {


    private String fiado_mail;
    private String qpay_seed;
    private String fiado_name;
    private String fiado_cellphone;

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

    public String getFiado_name() {
        return fiado_name;
    }

    public void setFiado_name(String fiado_name) {
        this.fiado_name = fiado_name;
    }

    public String getFiado_cellphone() {
        return fiado_cellphone;
    }

    public void setFiado_cellphone(String fiado_cellphone) {
        this.fiado_cellphone = fiado_cellphone;
    }
}
