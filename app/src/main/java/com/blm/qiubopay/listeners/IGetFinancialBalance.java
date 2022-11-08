package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;

public interface IGetFinancialBalance {
    public void getBalanceF(QPAY_VisaEmvRequest object);
}
