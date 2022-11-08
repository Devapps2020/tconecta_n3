package com.blm.qiubopay.models.bank;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_BankResponse extends QPAY_BaseResponse implements Serializable {
    private QPAY_Bank[] qpay_object;

    public QPAY_Bank[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Bank[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_BankResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_BankResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
