package com.blm.qiubopay.models.visa.response;

import java.io.Serializable;

public class Emv implements Serializable {

    private String applicationTransactionCounter;
    private String aid;
    private String applicationCryptogram;

    public String getApplicationTransactionCounter() {
        return applicationTransactionCounter;
    }

    public void setApplicationTransactionCounter(String applicationTransactionCounter) {
        this.applicationTransactionCounter = applicationTransactionCounter;
    }


    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getApplicationCryptogram() {
        return applicationCryptogram;
    }

    public void setApplicationCryptogram(String applicationCryptogram) {
        this.applicationCryptogram = applicationCryptogram;
    }
}
