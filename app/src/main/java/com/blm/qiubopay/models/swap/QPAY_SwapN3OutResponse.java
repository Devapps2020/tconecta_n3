package com.blm.qiubopay.models.swap;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_SwapN3OutResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_SwapN3Out[] qpay_object;

    public QPAY_SwapN3Out[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_SwapN3Out[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}
