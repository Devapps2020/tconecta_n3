package com.blm.qiubopay.models.cargos_y_abonos;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_CreditAndDebitTxrResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_CreditAndDebitTxr[] qpay_object;

    public QPAY_CreditAndDebitTxr[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_CreditAndDebitTxr[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_CreditAndDebitTxrResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_CreditAndDebitTxrResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
