package com.blm.qiubopay.models;

public class Carrier {
    public String carrier;
    public String image;

    public Carrier(String carrier, String image) {
        this.carrier = carrier;
        this.image = image;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
