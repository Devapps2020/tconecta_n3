package com.blm.qiubopay.models.apuestas;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_GetUrlResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_AdUrl[] qpay_object;

    public QPAY_AdUrl[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_AdUrl[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_GetUrlResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_GetUrlResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}