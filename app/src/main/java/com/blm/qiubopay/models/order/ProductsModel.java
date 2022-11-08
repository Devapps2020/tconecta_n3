package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class ProductsModel {

    @SerializedName("name")
    private String name;

    @SerializedName("qty")
    private Integer qty;

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
}
