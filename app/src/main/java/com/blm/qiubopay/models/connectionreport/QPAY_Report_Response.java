package com.blm.qiubopay.models.connectionreport;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.fiado.QPAY_Cliente;

public class QPAY_Report_Response extends QPAY_BaseResponse {

    private QPAY_Cliente[] qpay_object;

    public QPAY_Cliente[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Cliente[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
