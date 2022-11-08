package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class PedidoResponse extends QPAY_BaseResponse {

    private PedidoDTO[] product_object;

    public PedidoResponse(){}

    public PedidoDTO[] getProduct_object() {
        return product_object;
    }

    public void setProduct_object(PedidoDTO[] product_object) {
        this.product_object = product_object;
    }
}
