package com.blm.qiubopay.models.services;

import java.io.Serializable;

public class ServicePackageItemModel implements Serializable {

    private String description;
    private String amount;
    private String id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
