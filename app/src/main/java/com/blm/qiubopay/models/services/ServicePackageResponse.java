package com.blm.qiubopay.models.services;

import java.io.Serializable;
import java.util.ArrayList;

public class ServicePackageResponse implements Serializable {

    private String accountNumber;
    private String vendorReference;
    private String requestId;
    private ArrayList<ServicePackageModel> services;
    private String surcharge;
    private String amount;
    private String client;
    private String flatFee;
    private String vendorAmount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getVendorReference() {
        return vendorReference;
    }

    public void setVendorReference(String vendorReference) {
        this.vendorReference = vendorReference;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ArrayList<ServicePackageModel> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServicePackageModel> services) {
        this.services = services;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(String flatFee) {
        this.flatFee = flatFee;
    }

    public String getVendorAmount() {
        return vendorAmount;
    }

    public void setVendorAmount(String vendorAmount) {
        this.vendorAmount = vendorAmount;
    }

}


