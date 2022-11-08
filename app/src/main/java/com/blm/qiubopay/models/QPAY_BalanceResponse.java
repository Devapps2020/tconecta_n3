package com.blm.qiubopay.models;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_BalanceResponse extends QPAY_BaseResponse implements Serializable {

    private Balance[] qpay_object;

    public Balance[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(Balance[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_BalanceResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_BalanceResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
