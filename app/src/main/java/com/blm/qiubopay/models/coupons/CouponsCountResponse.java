package com.blm.qiubopay.models.coupons;

import com.google.gson.annotations.SerializedName;

public class CouponsCountResponse {

    @SerializedName("couponsAvailable")
    private String couponsAvailable;

    public String getCouponsAvailable() {
        return couponsAvailable;
    }

    public void setCouponsAvailable(String couponsAvailable) {
        this.couponsAvailable = couponsAvailable;
    }
}
