package com.blm.qiubopay.models.fiado;

import java.io.Serializable;

public class QPAY_List_Debts_Request implements Serializable {

    private String qpay_seed;
    private String fiado_mail;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getFiado_mail() {
        return fiado_mail;
    }

    public void setFiado_mail(String fiado_mail) {
        this.fiado_mail = fiado_mail;
    }
    
}
