package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class OrderRequest {

    @SerializedName("qpay_seed")
    private String qpay_seed;

    @SerializedName("page_after")
    private String page_after;

    @SerializedName("page_before")
    private String page_before;

    @SerializedName("status")
    private String status;

    @SerializedName("phone")
    private String phone;

    public OrderRequest(String qpay_seed, String page_after, String page_before, String status, String phone) {
        this.qpay_seed = qpay_seed;
        this.page_after = page_after;
        this.page_before = page_before;
        this.status = status;
        this.setPhone(phone);
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getPage_after() {
        return page_after;
    }

    public void setPage_after(String page_after) {
        this.page_after = page_after;
    }

    public String getPage_before() {
        return page_before;
    }

    public void setPage_before(String page_before) {
        this.page_before = page_before;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}