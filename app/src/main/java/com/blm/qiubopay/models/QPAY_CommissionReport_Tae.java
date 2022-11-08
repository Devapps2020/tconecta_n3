package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_CommissionReport_Tae extends QPAY_BaseResponse implements Serializable {
    private String transactionId;
    private String transactionDate;
    private String product;
    private String transactionAmount;
    private String commissionAmount;


    // Getter Methods

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getProduct() {
        return product;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    // Setter Methods

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}