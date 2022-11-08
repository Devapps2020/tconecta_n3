package com.blm.qiubopay.models.operative.restaurant;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_SaveTipResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_SaveTip[] qpay_object;

    public QPAY_SaveTip[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_SaveTip[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
