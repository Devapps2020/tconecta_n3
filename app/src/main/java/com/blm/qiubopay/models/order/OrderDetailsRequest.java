package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class OrderDetailsRequest {

    @SerializedName("qpay_seed")
    private String qpay_seed;

    @SerializedName("order_id")
    private String order_id;

    public OrderDetailsRequest(String qpay_seed, String order_id) {
        this.qpay_seed = qpay_seed;
        this.order_id = order_id;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}