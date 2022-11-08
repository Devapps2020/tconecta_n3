package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_StartTxn_object implements Serializable {

    private String timestamp;

    private String requestId;

    private String terminalRef;

    private String claveQiubo;

    private String amount;

    private String trxIdChannel;

    private String transRef;

    private String sessionStatus;

    private QPAY_StartTxn_item[] items;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public QPAY_StartTxn_item[] getItems() {
        return items;
    }

    public void setItems(QPAY_StartTxn_item[] items) {
        this.items = items;
    }

    public String getTerminalRef() {
        return terminalRef;
    }

    public void setTerminalRef(String terminalRef) {
        this.terminalRef = terminalRef;
    }

    public String getClaveQiubo() {
        return claveQiubo;
    }

    public void setClaveQiubo(String claveQiubo) {
        this.claveQiubo = claveQiubo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTrxIdChannel() {
        return trxIdChannel;
    }

    public void setTrxIdChannel(String trxIdChannel) {
        this.trxIdChannel = trxIdChannel;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }
}