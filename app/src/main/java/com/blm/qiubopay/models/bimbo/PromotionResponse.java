package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.List;

public class PromotionResponse extends QPAY_BaseResponse {

    private List<PromotionDTO> qpay_object;

    public PromotionResponse() {

    }

    public List<PromotionDTO> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<PromotionDTO> qpay_object) {
        this.qpay_object = qpay_object;
    }
}
