package com.blm.qiubopay.models.chambitas.campañas;

import com.google.gson.annotations.SerializedName;

public class CampaignsActiveNewCount {

    @SerializedName("active_campaigns")
    private
    String activeCampaigns;

    @SerializedName("completed_campaigns")
    private
    String completeCampaigns;

    @SerializedName("current_campaigns")
    private
    String currentCampaigns;

    public CampaignsActiveNewCount(String activeCampaigns, String completedCampaigns, String currentCampaigns) {
        this.setActiveCampaigns(activeCampaigns);
        this.setCompleteCampaigns(completedCampaigns);
        this.setCurrentCampaigns(currentCampaigns);
    }

    public String getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(String activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public String getCompleteCampaigns() {
        return completeCampaigns;
    }

    public void setCompleteCampaigns(String completeCampaigns) {
        this.completeCampaigns = completeCampaigns;
    }

    public String getCurrentCampaigns() {
        return currentCampaigns;
    }

    public void setCurrentCampaigns(String currentCampaigns) {
        this.currentCampaigns = currentCampaigns;
    }
}
