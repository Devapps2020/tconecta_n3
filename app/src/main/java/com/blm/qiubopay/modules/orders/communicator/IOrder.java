package com.blm.qiubopay.modules.orders.communicator;

import com.blm.qiubopay.models.order.ProductsAcceptModel;

import java.util.ArrayList;

public interface IOrder {

    void onDeliverOrderClickListener(String orderId, ArrayList<ProductsAcceptModel> productsAcceptModels, Double totalAmount, Integer time);
    void onRejectOrderClickListener(String orderId);
    void onOrderBackClickListener();
    void onGetOrderAcceptanceListener();

}
