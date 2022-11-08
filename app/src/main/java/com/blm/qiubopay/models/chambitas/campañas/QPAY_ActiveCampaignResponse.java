package com.blm.qiubopay.models.chambitas.campañas;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_ActiveCampaignResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_ActiveCampaign_Object[] qpay_object;

    public QPAY_ActiveCampaign_Object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_ActiveCampaign_Object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}