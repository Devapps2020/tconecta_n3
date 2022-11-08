package com.blm.qiubopay.models.product;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_EquipmentCostResponse extends QPAY_BaseResponse implements Serializable {

    private EquipmentCost[] qpay_object;

    public EquipmentCost[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(EquipmentCost[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
