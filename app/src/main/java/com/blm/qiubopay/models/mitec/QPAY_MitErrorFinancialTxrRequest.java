package com.blm.qiubopay.models.mitec;

import java.io.Serializable;

public class QPAY_MitErrorFinancialTxrRequest implements Serializable {
    private String qpay_seed;
    private String transaction;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}
