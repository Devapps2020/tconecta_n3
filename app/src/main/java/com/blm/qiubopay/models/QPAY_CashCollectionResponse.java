package com.blm.qiubopay.models;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.services.ServicePayment;

import java.io.Serializable;

public class QPAY_CashCollectionResponse extends QPAY_BaseResponse implements Serializable {

    private ServicePayment[] qpay_object;

    public ServicePayment[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(ServicePayment[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_CashCollectionResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_CashCollectionResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}
