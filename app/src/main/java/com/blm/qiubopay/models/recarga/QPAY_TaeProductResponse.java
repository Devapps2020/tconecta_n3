package com.blm.qiubopay.models.recarga;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TaeProductResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_TaeProduct[] qpay_object;

    public QPAY_TaeProduct[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_TaeProduct[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
