package com.blm.qiubopay.models.ahorros;

import java.io.Serializable;

public class QPAY_CreateUserSaving implements Serializable {

    private String qpay_seed;
    private String savingPercentage;
    private String newPercentageDate;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }


    // Getter Methods

    public String getSavingPercentage() {
        return savingPercentage;
    }

    public String getNewPercentageDate() {
        return newPercentageDate;
    }

    // Setter Methods

    public void setSavingPercentage(String savingPercentage) {
        this.savingPercentage = savingPercentage;
    }

    public void setNewPercentageDate(String newPercentageDate) {
        this.newPercentageDate = newPercentageDate;
    }

}