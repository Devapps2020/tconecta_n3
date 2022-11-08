package com.blm.qiubopay.models.tae;

import java.io.Serializable;

public class QPAY_KinetoTaeSaleResponse implements Serializable {

    private String transactionId;
    private String dateTime;
    private String requestId;
    private String reference;
    private String country;
    private String currency;
    private String amount;
    private String product;
    private String trxId;
    private String flatFee;
    private String mobileNumber;
    private String vendorReference;
    private String authorizationNumber;
    private String customerId;
    private String channelId;
    private String extra;
    private String terminalId;
    private String providerAuth;
    private String timestamp;
    private String commission;
    private String accountNumber;

    private Boolean bimboAward;
    private String bimboAwardMessage;

    private String transactionLabel;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(String flatFee) {
        this.flatFee = flatFee;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVendorReference() {
        return vendorReference;
    }

    public void setVendorReference(String vendorReference) {
        this.vendorReference = vendorReference;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getProviderAuth() {
        return providerAuth;
    }

    public void setProviderAuth(String providerAuth) {
        this.providerAuth = providerAuth;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getBimboAward() {
        return bimboAward;
    }

    public void setBimboAward(Boolean bimboAward) {
        this.bimboAward = bimboAward;
    }

    public String getBimboAwardMessage() {
        return bimboAwardMessage;
    }

    public void setBimboAwardMessage(String bimboAwardMessage) {
        this.bimboAwardMessage = bimboAwardMessage;
    }

    public String getTransactionLabel() {
        return transactionLabel;
    }

    public void setTransactionLabel(String transactionLabel) {
        this.transactionLabel = transactionLabel;
    }
}
