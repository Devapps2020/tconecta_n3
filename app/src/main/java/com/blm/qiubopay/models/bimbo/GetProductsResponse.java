package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.List;

public class GetProductsResponse extends QPAY_BaseResponse {

    private List<ProductoDTO> qpay_object;

    public GetProductsResponse(){}

    public List<ProductoDTO> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<ProductoDTO> qpay_object) {
        this.qpay_object = qpay_object;
    }
}
