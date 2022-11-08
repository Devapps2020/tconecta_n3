package com.blm.qiubopay.models.token;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TokenValidationResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_TokenData[] qpay_object;

    public QPAY_TokenData[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_TokenData[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}
