package com.blm.qiubopay.models.carousel;

import java.io.Serializable;

public class FullScreenData  implements Serializable {

    private Integer campaignId;
    private String title;
    private String description;
    private String onDemandImage;
    private String link;
    private String textButton;

    public FullScreenData(String title, String description, String onDemandImage, String link, String textButton, Integer campaignId) {
        this.title = title;
        this.description = description;
        this.onDemandImage = onDemandImage;
        this.link = link;
        this.textButton = textButton;
        this.setCampaignId(campaignId);
    }

    public String getOnDemandImage() {
        return onDemandImage;
    }

    public void setOnDemandImage(String onDemandImage) {
        this.onDemandImage = onDemandImage;
    }

    public String getTextButton() {
        return textButton;
    }

    public void setTextButton(String textButton) {
        this.textButton = textButton;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
}
