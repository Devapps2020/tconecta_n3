package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class ProductsAcceptModel {

    @SerializedName("name")
    private String name;

    @SerializedName("qty")
    private Integer qty;

    @SerializedName("amount")
    private Double amount;

    public ProductsAcceptModel() {}

    public ProductsAcceptModel(String name, Integer qty, Double amount) {
        this.name = name;
        this.qty = qty;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}