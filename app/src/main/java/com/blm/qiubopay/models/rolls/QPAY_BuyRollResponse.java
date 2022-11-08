package com.blm.qiubopay.models.rolls;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_BuyRollResponse extends QPAY_BaseResponse implements Serializable {
    private String createdAt;
    private String responseAt;
    private QPAY_BuyRollInfo[] qpay_object;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public QPAY_BuyRollInfo[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_BuyRollInfo[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_BuyRollResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_BuyRollResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
