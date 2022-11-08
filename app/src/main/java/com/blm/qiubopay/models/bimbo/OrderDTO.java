package com.blm.qiubopay.models.bimbo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("totalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("productCount")
    @Expose
    private Integer productCount;
    @SerializedName("send")
    @Expose
    private Boolean send;
    @SerializedName("brandid")
    @Expose
    private Integer brandid;
    @SerializedName("creditPesito")
    @Expose
    private String creditPesito;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusDescription")
    @Expose
    private String statusDescription;
    @SerializedName("lastUpdate")
    @Expose
    private Object lastUpdate;
    @SerializedName("wishList")
    @Expose
    private List<WishDTO> wishList = null;
    @SerializedName("retailer_id")
    @Expose
    private String retailerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public String getCreditPesito() {
        return creditPesito;
    }

    public void setCreditPesito(String creditPesito) {
        this.creditPesito = creditPesito;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Object getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Object lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<WishDTO> getWishList() {
        return wishList;
    }

    public void setWishList(List<WishDTO> wishList) {
        this.wishList = wishList;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }


}
