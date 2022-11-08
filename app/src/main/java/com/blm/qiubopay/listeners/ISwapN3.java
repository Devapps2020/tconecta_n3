package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.swap.QPAY_SwapN3LoginRequest;

public interface ISwapN3 {

    void loginSwap(QPAY_SwapN3LoginRequest object);

    void swapOutN3(QPAY_NewUser object);

    void swapInN3(QPAY_NewUser object);

}
