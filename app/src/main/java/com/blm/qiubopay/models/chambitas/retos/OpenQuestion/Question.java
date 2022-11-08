package com.blm.qiubopay.models.chambitas.retos.OpenQuestion;

import com.j256.ormlite.stmt.query.In;

import java.io.Serializable;

public class Question implements Serializable {
    private Integer questionId;
    private String answer;


    // Getter Methods

    public Integer getQuestionId() {
        return questionId;
    }

    public String getAnswer() {
        return answer;
    }

    // Setter Methods

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
