package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryPetition;

public interface IBalanceInquiry {
    public void getBalance(QPAY_BalanceInquiryPetition object);
}
