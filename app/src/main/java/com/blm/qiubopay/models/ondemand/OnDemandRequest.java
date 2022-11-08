package com.blm.qiubopay.models.ondemand;

import com.google.gson.annotations.SerializedName;

public class OnDemandRequest {

    @SerializedName("campaign_id")
    private String campaingId;

    public OnDemandRequest(String campingId) {
        this.campaingId = campingId;
    }

    public String getCampingId() {
        return campaingId;
    }

    public void setCampingId(String campingId) {
        this.campaingId = campingId;
    }
}
