package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.pedidos.QPAY_GetOrder;

public interface IGetOrder {
    public void getOrder(QPAY_GetOrder seed);
}
