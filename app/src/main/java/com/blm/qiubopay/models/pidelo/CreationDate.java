package com.blm.qiubopay.models.pidelo;

public class CreationDate {

    private String date;

    public String getDate() {
        return date.substring(0, date.indexOf("."));
    }

    public void setDate(String date) {
        this.date = date;
    }
}
