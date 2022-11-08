package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;
import java.util.List;

public class QPAY_CampaignDone_Object implements Serializable {

    private String id;
    private String campaignId;
    private String status;
    private String finishedChallenges;
    private String finishedDate;
    private String validatedDate;
    private String name;
    private String prize_amount = null;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getStatus() {
        return status;
    }

    public String getFinishedChallenges() {
        return finishedChallenges;
    }

    public String getFinishedDate() {
        return finishedDate;
    }

    public String getValidatedDate() {
        return validatedDate;
    }

    public String getName() {
        return name;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFinishedChallenges(String finishedChallenges) {
        this.finishedChallenges = finishedChallenges;
    }

    public void setFinishedDate(String finishedDate) {
        this.finishedDate = finishedDate;
    }

    public void setValidatedDate(String validatedDate) {
        this.validatedDate = validatedDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrize_amount(String prize_amount) {
        this.prize_amount = prize_amount;
    }

}