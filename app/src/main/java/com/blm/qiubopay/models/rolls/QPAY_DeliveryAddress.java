package com.blm.qiubopay.models.rolls;

import java.io.Serializable;

public class QPAY_DeliveryAddress implements Serializable {
    private String street;//" : "Coahuila";
    private String internalNumber;//" : "234";
    private String externalNumber;//" : "1";
    private String postalCode;//" : "15900";
    private String suburb;//" : "Acacias";
    private String state;//" : "9";
    private String municipality;//" : "Tlalpan";
    private String latitude;//" : 76676767.7655;
    private String longitude;//" : 76336732.7655;

    private String crossingStreets;
    private String references;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public void setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCrossingStreets() {
        return crossingStreets;
    }

    public void setCrossingStreets(String crossingStreets) {
        this.crossingStreets = crossingStreets;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

}
