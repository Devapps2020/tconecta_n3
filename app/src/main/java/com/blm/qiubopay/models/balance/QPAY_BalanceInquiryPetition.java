package com.blm.qiubopay.models.balance;

import java.io.Serializable;

public class QPAY_BalanceInquiryPetition implements Serializable {
    private String qpay_seed;//": "s/nGHtQTTChQm4E3yc+UxgEEZOS3EMCxc8jKeBkqtXg\u003d",
    private String qpay_mail;//": "conectaqa20@yopmail.com",
    private String qpay_balance_vas;//":"0/1",
    private String qpay_balance_financial;//":"0/1",
    private String qpay_balance_benefits;//":"0/1"

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_balance_vas() {
        return qpay_balance_vas;
    }

    public void setQpay_balance_vas(String qpay_balance_vas) {
        this.qpay_balance_vas = qpay_balance_vas;
    }

    public String getQpay_balance_financial() {
        return qpay_balance_financial;
    }

    public void setQpay_balance_financial(String qpay_balance_financial) {
        this.qpay_balance_financial = qpay_balance_financial;
    }

    public String getQpay_balance_benefits() {
        return qpay_balance_benefits;
    }

    public void setQpay_balance_benefits(String qpay_balance_benefits) {
        this.qpay_balance_benefits = qpay_balance_benefits;
    }
}
