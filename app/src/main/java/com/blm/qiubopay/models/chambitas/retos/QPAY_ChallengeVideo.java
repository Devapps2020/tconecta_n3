package com.blm.qiubopay.models.chambitas.retos;

import java.io.Serializable;

public class QPAY_ChallengeVideo implements Serializable {

    private String qpay_seed;
    private String campaign_id;
    private String challenge_id;


    // Getter Methods

    public String getQpay_seed() {
        return qpay_seed;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    // Setter Methods

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

}