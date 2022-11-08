package com.blm.qiubopay.models.chambitas.cupones;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponsResponse implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("validity")
    private String validity;

    @SerializedName("description")
    private String description;

    @SerializedName("canjeado")
    private boolean redeemed;

    @SerializedName("folio")
    private String folio;

    @SerializedName("comercio")
    private String storeName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}