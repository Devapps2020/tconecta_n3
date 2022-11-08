package com.blm.qiubopay.models.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_ServiceSaleBaseResponse implements Serializable {

    private String responseCode;
    private String responseDescription;
    private ServicePayment dynamicData;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public ServicePayment getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(ServicePayment dynamicData) {
        this.dynamicData = dynamicData;
    }

    public static class QPAY_ServiceSaleBaseResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_ServiceSaleBaseResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
