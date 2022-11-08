package com.blm.qiubopay.models.soporte;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_CommonErrorResponse extends QPAY_BaseResponse implements Serializable {
    private QPAY_CommonError[] qpay_object;

    public QPAY_CommonError[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_CommonError[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_CommonErrorResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_CommonErrorResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
