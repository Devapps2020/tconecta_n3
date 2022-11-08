package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_Seed;

public interface IDebitAndCreditTxrs {
    public void doGetTransactions(QPAY_Seed object);
}
