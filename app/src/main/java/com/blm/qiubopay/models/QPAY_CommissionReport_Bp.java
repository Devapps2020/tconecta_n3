package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_CommissionReport_Bp implements Serializable {

    private float transactionId;
    private String transactionDate;
    private String product;
    private float transactionAmount;
    private float commissionAmount;


    // Getter Methods

    public float getTransactionId() {
        return transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getProduct() {
        return product;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public float getCommissionAmount() {
        return commissionAmount;
    }

    // Setter Methods

    public void setTransactionId(float transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setCommissionAmount(float commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}