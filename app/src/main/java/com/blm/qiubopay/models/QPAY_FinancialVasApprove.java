package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_FinancialVasApprove implements Serializable {

    private String qpay_balance;
    private String qpay_max_limit;
    private String qpay_min_limit;
    private String qpay_VAS_previously_transferred;
    private String[] qpay_valid_amounts;

    public String getQpay_balance() {
        return qpay_balance;
    }

    public void setQpay_balance(String qpay_balance) {
        this.qpay_balance = qpay_balance;
    }

    public String getQpay_max_limit() {
        return qpay_max_limit;
    }

    public void setQpay_max_limit(String qpay_max_limit) {
        this.qpay_max_limit = qpay_max_limit;
    }

    public String getQpay_min_limit() {
        return qpay_min_limit;
    }

    public void setQpay_min_limit(String qpay_min_limit) {
        this.qpay_min_limit = qpay_min_limit;
    }

    public String getQpay_VAS_previously_transferred() {
        return qpay_VAS_previously_transferred;
    }

    public void setQpay_VAS_previously_transferred(String qpay_VAS_previously_transferred) {
        this.qpay_VAS_previously_transferred = qpay_VAS_previously_transferred;
    }

    public String[] getQpay_valid_amounts() {
        return qpay_valid_amounts;
    }

    public void setQpay_valid_amounts(String[] qpay_valid_amounts) {
        this.qpay_valid_amounts = qpay_valid_amounts;
    }
}
