package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.bimbo.GetOrdersRequest;

public interface IGetOrders {
    public void getOrderByRetailerId(GetOrdersRequest request);
}
