package com.blm.qiubopay.models.chambitas.campañas;

import com.blm.qiubopay.utils.Globals;

import java.io.Serializable;

public class QPAY_ActiveCampaignNew implements Serializable {

    private String qpay_seed;
    final private Integer version = Globals.CHAMBITAS_VERSION;


    // Getter Methods

    public String getQpay_seed(String qpay_seed) {
        return this.qpay_seed;
    }

    // Setter Methods

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

}