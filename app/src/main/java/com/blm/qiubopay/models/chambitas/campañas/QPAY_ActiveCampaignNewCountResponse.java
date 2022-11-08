package com.blm.qiubopay.models.chambitas.campañas;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;
import java.util.List;

public class QPAY_ActiveCampaignNewCountResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_ActiveCampaignNewCount[] qpay_object;

    public QPAY_ActiveCampaignNewCount[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_ActiveCampaignNewCount[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}