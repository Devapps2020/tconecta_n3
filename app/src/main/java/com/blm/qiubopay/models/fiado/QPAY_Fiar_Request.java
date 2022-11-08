package com.blm.qiubopay.models.fiado;

import java.io.Serializable;

public class QPAY_Fiar_Request implements Serializable {

    private String fiado_mail;
    private String qpay_seed;
    private String fiado_debt_amount;
    private String fiado_debt_detail;

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

    public String getFiado_debt_amount() {
        return fiado_debt_amount;
    }

    public void setFiado_debt_amount(String fiado_debt_amount) {
        this.fiado_debt_amount = fiado_debt_amount;
    }

    public String getFiado_debt_detail() {
        return fiado_debt_detail;
    }

    public void setFiado_debt_detail(String fiado_debt_detail) {
        this.fiado_debt_detail = fiado_debt_detail;
    }
}
