package com.blm.qiubopay.models.carousel;

import com.google.gson.annotations.SerializedName;

public class CarouselCampaignsRequest {

    @SerializedName("qpay_seed")
    private String qpaySeed;

    public CarouselCampaignsRequest(String qpaySeed) {
        this.qpaySeed = qpaySeed;
    }

    public String getQpaySeed() {
        return qpaySeed;
    }

    public void setQpaySeed(String qpaySeed) {
        this.qpaySeed = qpaySeed;
    }

}
