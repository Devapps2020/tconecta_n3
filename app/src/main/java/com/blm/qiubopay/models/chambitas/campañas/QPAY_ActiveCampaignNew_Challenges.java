package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;
import java.util.List;

public class QPAY_ActiveCampaignNew_Challenges implements Serializable {
    private String companyId = null;
    private String id;
    private String challengeType;
    private String title;
    private String image = null;
    private String description;
    private String link = null;
    private boolean isDone;


    // Getter Methods

    public String getCompanyId() {
        return companyId;
    }

    public String getId() {
        return id;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public boolean getIsDone() {
        return isDone;
    }

    // Setter Methods

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

}