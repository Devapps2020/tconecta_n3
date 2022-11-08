package com.blm.qiubopay.models;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class QPAY_VisaResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_VisaEmvResponse[] qpay_object;

    public ArrayList<QPAY_VisaEmvResponse> cancelaciones;

    public ArrayList<QPAY_VisaEmvResponse> ventas;

    public QPAY_VisaEmvResponse[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_VisaEmvResponse[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_VisaResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_VisaResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
