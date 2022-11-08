package com.blm.qiubopay.models.chambitas.retos;

import java.io.Serializable;

public class QPAY_ChallengeAnswer implements Serializable {

    private String questionId;
    private String answerId;
    private String answer;


    // Getter Methods

    public String getQuestionId() {
        return questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public String getAnswer() {
        return answer;
    }

    // Setter Methods

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}

