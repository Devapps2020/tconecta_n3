package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderModelResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("merchantId")
    private String merchantId;

    @SerializedName("clientName")
    private String clientName;

    @SerializedName("clientNumber")
    private String clientNumber;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("address")
    private String address;

    @SerializedName("status")
    private String status;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("updateDate")
    private String updateDate;

    @SerializedName("totalAmount")
    private Double totalAmount;

    @SerializedName("estimatedTime")
    private String estimatedTime;

    @SerializedName("products")
    private ArrayList<ProductsModel> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public ArrayList<ProductsModel> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductsModel> products) {
        this.products = products;
    }

}