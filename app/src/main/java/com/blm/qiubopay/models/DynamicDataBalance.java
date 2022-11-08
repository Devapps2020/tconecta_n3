package com.blm.qiubopay.models;

import java.io.Serializable;

public class DynamicDataBalance implements Serializable {

    public String balance;
    public String requestId;

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
}
