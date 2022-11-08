package com.blm.qiubopay.models.visa.request;

import java.io.Serializable;

public class CspBody implements Serializable {

    private String amt;//": "262",
    private String cashbackAmt;//": "100",
    private String cashbackFee;//": "12",
    private String encryptionMode;//": "0",
    private String keyId;
    private String track2;//": "3b343037393436303030303030303030343d31353132323031383939303030303030303030313f",
    private String posCondCode;//": "0",
    private String cardholderIdMet;//": "2",
    private String pinEntryMode;//": "1",
    private String expYear;//": "15",
    private String expMonth;//": "12",
    private String issuer;//": "13",
    private String capture;//": "9",
    private Emv emv;
    private String txId;

    public void setNullObjects() {
        amt = null;
        cashbackAmt = null;
        cashbackFee = null;
        encryptionMode = null;
        keyId = null;
        track2 = null;
        posCondCode = null;
        cardholderIdMet = null;
        pinEntryMode = null;
        expYear = null;
        expMonth = null;
        issuer = null;
        capture = null;
        emv = null;
    }

    public CspBody()
    {
        this.emv = new Emv();
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCashbackAmt() {
        return cashbackAmt;
    }

    public void setCashbackAmt(String cashbackAmt) {
        this.cashbackAmt = cashbackAmt;
    }

    public String getCashbackFee() {
        return cashbackFee;
    }

    public void setCashbackFee(String cashbackFee) {
        this.cashbackFee = cashbackFee;
    }

    public String getEncryptionMode() {
        return encryptionMode;
    }

    public void setEncryptionMode(String encryptionMode) {
        this.encryptionMode = encryptionMode;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getPosCondCode() {
        return posCondCode;
    }

    public void setPosCondCode(String posCondCode) {
        this.posCondCode = posCondCode;
    }

    public String getCardholderIdMet() {
        return cardholderIdMet;
    }

    public void setCardholderIdMet(String cardholderIdMet) {
        this.cardholderIdMet = cardholderIdMet;
    }

    public String getPinEntryMode() {
        return pinEntryMode;
    }

    public void setPinEntryMode(String pinEntryMode) {
        this.pinEntryMode = pinEntryMode;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCapture() {
        return capture;
    }

    public void setCapture(String capture) {
        this.capture = capture;
    }

    public Emv getEmv() {
        return emv;
    }

    public void setEmv(Emv emv) {
        this.emv = emv;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
