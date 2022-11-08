package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;

public class QPAY_CampaignDone implements Serializable {

    private String qpay_seed;
    private String min_date;

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public String getMin_date() {
        return min_date;
    }

    public void setMin_date(String min_date) {
        this.min_date = min_date;
    }
}