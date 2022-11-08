package com.blm.qiubopay.listeners.connectionreport;

import com.blm.qiubopay.models.connectionreport.QPAY_Report_Request;
import com.blm.qiubopay.models.transactions.TransactionsModel;

public interface IConnectionReport {
    public void report(TransactionsModel object);
}
