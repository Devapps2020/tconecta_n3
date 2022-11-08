package com.blm.qiubopay.models.migration_kineto;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_ConfirmMigrationResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_Url[] qpay_object;

    public QPAY_Url[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Url[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}