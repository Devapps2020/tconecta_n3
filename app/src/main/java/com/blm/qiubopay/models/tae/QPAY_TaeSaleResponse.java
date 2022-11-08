package com.blm.qiubopay.models.tae;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TaeSaleResponse extends QPAY_BaseResponse implements Serializable {

    private TaeSale[] qpay_object;

    public TaeSale[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(TaeSale[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_TaeSaleResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_TaeSaleResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
