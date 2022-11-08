package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class OrderDetailsResponse {

    @SerializedName("order")
    private OrderModelResponse order;

    public OrderModelResponse getOrder() {
        return order;
    }

    public void setOrder(OrderModelResponse order) {
        this.order = order;
    }
}



