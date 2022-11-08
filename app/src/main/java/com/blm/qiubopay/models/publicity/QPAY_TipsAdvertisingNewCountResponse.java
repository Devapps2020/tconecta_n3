package com.blm.qiubopay.models.publicity;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TipsAdvertisingNewCountResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_TipsAdvertisingNewCount[] qpay_object;

    public QPAY_TipsAdvertisingNewCount[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_TipsAdvertisingNewCount[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}