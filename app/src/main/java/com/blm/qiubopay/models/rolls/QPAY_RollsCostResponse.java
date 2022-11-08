package com.blm.qiubopay.models.rolls;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_RollsCostResponse extends QPAY_BaseResponse implements Serializable {
    private QPAY_Roll[] qpay_object;

    public QPAY_Roll[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Roll[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_RollsCostResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_RollsCostResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
