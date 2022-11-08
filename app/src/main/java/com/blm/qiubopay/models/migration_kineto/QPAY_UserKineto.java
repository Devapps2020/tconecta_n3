package com.blm.qiubopay.models.migration_kineto;

import java.io.Serializable;

public class QPAY_UserKineto implements Serializable {

    private String kineto_user;
    private String kineto_pin;
    private String kineto_cellphone;

    public String getKineto_user() {
        return kineto_user;
    }

    public void setKineto_user(String kineto_user) {
        this.kineto_user = kineto_user;
    }

    public String getKineto_pin() {
        return kineto_pin;
    }

    public void setKineto_pin(String kineto_pin) {
        this.kineto_pin = kineto_pin;
    }

    public String getKineto_cellphone() {
        return kineto_cellphone;
    }

    public void setKineto_cellphone(String kineto_cellphone) {
        this.kineto_cellphone = kineto_cellphone;
    }

}
