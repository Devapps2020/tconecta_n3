package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.swap.QPAY_LinkT1000Request;
import com.blm.qiubopay.models.QPAY_NewUser;

public interface IMultiDevice {

    void startLinkT1000(QPAY_LinkT1000Request object);

    void validateLinkT1000(QPAY_LinkT1000Request object);

    void finishLinkT1000(QPAY_NewUser object);

}
