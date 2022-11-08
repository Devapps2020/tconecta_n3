package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class AcceptOrderResponse {

    @SerializedName("orderId")
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
