package com.blm.qiubopay.models;

public class QPAY_Pin {

    private String pin;

    private Boolean biometric;


    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Boolean getBiometric() {

        if(biometric == null)
            return false;

        return biometric;
    }

    public void setBiometric(Boolean biometric) {
        this.biometric = biometric;
    }
}
