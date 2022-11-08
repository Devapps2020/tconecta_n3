package com.blm.qiubopay.models.reportes;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class FinancialReportResponse extends QPAY_BaseResponse implements Serializable {

    private FinancialReport[] qpay_object;

    public FinancialReport[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(FinancialReport[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
