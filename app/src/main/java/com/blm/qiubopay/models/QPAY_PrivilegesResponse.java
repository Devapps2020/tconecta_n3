package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_PrivilegesResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_Privileges[] qpay_object;

    public QPAY_Privileges[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Privileges[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
