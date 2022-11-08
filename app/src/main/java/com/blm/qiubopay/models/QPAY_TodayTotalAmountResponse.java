package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class QPAY_TodayTotalAmountResponse extends QPAY_BaseResponse {

    private Balance[] qpay_object;

    public Balance[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(Balance[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}
