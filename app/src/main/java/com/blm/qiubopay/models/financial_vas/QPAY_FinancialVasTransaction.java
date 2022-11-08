package com.blm.qiubopay.models.financial_vas;

import java.io.Serializable;

public class QPAY_FinancialVasTransaction implements Serializable {

    private String amount;
    private String requestId;
    private String transactionId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
