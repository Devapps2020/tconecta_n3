package com.blm.qiubopay.models.tae;

import com.blm.qiubopay.models.DynamicDataSaleResponse;

import java.io.Serializable;

public class TaeSale implements Serializable {
    private String qpay_dateTime;
    private String qpay_transaction_id;
    private String qpay_request_id;
    private String qpay_amount;
    private String qpay_product;
    private String qpay_mobile_number;
    private String qpay_providerAuth;

    private String createdAt;
    private String responseAt;
    private String qpay_carrier;
    private String transactionId;
    private String rspCode;
    private String rspDescription;
    private String qpay_rspBody;
    private String qpay_administrator_id;

    public DynamicDataSaleResponse dynamicData;

    public DynamicDataSaleResponse getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicDataSaleResponse dynamicData) {
        this.dynamicData = dynamicData;
    }

    public String getQpay_dateTime() {
        return qpay_dateTime;
    }

    public void setQpay_dateTime(String qpay_dateTime) {
        this.qpay_dateTime = qpay_dateTime;
    }

    public String getQpay_transaction_id() {
        return qpay_transaction_id;
    }

    public void setQpay_transaction_id(String qpay_transaction_id) {
        this.qpay_transaction_id = qpay_transaction_id;
    }

    public String getQpay_request_id() {
        return qpay_request_id;
    }

    public void setQpay_request_id(String qpay_request_id) {
        this.qpay_request_id = qpay_request_id;
    }

    public String getQpay_amount() {
        return qpay_amount;
    }

    public void setQpay_amount(String qpay_amount) {
        this.qpay_amount = qpay_amount;
    }

    public String getQpay_product() {
        return qpay_product;
    }

    public void setQpay_product(String qpay_product) {
        this.qpay_product = qpay_product;
    }

    public String getQpay_mobile_number() {
        return qpay_mobile_number;
    }

    public void setQpay_mobile_number(String qpay_mobile_number) {
        this.qpay_mobile_number = qpay_mobile_number;
    }

    public String getQpay_providerAuth() {
        return qpay_providerAuth;
    }

    public void setQpay_providerAuth(String qpay_providerAuth) {
        this.qpay_providerAuth = qpay_providerAuth;
    }


    //**************


    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public String getQpay_carrier() {
        return qpay_carrier;
    }

    public void setQpay_carrier(String qpay_carrier) {
        this.qpay_carrier = qpay_carrier;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /*public static class TaeSaleExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_TaeSaleResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }*/

    public String getQpay_administrator_id() {
        return qpay_administrator_id;
    }

    public void setQpay_administrator_id(String qpay_administrator_id) {
        this.qpay_administrator_id = qpay_administrator_id;
    }
}
