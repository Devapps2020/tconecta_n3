package com.blm.qiubopay.models.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_ServiceSaleResponseSecond implements Serializable {
    private String responseAt;
    private String qpay_account_number;
    private String qpay_account_number1;
    private String qpay_account_number2;
    private String qpay_account_number3;
    private String qpay_verification_digit;
    private String qpay_amount;
    private String qpay_transactionId;
    private String qpay_product;
    private String requestId;
    private String rspCode;
    private String rspDescription;
    private String qpay_rspBody;

    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public String getQpay_account_number() {
        return qpay_account_number;
    }

    public void setQpay_account_number(String qpay_account_number) {
        this.qpay_account_number = qpay_account_number;
    }

    public String getQpay_account_number1() {
        return qpay_account_number1;
    }

    public void setQpay_account_number1(String qpay_account_number1) {
        this.qpay_account_number1 = qpay_account_number1;
    }

    public String getQpay_account_number2() {
        return qpay_account_number2;
    }

    public void setQpay_account_number2(String qpay_account_number2) {
        this.qpay_account_number2 = qpay_account_number2;
    }

    public String getQpay_account_number3() {
        return qpay_account_number3;
    }

    public void setQpay_account_number3(String qpay_account_number3) {
        this.qpay_account_number3 = qpay_account_number3;
    }

    public String getQpay_verification_digit() {
        return qpay_verification_digit;
    }

    public void setQpay_verification_digit(String qpay_verification_digit) {
        this.qpay_verification_digit = qpay_verification_digit;
    }

    public String getQpay_amount() {
        return qpay_amount;
    }

    public void setQpay_amount(String qpay_amount) {
        this.qpay_amount = qpay_amount;
    }

    public String getQpay_transactionId() {
        return qpay_transactionId;
    }

    public void setQpay_transactionId(String qpay_transactionId) {
        this.qpay_transactionId = qpay_transactionId;
    }

    public String getQpay_product() {
        return qpay_product;
    }

    public void setQpay_product(String qpay_product) {
        this.qpay_product = qpay_product;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getQpay_rspBody() {
        return qpay_rspBody;
    }

    public void setQpay_rspBody(String qpay_rspBody) {
        this.qpay_rspBody = qpay_rspBody;
    }

    public static class QPAY_ServiceSaleResponseSecondExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_ServiceSaleResponseSecond.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
