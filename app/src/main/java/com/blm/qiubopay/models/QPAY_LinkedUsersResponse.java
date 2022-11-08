package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_LinkedUsersResponse extends QPAY_BaseResponse implements Serializable {

    private LinkedUser[] qpay_object;

    public LinkedUser[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(LinkedUser[] qpay_object) {
        this.qpay_object = qpay_object;
    }


}
