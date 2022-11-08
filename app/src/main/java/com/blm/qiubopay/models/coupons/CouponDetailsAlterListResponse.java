package com.blm.qiubopay.models.coupons;

import com.google.gson.annotations.SerializedName;

public class CouponDetailsAlterListResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("assignedAt")
    private String assignedAt;

    @SerializedName("expirationDate")
    private String expirationDate;

    @SerializedName("printedAt")
    private String printedAt;

    @SerializedName("folio")
    private String folio;

    @SerializedName("description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPrintedAt() {
        return printedAt;
    }

    public void setPrintedAt(String printedAt) {
        this.printedAt = printedAt;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
