package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.List;

public class ReedemPointsResponse extends QPAY_BaseResponse {

    private List<ReedemPointsDTO> qpay_object;

    public ReedemPointsResponse(){}

    public List<ReedemPointsDTO> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<ReedemPointsDTO> qpay_object) {
        this.qpay_object = qpay_object;
    }
}
