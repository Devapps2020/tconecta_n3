package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class SellerUserResponse extends QPAY_BaseResponse {

    private SellerUserDTO[] qpay_object;

    public SellerUserResponse() {

    }

    public SellerUserDTO[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(SellerUserDTO[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
