package com.blm.qiubopay.models.visa.request;

import java.io.Serializable;

public class Emv implements Serializable {
    private String aid;                                 //": "A0000000031010",
    private String applicationInterchangeProfile;       //": "1c00",
    private String dedicatedFileName;                   //": "a0000000031010",
    private String terminalVerificationResults;         //": "8000008000",
    private String transactionDate;                     //": "151007",
    private String tsi;                                 //": "6800",
    private String transactionType;                     //": "0",
    private String issuerCountryCode;                   //": "840",
    private String transactionCurrencyCode;             //": "484",
    private String cardSequenceNumber;                  //": "0",
    private String transactionAmount;                   //": "262",
    private String amountOther;                         //": "100",
    private String applicationVersionNumber;            //": "008c",
    private String issuerApplicationData;               //": "06010a03a40000",
    private String terminalCountryCode;                 //": "484",
    private String interfaceDeviceSerialNumber;         //": "324b353534323833",
    private String applicationCryptogram;               //": "543a534f738c3993",
    private String cryptogramInformationData;           //": "80",
    private String terminalCapabilities;                //": "e0b0c8",
    private String cardholderVerificationMethodResults; //": "410302",
    private String terminalType;                        //": "22",
    private String applicationTransactionCounter;       //": "0100",
    private String unpredictableNumber;                 //": "1eb0b54e",
    private String transactionSequenceCounterId;        //": "17794",
    private String applicationCurrencyCode;             //": "840"

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getApplicationInterchangeProfile() {
        return applicationInterchangeProfile;
    }

    public void setApplicationInterchangeProfile(String applicationInterchangeProfile) {
        this.applicationInterchangeProfile = applicationInterchangeProfile;
    }

    public String getDedicatedFileName() {
        return dedicatedFileName;
    }

    public void setDedicatedFileName(String dedicatedFileName) {
        this.dedicatedFileName = dedicatedFileName;
    }

    public String getTerminalVerificationResults() {
        return terminalVerificationResults;
    }

    public void setTerminalVerificationResults(String terminalVerificationResults) {
        this.terminalVerificationResults = terminalVerificationResults;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getIssuerCountryCode() {
        return issuerCountryCode;
    }

    public void setIssuerCountryCode(String issuerCountryCode) {
        this.issuerCountryCode = issuerCountryCode;
    }

    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getCardSequenceNumber() {
        return cardSequenceNumber;
    }

    public void setCardSequenceNumber(String cardSequenceNumber) {
        this.cardSequenceNumber = cardSequenceNumber;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getAmountOther() {
        return amountOther;
    }

    public void setAmountOther(String amountOther) {
        this.amountOther = amountOther;
    }

    public String getApplicationVersionNumber() {
        return applicationVersionNumber;
    }

    public void setApplicationVersionNumber(String applicationVersionNumber) {
        this.applicationVersionNumber = applicationVersionNumber;
    }

    public String getIssuerApplicationData() {
        return issuerApplicationData;
    }

    public void setIssuerApplicationData(String issuerApplicationData) {
        this.issuerApplicationData = issuerApplicationData;
    }

    public String getTerminalCountryCode() {
        return terminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        this.terminalCountryCode = terminalCountryCode;
    }

    public String getInterfaceDeviceSerialNumber() {
        return interfaceDeviceSerialNumber;
    }

    public void setInterfaceDeviceSerialNumber(String interfaceDeviceSerialNumber) {
        this.interfaceDeviceSerialNumber = interfaceDeviceSerialNumber;
    }

    public String getApplicationCryptogram() {
        return applicationCryptogram;
    }

    public void setApplicationCryptogram(String applicationCryptogram) {
        this.applicationCryptogram = applicationCryptogram;
    }

    public String getCryptogramInformationData() {
        return cryptogramInformationData;
    }

    public void setCryptogramInformationData(String cryptogramInformationData) {
        this.cryptogramInformationData = cryptogramInformationData;
    }

    public String getTerminalCapabilities() {
        return terminalCapabilities;
    }

    public void setTerminalCapabilities(String terminalCapabilities) {
        this.terminalCapabilities = terminalCapabilities;
    }

    public String getCardholderVerificationMethodResults() {
        return cardholderVerificationMethodResults;
    }

    public void setCardholderVerificationMethodResults(String cardholderVerificationMethodResults) {
        this.cardholderVerificationMethodResults = cardholderVerificationMethodResults;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getApplicationTransactionCounter() {
        return applicationTransactionCounter;
    }

    public void setApplicationTransactionCounter(String applicationTransactionCounter) {
        this.applicationTransactionCounter = applicationTransactionCounter;
    }

    public String getUnpredictableNumber() {
        return unpredictableNumber;
    }

    public void setUnpredictableNumber(String unpredictableNumber) {
        this.unpredictableNumber = unpredictableNumber;
    }

    public String getTransactionSequenceCounterId() {
        return transactionSequenceCounterId;
    }

    public void setTransactionSequenceCounterId(String transactionSequenceCounterId) {
        this.transactionSequenceCounterId = transactionSequenceCounterId;
    }

    public String getApplicationCurrencyCode() {
        return applicationCurrencyCode;
    }

    public void setApplicationCurrencyCode(String applicationCurrencyCode) {
        this.applicationCurrencyCode = applicationCurrencyCode;
    }
}
