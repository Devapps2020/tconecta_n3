package com.blm.qiubopay.models.carousel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QPayObjectResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private String status;
    @SerializedName("name")
    private String name;
    @SerializedName("activation_date")
    private String activationDate;
    @SerializedName("expiration_date")
    private String expirationDate;
    @SerializedName("approved_date")
    private String approvedDate;
    @SerializedName("publicities")
    private ArrayList<PublicityResponse> publicities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public ArrayList<PublicityResponse> getPublicities() {
        return publicities;
    }

    public void setPublicities(ArrayList<PublicityResponse> publicities) {
        this.publicities = publicities;
    }
}
