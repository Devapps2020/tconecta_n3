package com.blm.qiubopay.models.chambitas.retos;

import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Object;

import java.io.Serializable;

public class QPAY_ChallengeTypes implements Serializable {

    private QPAY_ChallengeTypes_Object[] qpay_object;

    public QPAY_ChallengeTypes_Object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_ChallengeTypes_Object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}