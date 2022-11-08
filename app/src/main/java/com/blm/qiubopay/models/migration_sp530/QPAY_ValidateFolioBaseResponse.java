package com.blm.qiubopay.models.migration_sp530;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_ValidateFolioBaseResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_ValidateFolioResponse[] qpay_object;

    public QPAY_ValidateFolioResponse[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_ValidateFolioResponse[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}
