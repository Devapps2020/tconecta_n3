package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;
import java.util.List;

public class QPAY_ActiveCampaignNew_Object implements Serializable {

    private String id;
    private String status;
    private String name;
    private String activation_date;
    private String expiration_date;
    private String approved_date;
    private String prize_amount;


    private List<QPAY_ActiveCampaignNew_Challenges> challenges;

    public List<QPAY_ActiveCampaignNew_Challenges> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<QPAY_ActiveCampaignNew_Challenges> challenges) {
        this.challenges = challenges;
    }


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getActivation_date() {
        return activation_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation_date(String activation_date) {
        this.activation_date = activation_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public void setPrize_amount(String prize_amount) {
        this.prize_amount = prize_amount;
    }

}