package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_Sigature implements Serializable {
    private String qpay_seed;
    private String qpay_rspId;
    private String qpay_image_name_1;
    private String qpay_image_1;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_rspId() {
        return qpay_rspId;
    }

    public void setQpay_rspId(String qpay_rspId) {
        this.qpay_rspId = qpay_rspId;
    }

    public String getQpay_image_name_1() {
        return qpay_image_name_1;
    }

    public void setQpay_image_name_1(String qpay_image_name_1) {
        this.qpay_image_name_1 = qpay_image_name_1;
    }

    public String getQpay_image_1() {
        return qpay_image_1;
    }

    public void setQpay_image_1(String qpay_image_1) {
        this.qpay_image_1 = qpay_image_1;
    }
}
