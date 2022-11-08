package com.blm.qiubopay.models.financial_vas;

import com.blm.qiubopay.models.QPAY_FinancialVasApprove;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_FinancialVasApproveResponse extends QPAY_BaseResponse implements Serializable {

    private QPAY_FinancialVasApprove[] qpay_object;

    public QPAY_FinancialVasApprove[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_FinancialVasApprove[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public static class QPAY_FinancialVasResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_FinancialVasApproveResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
