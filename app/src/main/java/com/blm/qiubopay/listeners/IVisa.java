package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;

public interface IVisa {
    public void getTransact(QPAY_VisaEmvRequest object);
}
