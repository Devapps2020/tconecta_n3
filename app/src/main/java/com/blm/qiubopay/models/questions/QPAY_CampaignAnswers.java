package com.blm.qiubopay.models.questions;

import java.io.Serializable;

public class QPAY_CampaignAnswers implements Serializable {

    private Integer id;
    private String qpay_seed;
    private String qpay_mail;
    private Boolean skipped;

    QPAY_CampaignAnswers_answers[] answers;

    public QPAY_CampaignAnswers_answers[] getAnswers() {
        return answers;
    }

    public void setAnswers(QPAY_CampaignAnswers_answers[] answers) {
        this.answers = answers;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public Boolean getSkipped() {
        return skipped;
    }

    public void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }
}