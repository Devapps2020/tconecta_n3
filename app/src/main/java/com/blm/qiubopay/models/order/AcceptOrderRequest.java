package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AcceptOrderRequest {

    @SerializedName("qpay_seed")
    private String qpay_seed;

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("products")
    private ArrayList<ProductsAcceptModel> products;

    @SerializedName("total_amount")
    private Double totalAmount;

    @SerializedName("time")
    private Integer time;

    public AcceptOrderRequest(String qpay_seed, String order_id, ArrayList<ProductsAcceptModel> products, Double totalAmount, Integer time) {
        this.qpay_seed = qpay_seed;
        this.order_id = order_id;
        this.products = products;
        this.totalAmount = totalAmount;
        this.time = time;
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

    public ArrayList<ProductsAcceptModel> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductsAcceptModel> products) {
        this.products = products;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}