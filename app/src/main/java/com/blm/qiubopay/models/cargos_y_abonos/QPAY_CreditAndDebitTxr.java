package com.blm.qiubopay.models.cargos_y_abonos;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_CreditAndDebitTxr implements Serializable {

    private String id;
    private String status;
    private String reference;
    private String amount;
    private String requestId;
    private String transactionId;
    private String rspCode;
    private String operationType;
    private String responseAt;
    private String createdAt;
    private String qpay_administrator_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class QPAY_CreditAndDebitTxrExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_CreditAndDebitTxr.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    public String getQpay_administrator_id() {
        return qpay_administrator_id;
    }

    public void setQpay_administrator_id(String qpay_administrator_id) {
        this.qpay_administrator_id = qpay_administrator_id;
    }
}
