package com.blm.qiubopay.models.base;

import com.google.gson.annotations.SerializedName;

public class BaseRequest {

    @SerializedName("qpay_seed")
    private String qpaySeed;

    public BaseRequest(String qpaySeed) {
        this.qpaySeed = qpaySeed;
    }

    public String getQpaySeed() {
        return qpaySeed;
    }

    public void setQpaySeed(String qpaySeed) {
        this.qpaySeed = qpaySeed;
    }

}
