package com.blm.qiubopay.models.bimbo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pieces")
    @Expose
    private Integer pieces;
    @SerializedName("creditPesito")
    @Expose
    private Boolean creditPesito;
    @SerializedName("promotion")
    @Expose
    private Object promotion;
    @SerializedName("route")
    @Expose
    private Object route;
    @SerializedName("originAmount")
    @Expose
    private Double originAmount;
    @SerializedName("totalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("orderSaleId")
    @Expose
    private Integer orderSaleId;
    @SerializedName("brandid")
    @Expose
    private Integer brandid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public Boolean getCreditPesito() {
        return creditPesito;
    }

    public void setCreditPesito(Boolean creditPesito) {
        this.creditPesito = creditPesito;
    }

    public Object getPromotion() {
        return promotion;
    }

    public void setPromotion(Object promotion) {
        this.promotion = promotion;
    }

    public Object getRoute() {
        return route;
    }

    public void setRoute(Object route) {
        this.route = route;
    }

    public Double getOriginAmount() {
        return originAmount;
    }

    public void setOriginAmount(Double originAmount) {
        this.originAmount = originAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrderSaleId() {
        return orderSaleId;
    }

    public void setOrderSaleId(Integer orderSaleId) {
        this.orderSaleId = orderSaleId;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

}
