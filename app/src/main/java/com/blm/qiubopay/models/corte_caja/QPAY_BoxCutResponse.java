package com.blm.qiubopay.models.corte_caja;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_BoxCutResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_BoxCutInfo[] qpay_object;

    public QPAY_BoxCutInfo[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_BoxCutInfo[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_BoxCutResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_BoxCutResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
