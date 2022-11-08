package com.blm.qiubopay.models.swap;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_SwapN3LoginResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_SwapN3Login[] qpay_object;

    public QPAY_SwapN3Login[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_SwapN3Login[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}
