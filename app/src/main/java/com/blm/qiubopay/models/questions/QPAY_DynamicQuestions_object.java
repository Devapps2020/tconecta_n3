package com.blm.qiubopay.models.questions;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_DynamicQuestions_object extends QPAY_BaseResponse implements Serializable {


    private Integer id;
    private String name;
    private String activation_date;
    private String expiration_date;
    private String status;

    private QPAY_DynamicQuestions_questions[] question;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivation_date() {
        return activation_date;
    }

    public void setActivation_date(String activation_date) {
        this.activation_date = activation_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public QPAY_DynamicQuestions_questions[] getQuestion() {
        return question;
    }

    public void setQuestion(QPAY_DynamicQuestions_questions[] question) {
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}