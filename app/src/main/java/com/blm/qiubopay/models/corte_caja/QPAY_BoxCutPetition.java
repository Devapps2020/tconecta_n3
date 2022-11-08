package com.blm.qiubopay.models.corte_caja;

import java.io.Serializable;

public class QPAY_BoxCutPetition implements Serializable {
    private String qpay_seed;
    private String qpay_initial_hour;

    public QPAY_BoxCutPetition(){
        this.qpay_initial_hour = null;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_initial_hour() {
        return qpay_initial_hour;
    }

    public void setQpay_initial_hour(String qpay_initial_hour) {
        this.qpay_initial_hour = qpay_initial_hour;
    }
}
