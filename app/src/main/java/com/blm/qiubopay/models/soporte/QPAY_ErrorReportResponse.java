package com.blm.qiubopay.models.soporte;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_ErrorReportResponse extends QPAY_BaseResponse implements Serializable {
    private QPAY_ErrorFolio[] qpay_object;

    public QPAY_ErrorFolio[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_ErrorFolio[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_ErrorReportResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_ErrorReportResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
