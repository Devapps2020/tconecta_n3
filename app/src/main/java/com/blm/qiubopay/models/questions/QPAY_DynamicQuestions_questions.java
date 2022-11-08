package com.blm.qiubopay.models.questions;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_DynamicQuestions_questions extends QPAY_BaseResponse implements Serializable {

    private Integer id;
    private String question;
    private Integer position;
    private Integer campaignId;

    private QPAY_DynamicQuestions_answers[] answers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QPAY_DynamicQuestions_answers[] getAnswers() {
        return answers;
    }

    public void setAnswers(QPAY_DynamicQuestions_answers[] answers) {
        this.answers = answers;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
}