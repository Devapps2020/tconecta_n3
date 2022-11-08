package com.blm.qiubopay.models.tae;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_TaeSaleBaseResponse implements Serializable {

    private String responseCode;
    private String responseDescription;
    private QPAY_KinetoTaeSaleResponse dynamicData;

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

    public QPAY_KinetoTaeSaleResponse getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(QPAY_KinetoTaeSaleResponse dynamicData) {
        this.dynamicData = dynamicData;
    }

    public static class QPAY_TaeSaleBaseResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_TaeSaleBaseResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
