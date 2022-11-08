package com.blm.qiubopay.models.questions;

import com.google.gson.annotations.SerializedName;

public class CampaignViewedRequest {

    @SerializedName("id")
    private Integer id;

    @SerializedName("qpay_seed")
    private String qpaySeed;

    @SerializedName("qpay_mail")
    private String qpayMail;

    @SerializedName("skipped")
    private Boolean skipped;

    public CampaignViewedRequest(Integer id, String qpaySeed, String qpayMail, Boolean skipped) {
        this.id = id;
        this.qpaySeed = qpaySeed;
        this.qpayMail = qpayMail;
        this.skipped = skipped;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQpaySeed() {
        return qpaySeed;
    }

    public void setQpaySeed(String qpaySeed) {
        this.qpaySeed = qpaySeed;
    }

    public String getQpayMail() {
        return qpayMail;
    }

    public void setQpayMail(String qpayMail) {
        this.qpayMail = qpayMail;
    }

    public Boolean getSkipped() {
        return skipped;
    }

    public void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }
}
