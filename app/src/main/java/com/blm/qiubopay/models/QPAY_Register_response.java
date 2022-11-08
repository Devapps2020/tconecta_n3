package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_Register_response extends QPAY_BaseResponse implements Serializable {

    private QPAY_Register_object[] qpay_object;

    public QPAY_Register_object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Register_object[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}