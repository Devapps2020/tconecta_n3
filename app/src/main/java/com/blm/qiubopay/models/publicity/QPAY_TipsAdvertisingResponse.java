package com.blm.qiubopay.models.publicity;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TipsAdvertisingResponse extends QPAY_BaseResponse implements Serializable {

    private  QPAY_TipsAdvertising_object[] qpay_object;

    public QPAY_TipsAdvertising_object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_TipsAdvertising_object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}