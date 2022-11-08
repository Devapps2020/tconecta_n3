package com.blm.qiubopay.models.prestamos;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_LoanResponse implements Serializable {

    private String dateTime;
    private String availableOverdraft;
    private String interestDue;
    private String requestId;
    private String currentOverdraft;
    private String minimumDenomination;
    private String country;
    private String amount;
    private String vendorReference;
    private String currency;
    private String trxId;
    private String transactionId;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAvailableOverdraft() {
        return availableOverdraft;
    }

    public void setAvailableOverdraft(String availableOverdraft) {
        this.availableOverdraft = availableOverdraft;
    }

    public String getInterestDue() {
        return interestDue;
    }

    public void setInterestDue(String interestDue) {
        this.interestDue = interestDue;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCurrentOverdraft() {
        return currentOverdraft;
    }

    public void setCurrentOverdraft(String currentOverdraft) {
        this.currentOverdraft = currentOverdraft;
    }

    public String getMinimumDenomination() {
        return minimumDenomination;
    }

    public void setMinimumDenomination(String minimumDenomination) {
        this.minimumDenomination = minimumDenomination;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVendorReference() {
        return vendorReference;
    }

    public void setVendorReference(String vendorReference) {
        this.vendorReference = vendorReference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public static class QPAY_LoanResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_LoanResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
