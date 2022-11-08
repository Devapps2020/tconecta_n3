package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_ActivationMerchant;

public interface IValidateMerchantActivationCode {
    public void doValidateCode(QPAY_ActivationMerchant object);
}
