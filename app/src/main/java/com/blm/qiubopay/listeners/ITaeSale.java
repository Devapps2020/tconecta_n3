package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.recarga.QPAY_TaeProductRequest;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;

public interface ITaeSale {

    void doTaeSale(QPAY_TaeSale object);

    void getTaeProducts(QPAY_TaeProductRequest object);
}
