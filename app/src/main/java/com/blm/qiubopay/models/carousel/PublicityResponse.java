package com.blm.qiubopay.models.carousel;

import com.google.gson.annotations.SerializedName;

public class PublicityResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("carrouselImage")
    private String carrouselImage;
    @SerializedName("buttonText")
    private String buttonText;
    @SerializedName("description")
    private String description;
    @SerializedName("link")
    private String link;
    @SerializedName("creation_date")
    private String creationDate ;
    @SerializedName("company_id")
    private int companyId;
    private int campaignId;

    public PublicityResponse(Integer publicityId, String title, String image, String carrouselImage, String buttonText, String description, String link, String creationDate, Integer companyId, Integer campaignId) {
        this.id = publicityId;
        this.title = title;
        this.image = image;
        this.carrouselImage = carrouselImage;
        this.buttonText = buttonText;
        this.description = description;
        this.link = link;
        this.creationDate = creationDate;
        this.companyId = companyId;
        this.campaignId = campaignId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCarrouselImage() {
        return carrouselImage;
    }

    public void setCarrouselImage(String carrouselImage) {
        this.carrouselImage = carrouselImage;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
}
