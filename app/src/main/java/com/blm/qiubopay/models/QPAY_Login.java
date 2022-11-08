package com.blm.qiubopay.models;

import android.content.Context;

import java.io.Serializable;

public class QPAY_Login implements Serializable {

    private String qpay_mail;
    private String qpay_password;
    private String qpay_telemetry;
    private String login_type;
    private String social_token;

    private String qpay_fcmId;

    private String tconectaMigration;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_Login(Context context) {
        qpay_device_info = new QPAY_DeviceInfo[1];
        QPAY_DeviceInfo deviceInfo = new QPAY_DeviceInfo();
        this.qpay_device_info[0] = deviceInfo;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_password() {
        return qpay_password;
    }

    public void setQpay_password(String qpay_password) {
        this.qpay_password = qpay_password;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }

    public String getQpay_fcmId() {
        return qpay_fcmId;
    }

    public void setQpay_fcmId(String qpay_fcmId) {
        this.qpay_fcmId = qpay_fcmId;
    }

    public String getQpay_telemetry() {
        return qpay_telemetry;
    }

    public void setQpay_telemetry(String qpay_telemetry) {
        this.qpay_telemetry = qpay_telemetry;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }

    public String getTconectaMigration() {
        return tconectaMigration;
    }

    public void setTconectaMigration(String tconectaMigration) {
        this.tconectaMigration = tconectaMigration;
    }
}
