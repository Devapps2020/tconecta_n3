package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;

public interface IGetLastFinancialTransactions {
    public void getLastFinancialTransactions(QPAY_VisaEmvRequest object);
}
