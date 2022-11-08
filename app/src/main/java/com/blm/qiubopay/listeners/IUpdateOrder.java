package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.bimbo.UpdateOrderRequest;

public interface IUpdateOrder {
    public void changeStatusSaleOrder(UpdateOrderRequest request);
}
