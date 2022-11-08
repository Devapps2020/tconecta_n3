package com.blm.qiubopay.models.apuestas;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_GetBetDetailsResponse extends QPAY_BaseResponse implements Serializable {

    private FolioDetail[] qpay_object;

    public FolioDetail[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(FolioDetail[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_GetBetDetailsResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_GetBetDetailsResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}
