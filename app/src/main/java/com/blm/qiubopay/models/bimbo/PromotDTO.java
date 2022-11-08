package com.blm.qiubopay.models.bimbo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromotDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("retailerCode")
    @Expose
    private Integer retailerCode;
    @SerializedName("promotionCode")
    @Expose
    private String promotionCode;
    @SerializedName("requestDate")
    @Expose
    private String requestDate;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusDescription")
    @Expose
    private String statusDescription;
    @SerializedName("lastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("promotionName")
    @Expose
    private String promotionName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRetailerCode() {
        return retailerCode;
    }

    public void setRetailerCode(Integer retailerCode) {
        this.retailerCode = retailerCode;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }
}
