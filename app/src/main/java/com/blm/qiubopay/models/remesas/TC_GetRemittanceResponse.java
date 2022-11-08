package com.blm.qiubopay.models.remesas;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class TC_GetRemittanceResponse extends QPAY_BaseResponse implements Serializable {
    private TC_RemittanceTrx[] qpay_object;

    public TC_RemittanceTrx[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(TC_RemittanceTrx[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class TC_GetRemittanceResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(TC_GetRemittanceResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
