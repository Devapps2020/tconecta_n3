package com.blm.qiubopay.models;

import java.io.Serializable;

public class BL_Device implements Serializable {
    private String name;
    private String macAddress;
    private String serie;
    private boolean qtcRegistered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public boolean isQtcRegistered() {
        return qtcRegistered;
    }

    public void setQtcRegistered(boolean qtcRegistered) {
        this.qtcRegistered = qtcRegistered;
    }
}
