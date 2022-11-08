package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_NewUser;

public interface ISwapT1000 {

    void beginSwap(QPAY_NewUser object);

    void finishSwap(QPAY_NewUser object);

}
