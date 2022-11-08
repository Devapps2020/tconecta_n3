package com.blm.qiubopay.models;

import java.io.Serializable;

public class Parent implements Serializable {

    private String lookupType;
    private String lookupValue;

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(String lookupValue) {
        this.lookupValue = lookupValue;
    }
}
