package com.blm.qiubopay.models.publicity;

import com.google.gson.annotations.SerializedName;

public class CampaignsActiveCount {

    @SerializedName("active_publicities")
    private
    String activePublicities;

    public CampaignsActiveCount(String activePublicities) {
        this.setActivePublicities(activePublicities);
    }

    public String getActivePublicities() {
        return activePublicities;
    }

    public void setActivePublicities(String activePublicities) {
        this.activePublicities = activePublicities;
    }
}
