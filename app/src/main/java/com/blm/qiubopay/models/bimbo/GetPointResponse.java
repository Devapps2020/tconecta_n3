package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class GetPointResponse extends QPAY_BaseResponse {

    private GetPointDTO[] qpay_object;

    public GetPointResponse(){}


    public GetPointDTO[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(GetPointDTO[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
