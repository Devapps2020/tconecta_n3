package com.blm.qiubopay.models.mitec;

import java.io.Serializable;

import integration.praga.mit.com.apiintegration.model.TransactionResponse;

public class CustomMitecTransaction implements Serializable {
    private String paymentType;
    private String qpay_response;
    private String qpay_description;
    private String initial_amount;
    private String comision;
    private String total;
    private boolean usePIN;
    private TransactionResponse transactionResponse;
    private Boolean comesFromATippedSale;
    private String posEntryMode;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getQpay_response() {
        return qpay_response;
    }

    public void setQpay_response(String qpay_response) {
        this.qpay_response = qpay_response;
    }

    public String getQpay_description() {
        return qpay_description;
    }

    public void setQpay_description(String qpay_description) {
        this.qpay_description = qpay_description;
    }

    public String getComision() {
        return comision;
    }

    public void setComision(String comision) {
        this.comision = comision;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

    public void setTransactionResponse(TransactionResponse transactionResponse) {
        this.transactionResponse = transactionResponse;
    }

    public boolean isUsePIN() {
        return usePIN;
    }

    public void setUsePIN(boolean usePIN) {
        this.usePIN = usePIN;
    }

    public String getInitial_amount() {
        return initial_amount;
    }

    public void setInitial_amount(String initial_amount) {
        this.initial_amount = initial_amount;
    }

    public Boolean getComesFromATippedSale() {
        return comesFromATippedSale;
    }

    public void setComesFromATippedSale(Boolean comesFromATippedSale) {
        this.comesFromATippedSale = comesFromATippedSale;
    }

    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }
}
