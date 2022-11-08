package com.blm.qiubopay.models.swap;

import java.io.Serializable;

public class QPAY_SwapN3LoginRequest implements Serializable {

    private String promotorNumber;
    private String password;

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
}
