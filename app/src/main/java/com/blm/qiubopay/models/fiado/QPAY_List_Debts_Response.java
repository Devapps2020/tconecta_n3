package com.blm.qiubopay.models.fiado;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class QPAY_List_Debts_Response extends QPAY_BaseResponse {

    private QPAY_Fiado[] qpay_object;

    public QPAY_Fiado[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Fiado[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
