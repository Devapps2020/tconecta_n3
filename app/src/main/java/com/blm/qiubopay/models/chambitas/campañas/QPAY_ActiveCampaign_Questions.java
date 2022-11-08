package com.blm.qiubopay.models.chambitas.campañas;

import java.io.Serializable;
import java.util.List;

public class QPAY_ActiveCampaign_Questions implements Serializable {

    private String id;
    private String question;
    private String is_numeric;
    private List<QPAY_ActiveCampaign_Answers> answers;



        public List<QPAY_ActiveCampaign_Answers> getAnswers() {
            return answers;
        }

        public String getId() {
            return id;
        }

        public String getQuestion() {
            return question;
        }

        public String getIsNumeric() {
            return is_numeric;
        }


        public void setId(String id) {
            this.id = id;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public void setAnswers(List<QPAY_ActiveCampaign_Answers> answers) {
            this.answers = answers;
        }

        public void setIsNumeric(String is_numeric) {
            this.is_numeric = is_numeric;
        }
}