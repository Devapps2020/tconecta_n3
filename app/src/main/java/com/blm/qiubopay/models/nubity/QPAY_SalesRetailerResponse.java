package com.blm.qiubopay.models.nubity;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_SalesRetailerResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_SalesRetailer_Object[] qpay_object;

    public QPAY_SalesRetailer_Object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_SalesRetailer_Object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}