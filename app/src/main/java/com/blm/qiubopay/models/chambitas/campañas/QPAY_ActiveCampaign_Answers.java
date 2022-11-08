package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;

public class QPAY_ActiveCampaign_Answers implements Serializable {

    private String id;
    private String answer;
    private boolean isCorrect;
    private boolean isOpenQuestion;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public boolean getIsOpenQuestion() {
        return isOpenQuestion;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setIsOpenQuestion(boolean isOpenQuestion) {
        this.isOpenQuestion = isOpenQuestion;
    }


}