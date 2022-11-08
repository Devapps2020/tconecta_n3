package com.blm.qiubopay.models.tae;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TaeSaleResponseFirst extends QPAY_BaseResponse implements Serializable {

    private QPAY_KinetoTaeSaleResponse[] qpay_object;

    public QPAY_KinetoTaeSaleResponse[] getQpay_object() {
        return qpay_object;
    }

    private String createdAt;

    public void setQpay_object(QPAY_KinetoTaeSaleResponse[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_TaeSaleResponseFirstExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_TaeSaleResponseFirst.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
