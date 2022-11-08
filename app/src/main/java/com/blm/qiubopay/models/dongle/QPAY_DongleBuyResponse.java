package com.blm.qiubopay.models.dongle;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_DongleBuyResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_DongleBuyBaseResponse[] qpay_object;

    private String createdAt;
    private String responseAt;


    public QPAY_DongleBuyBaseResponse[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_DongleBuyBaseResponse[] qpay_object) {
        this.qpay_object = qpay_object;
    }

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

    public static class QPAY_DongleBuyResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_DongleBuyResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
