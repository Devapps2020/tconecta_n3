package com.blm.qiubopay.models.questions;


import java.io.Serializable;

public class QPAY_CampaignAnswers_answers implements Serializable {

  private Integer questionId;
  private Integer answerId;

  public Integer getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }

  public Integer getAnswerId() {
    return answerId;
  }

  public void setAnswerId(Integer answerId) {
    this.answerId = answerId;
  }
}