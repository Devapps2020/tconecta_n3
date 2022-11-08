package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.List;

public class GetAwardsResponse extends QPAY_BaseResponse {

    private List<GetAwardsDTO> qpay_object;

    public GetAwardsResponse(){}

    public List<GetAwardsDTO> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<GetAwardsDTO> qpay_object) {
        this.qpay_object = qpay_object;
    }
}
