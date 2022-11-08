package com.blm.qiubopay.models.pedidos;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;
import java.util.List;

public class QPAY_GetOrderResponse extends QPAY_BaseResponse implements Serializable {

    private List<QPAY_GetInventory_Object>[] qpay_object;

    public List<QPAY_GetInventory_Object>[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<QPAY_GetInventory_Object>[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}