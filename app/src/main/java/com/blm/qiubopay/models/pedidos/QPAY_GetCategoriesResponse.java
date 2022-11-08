package com.blm.qiubopay.models.pedidos;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_GetCategoriesResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_GetCategories_Object[] qpay_object;

    public QPAY_GetCategories_Object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_GetCategories_Object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}