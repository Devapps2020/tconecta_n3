package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class SetOrderAcceptanceRequest {

    @SerializedName("qpay_seed")
    private String qpaySeed;

    @SerializedName("accept_b2c_orders")
    private String acceptB2cOrders;

    @SerializedName("b2c_lat")
    private String b2cLat;

    @SerializedName("b2c_lon")
    private String b2cLon;

    public SetOrderAcceptanceRequest(String qpaySeed, String acceptB2cOrders, String b2cLat, String b2cLon) {
        this.qpaySeed = qpaySeed;
        this.acceptB2cOrders = acceptB2cOrders;
        this.b2cLat = b2cLat;
        this.b2cLon = b2cLon;
    }

    public String getQpaySeed() {
        return qpaySeed;
    }

    public void setQpaySeed(String qpaySeed) {
        this.qpaySeed = qpaySeed;
    }

    public String getAcceptB2cOrders() {
        return acceptB2cOrders;
    }

    public void setAcceptB2cOrders(String acceptB2cOrders) {
        this.acceptB2cOrders = acceptB2cOrders;
    }

    public String getB2cLat() {
        return b2cLat;
    }

    public void setB2cLat(String b2cLat) {
        this.b2cLat = b2cLat;
    }

    public String getB2cLon() {
        return b2cLon;
    }

    public void setB2cLon(String b2cLon) {
        this.b2cLon = b2cLon;
    }
}