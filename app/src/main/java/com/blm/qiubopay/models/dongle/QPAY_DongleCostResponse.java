package com.blm.qiubopay.models.dongle;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_DongleCostResponse extends QPAY_BaseResponse implements Serializable {
    private DongleCost[] qpay_object;

    public DongleCost[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(DongleCost[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_DongleCostResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_DongleCostResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
