package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_CommissionReportResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_CommissionReport_Object[] qpay_object;

    public QPAY_CommissionReport_Object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_CommissionReport_Object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}