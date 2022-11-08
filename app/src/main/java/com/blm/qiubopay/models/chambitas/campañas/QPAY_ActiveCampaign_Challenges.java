package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;
import java.util.List;

public class QPAY_ActiveCampaign_Challenges implements Serializable {

    private String companyId = null;
    private String id;
    private String challengeType;
    private String indication;
    private String title;
    private String image = null;
    private String description;
    private String link = null;
    private Boolean isDone;

    private List<QPAY_ActiveCampaign_Questions> questions;

    public List<QPAY_ActiveCampaign_Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QPAY_ActiveCampaign_Questions> questions) {
        this.questions = questions;
    }


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

    public String getIndication() { return indication; }

    public String getTitle() { return title; }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
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

    public void setIndication(String indication) { this.indication = indication; }

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

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }
}