package com.blm.qiubopay.models.swap;

import java.io.Serializable;

public class QPAY_SwapN3Login implements Serializable {

    private String id;
    private String promotorName;
    private String promotorNumber;
    private String password;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPromotorName() {
        return promotorName;
    }

    public void setPromotorName(String promotorName) {
        this.promotorName = promotorName;
    }

    public String getPromotorNumber() {
        return promotorNumber;
    }

    public void setPromotorNumber(String promotorNumber) {
        this.promotorNumber = promotorNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
