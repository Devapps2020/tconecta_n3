package com.blm.qiubopay.models.balance;

import com.blm.qiubopay.models.Balance;
import com.blm.qiubopay.models.QPAY_CommissionReport_Object;

import java.io.Serializable;

public class QPAY_BalanceInquiry implements Serializable {

    private Balance balance;
    private Balance today;
    private QPAY_CommissionReport_Object commissions;

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Balance getToday() {
        return today;
    }

    public void setToday(Balance today) {
        this.today = today;
    }

    public QPAY_CommissionReport_Object getCommissions() {
        return commissions;
    }

    public void setCommissions(QPAY_CommissionReport_Object commissions) {
        this.commissions = commissions;
    }
}
