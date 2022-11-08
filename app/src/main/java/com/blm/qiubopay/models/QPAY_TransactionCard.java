package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_TransactionCard implements Serializable {

    private String qpay_reference;
    private String qpay_amount;
    private String qpay_operationType;
    private String qpay_transactionId;
    private String qpay_seed;

    public QPAY_TransactionCard() {

    }

    public String getQpay_reference() {
        return qpay_reference;
    }

    public void setQpay_reference(String qpay_reference) {
        this.qpay_reference = qpay_reference;
    }

    public String getQpay_amount() {
        return qpay_amount;
    }

    public void setQpay_amount(String qpay_amount) {
        this.qpay_amount = qpay_amount;
    }

    public String getQpay_operationType() {
        return qpay_operationType;
    }

    public void setQpay_operationType(String qpay_operationType) {
        this.qpay_operationType = qpay_operationType;
    }

    public String getQpay_transactionId() {
        return qpay_transactionId;
    }

    public void setQpay_transactionId(String qpay_transactionId) {
        this.qpay_transactionId = qpay_transactionId;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

}
