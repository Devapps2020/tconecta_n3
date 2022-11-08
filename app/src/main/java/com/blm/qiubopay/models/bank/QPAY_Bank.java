package com.blm.qiubopay.models.bank;

import java.io.Serializable;

public class QPAY_Bank implements Serializable {
    private String key;
    private String shortname;
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
