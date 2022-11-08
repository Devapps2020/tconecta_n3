package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.services.QPAY_ServicePayment;

public interface iServicePayment {
    void doServicePayment(QPAY_ServicePayment object);
    void doQueryPayment(QPAY_ServicePayment object);
}
