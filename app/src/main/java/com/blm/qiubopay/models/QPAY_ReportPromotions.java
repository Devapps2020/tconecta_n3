package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.List;

public class QPAY_ReportPromotions extends QPAY_BaseResponse {

    private List<QPAY_ReportRecarga> qpay_object;

    public List<QPAY_ReportRecarga> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<QPAY_ReportRecarga> qpay_object) {
        this.qpay_object = qpay_object;
    }
}