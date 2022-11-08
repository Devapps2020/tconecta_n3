package com.blm.qiubopay.models.visa.response;

import java.io.Serializable;

public class CspBody implements Serializable {

    private String code;//": "00",
    private String message;//": "Aprobada",
    private String rrn;//": "829506814322",
    private String authNum;//": "18191",
    private String maskedPan;//": "************0004",
    private String batchNo;//": "271",
    private String invoice;//": "15349",
    private String txDate;//": "2018-10-22T01:23:36-05:00",
    private Emv emv;


    private String amt;//": "74.85",
    private String cashbackAmt;//": "0",
    private String cashbackFee;//": "0",
    private String pinEntryMode;//": "1",

    private String txId;

    private String origRrn;
    private String currency;
    private String origInv;

    private String paymentType;

    //private String code;//": "00",
    //private String message;//": "Aprobada",
    //private String rrn;//": "830206816531",
    //private String authNum;//": "14643",
    //private String maskedPan;//": "************8225",
    //private String batchNo;//": "5",
    //private String invoice;//": "71",
    //private String txDate;//": "2018-10-29T12:40:30-05:00",

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAuthNum() {
        return authNum;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public Emv getEmv() {
        return emv;
    }

    public void setEmv(Emv emv) {
        this.emv = emv;
    }


    /////////////


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

    public String getPinEntryMode() {
        return pinEntryMode;
    }

    public void setPinEntryMode(String pinEntryMode) {
        this.pinEntryMode = pinEntryMode;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getOrigRrn() {
        return origRrn;
    }

    public void setOrigRrn(String origRrn) {
        this.origRrn = origRrn;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrigInv() {
        return origInv;
    }

    public void setOrigInv(String origInv) {
        this.origInv = origInv;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
