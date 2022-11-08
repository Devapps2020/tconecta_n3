package com.blm.qiubopay.models.pagos_qiubo;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_QiuboPaymentListResponse extends QPAY_BaseResponse implements Serializable {
    private QPAY_QiuboPaymentItem[] qpay_object;

    public QPAY_QiuboPaymentItem[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_QiuboPaymentItem[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_QiuboPaymentListExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_QiuboPaymentListResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
