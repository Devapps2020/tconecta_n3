package com.blm.qiubopay.models.tae;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_TaeSaleResponseSecond implements Serializable {//extends QPAY_BaseResponse implements Serializable {

    private String qpay_amount;
    private String qpay_carrier;
    private String qpay_mobile_number;
    private String qpay_rspBody;
    private String responseAt;
    private String rspCode;
    private String rspDescription;
    private String transactionId;

    public String getQpay_amount() {
        return qpay_amount;
    }

    public void setQpay_amount(String qpay_amount) {
        this.qpay_amount = qpay_amount;
    }

    public String getQpay_carrier() {
        return qpay_carrier;
    }

    public void setQpay_carrier(String qpay_carrier) {
        this.qpay_carrier = qpay_carrier;
    }

    public String getQpay_mobile_number() {
        return qpay_mobile_number;
    }

    public void setQpay_mobile_number(String qpay_mobile_number) {
        this.qpay_mobile_number = qpay_mobile_number;
    }

    public String getQpay_rspBody() {
        return qpay_rspBody;
    }

    public void setQpay_rspBody(String qpay_rspBody) {
        this.qpay_rspBody = qpay_rspBody;
    }

    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDescription() {
        return rspDescription;
    }

    public void setRspDescription(String rspDescription) {
        this.rspDescription = rspDescription;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public static class QPAY_TaeSaleResponseSecondExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_TaeSaleResponseSecond.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
