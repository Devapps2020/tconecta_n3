package com.blm.qiubopay.models.proceedings;

import java.io.Serializable;

public class QPAY_AllDataRecord implements Serializable {

    private String qpay_seed;
    private boolean includeImages;


    // Getter Methods

    public String getQpay_seed() {
        return qpay_seed;
    }

    public boolean getIncludeImages() {
        return includeImages;
    }

    // Setter Methods

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public void setIncludeImages(boolean includeImages) {
        this.includeImages = includeImages;
    }

}