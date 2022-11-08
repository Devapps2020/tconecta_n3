package com.blm.qiubopay.models.fiado;

import java.io.Serializable;

public class QPAY_Pago_Parcial_Request implements Serializable {

    private String fiado_mail;
    private String qpay_seed;
    private String fiado_payment_amount;
    private String fiado_debt_id;

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

    public String getFiado_payment_amount() {
        return fiado_payment_amount;
    }

    public void setFiado_payment_amount(String fiado_payment_amount) {
        this.fiado_payment_amount = fiado_payment_amount;
    }

    public String getFiado_debt_id() {
        return fiado_debt_id;
    }

    public void setFiado_debt_id(String fiado_debt_id) {
        this.fiado_debt_id = fiado_debt_id;
    }
}
