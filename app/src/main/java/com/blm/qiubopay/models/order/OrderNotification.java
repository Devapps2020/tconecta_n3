package com.blm.qiubopay.models.order;

public class OrderNotification {

    private String newOrderId;
    private String orderType;

    public OrderNotification(String newOrderId, String orderType) {
        this.newOrderId = newOrderId;
        this.orderType = orderType;
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
