package com.blm.qiubopay.models.apuestas;

import java.io.Serializable;

public class QPAY_GetBetDetailsPetition implements Serializable {

    private String qpay_seed;
    private String qpay_ticket_number;
    private String qpay_code;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_ticket_number() {
        return qpay_ticket_number;
    }

    public void setQpay_ticket_number(String qpay_ticket_number) {
        this.qpay_ticket_number = qpay_ticket_number;
    }

    public String getQpay_code() {
        return qpay_code;
    }

    public void setQpay_code(String qpay_code) {
        this.qpay_code = qpay_code;
    }
}
