package com.blm.qiubopay.models.proceedings;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;
import java.util.List;

public class QPAY_UserFcIdentificationResponse extends QPAY_BaseResponse implements Serializable {

    private List<List<QPAY_UserFcIdentification_Object>> qpay_object;

    public List<List<QPAY_UserFcIdentification_Object>> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<List<QPAY_UserFcIdentification_Object>> qpay_object) {
        this.qpay_object = qpay_object;
    }

}