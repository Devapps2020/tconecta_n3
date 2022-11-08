package com.blm.qiubopay.adapters.odrer.communicator;

import com.blm.qiubopay.models.order.OrderIds;

public interface IStatusOrderForAdapter {

    void onStatusOrderClickListener(OrderIds current);

    void onDeliverOrderClickListener(String orderId);

}
