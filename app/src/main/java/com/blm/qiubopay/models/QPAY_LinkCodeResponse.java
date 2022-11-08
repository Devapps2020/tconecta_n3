package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_LinkCodeResponse extends QPAY_BaseResponse implements Serializable {

    private LinkCode[] qpay_object;

    public LinkCode[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(LinkCode[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
