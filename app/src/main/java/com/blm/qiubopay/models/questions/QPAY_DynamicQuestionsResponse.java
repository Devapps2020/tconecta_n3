package com.blm.qiubopay.models.questions;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_DynamicQuestionsResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_DynamicQuestions_object[] qpay_object;

    public QPAY_DynamicQuestions_object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_DynamicQuestions_object[] qpay_object) {
        this.qpay_object = qpay_object;
    }

}