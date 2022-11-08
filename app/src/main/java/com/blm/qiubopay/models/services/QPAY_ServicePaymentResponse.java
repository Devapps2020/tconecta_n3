package com.blm.qiubopay.models.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_ServicePaymentResponse extends QPAY_BaseResponse implements Serializable {

    private String createdAt;

    private ServicePayment[] qpay_object;

    public ServicePayment[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(ServicePayment[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_ServicePaymentResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_ServicePaymentResponse.class);
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
