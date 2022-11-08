package com.blm.qiubopay.models.coupons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponDetailsListResponse implements Serializable {

    @SerializedName("idcoupon")
    private String idCoupon;

    @SerializedName("nombreComercio")
    private String storeName;

    @SerializedName("vigencia")
    private String validity;

    @SerializedName("folio")
    private String invoice;

    @SerializedName("descripcion")
    private String description;

    @SerializedName("bimboID")
    private String bimboId;

    @SerializedName("printed_at")
    private String printedAt;

    @SerializedName("expiration_at")
    private String expirationAt;

    @SerializedName("chambitaName")
    private String chambitaName;

    @SerializedName("chambitaURL")
    private String chambitaURL;

    public String getIdCoupon() {
        return idCoupon;
    }

    public void setIdCoupon(String idCoupon) {
        this.idCoupon = idCoupon;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBimboId() {
        return bimboId;
    }

    public void setBimboId(String bimboId) {
        this.bimboId = bimboId;
    }

    public String getPrintedAt() {
        return printedAt;
    }

    public void setPrintedAt(String printedAt) {
        this.printedAt = printedAt;
    }

    public String getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(String expirationAt) {
        this.expirationAt = expirationAt;
    }

    public String getChambitaName() {
        return chambitaName;
    }

    public void setChambitaName(String chambitaName) {
        this.chambitaName = chambitaName;
    }

    public String getChambitaURL() {
        return chambitaURL;
    }

    public void setChambitaURL(String chambitaURL) {
        this.chambitaURL = chambitaURL;
    }
}
