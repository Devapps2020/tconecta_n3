package com.blm.qiubopay.models.coupons;

import com.google.gson.annotations.SerializedName;

public class CouponDetailsRequest {

    @SerializedName("seed")
    private String seed;

    @SerializedName("bimboId")
    private String bimboId;

    @SerializedName("cuponId")
    private String couponId;

    public CouponDetailsRequest(String seed, String bimboId, String couponId) {
        this.seed = seed;
        this.bimboId = bimboId;
        this.couponId = couponId;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getBimboId() {
        return bimboId;
    }

    public void setBimboId(String bimboId) {
        this.bimboId = bimboId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
