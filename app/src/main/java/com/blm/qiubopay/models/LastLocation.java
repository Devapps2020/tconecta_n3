package com.blm.qiubopay.models;

import java.io.Serializable;

public class LastLocation implements Serializable {

    private double latitude;
    private double longitude;
    private String date;
    private Boolean isAlreadySent;

    public LastLocation(){}

    public LastLocation(double latitude, double longitude, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getAlreadySent() {
        return isAlreadySent;
    }

    public void setAlreadySent(Boolean alreadySent) {
        isAlreadySent = alreadySent;
    }
}
