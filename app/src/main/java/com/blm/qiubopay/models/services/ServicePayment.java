package com.blm.qiubopay.models.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.DynamicDataSaleResponse;

import java.io.Serializable;

public class ServicePayment implements Serializable {

    private String transactionId;
    private String verificationDigit;
    private String dateTime;
    private String requestId;
    private String channelId;
    private String extra;
    private String amount;
    private String product;
    private String terminalId;
    private String accountNumber;
    private String flatFee;
    private String providerAuth;
    private String bill_amount_currency;
    private String created_at;
    private String bill_amount;
    private String account_number;
    private String biller_id;
    private String vendorReference;
    private String responseAt;
    private String qpay_account_number;
    private String qpay_account_number1;
    private String qpay_account_number2;
    private String qpay_account_number3;
    private String qpay_verification_digit;
    private String qpay_amount;
    private String qpay_transactionId;
    private String qpay_product;
    private String rspCode;
    private String rspDescription;

    private String qpay_rspBody;

    private String billReference;
    private String commission;
    private String surcharge;
    private String clientName;
    private String client;

    private String country;
    private String trxId;
    private String currency;

    private String vendorAmount;

    private String qpay_administrator_id;

    public DynamicDataSaleResponse dynamicData;

    private String createdAt;

    private String ticket_text;

    public DynamicDataSaleResponse getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicDataSaleResponse dynamicData) {
        this.dynamicData = dynamicData;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getVerificationDigit() {
        return verificationDigit;
    }

    public void setVerificationDigit(String verificationDigit) {
        this.verificationDigit = verificationDigit;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(String flatFee) {
        this.flatFee = flatFee;
    }

    public String getProviderAuth() {
        return providerAuth;
    }

    public void setProviderAuth(String providerAuth) {
        this.providerAuth = providerAuth;
    }

    public String getBill_amount_currency() {
        return bill_amount_currency;
    }

    public void setBill_amount_currency(String bill_amount_currency) {
        this.bill_amount_currency = bill_amount_currency;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBiller_id() {
        return biller_id;
    }

    public void setBiller_id(String biller_id) {
        this.biller_id = biller_id;
    }

    public String getVendorReference() {
        return vendorReference;
    }

    public void setVendorReference(String vendorReference) {
        this.vendorReference = vendorReference;
    }

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

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getQpay_rspBody() {
        return qpay_rspBody;
    }

    public void setQpay_rspBody(String qpay_rspBody) {
        this.qpay_rspBody = qpay_rspBody;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class ServicePaymentExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(ServicePayment.class);
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVendorAmount() {
        return vendorAmount;
    }

    public void setVendorAmount(String vendorAmount) {
        this.vendorAmount = vendorAmount;
    }

    public String getTicket_text() {
        return ticket_text;
    }

    public void setTicket_text(String ticket_text) {
        this.ticket_text = ticket_text;
    }
}
