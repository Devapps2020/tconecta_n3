package com.blm.qiubopay.models.proceedings;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;
import java.util.List;

public class QPAY_AllDataRecordResponse extends QPAY_BaseResponse implements Serializable {

        private List<QPAY_AllDataRecord_Object> qpay_object;

    public List<QPAY_AllDataRecord_Object> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<QPAY_AllDataRecord_Object> qpay_object) {
        this.qpay_object = qpay_object;
    }

}