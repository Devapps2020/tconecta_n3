package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_TokenValidateInformation;

public interface ITokenValidateInformation {
    public void doValidateTokenInformation(QPAY_TokenValidateInformation object);
}
