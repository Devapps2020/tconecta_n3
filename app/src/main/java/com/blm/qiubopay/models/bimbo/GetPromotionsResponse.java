package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.List;

public class GetPromotionsResponse extends QPAY_BaseResponse {

    private List<PromotDTO> qpay_object;

    public List<PromotDTO> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<PromotDTO> qpay_object) {
        this.qpay_object = qpay_object;
    }

}
