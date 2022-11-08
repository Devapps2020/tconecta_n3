package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderResponse {

    @SerializedName("previousPage")
    private String previousPage;

    @SerializedName("nextPage")
    private String nextPage;

    @SerializedName("orders")
    private ArrayList<OrderIds> orderIds;

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public ArrayList<OrderIds> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(ArrayList<OrderIds> orderIds) {
        this.orderIds = orderIds;
    }

}

