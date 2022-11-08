package com.blm.qiubopay.models.dongle;

import java.io.Serializable;

public class QPAY_DongleBuyBaseResponse implements Serializable {
    private String balance;
    private String requestId;
    private String transactionId;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
