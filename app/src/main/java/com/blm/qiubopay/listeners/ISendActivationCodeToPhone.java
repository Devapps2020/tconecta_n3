package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_Seed;

public interface ISendActivationCodeToPhone {
    public void doSendCode(QPAY_Seed object);
}
