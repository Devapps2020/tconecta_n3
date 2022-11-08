package com.blm.qiubopay.models.financial_vas;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_FinancialVasGetAmountsResponse extends QPAY_BaseResponse implements Serializable  {

    private QPAY_FinancialVasAmounts[][] qpay_object;

    public QPAY_FinancialVasAmounts[][] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_FinancialVasAmounts[][] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_FinancialVasGetAmountsResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_FinancialVasGetAmountsResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
