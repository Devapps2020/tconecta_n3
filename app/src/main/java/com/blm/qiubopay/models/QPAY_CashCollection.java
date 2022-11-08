package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_CashCollection implements Serializable {

    private String qpay_seed;

    private String qpay_pin1;
    private String qpay_pin2;
    private String qpay_pin3;
    private String qpay_merchantId;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_CashCollection(){
        qpay_device_info = new QPAY_DeviceInfo[1];
        QPAY_DeviceInfo deviceInfo = new QPAY_DeviceInfo();
        this.qpay_device_info[0] = deviceInfo;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_pin1() {
        return qpay_pin1;
    }

    public void setQpay_pin1(String qpay_pin1) {
        this.qpay_pin1 = qpay_pin1;
    }

    public String getQpay_pin2() {
        return qpay_pin2;
    }

    public void setQpay_pin2(String qpay_pin2) {
        this.qpay_pin2 = qpay_pin2;
    }

    public String getQpay_pin3() {
        return qpay_pin3;
    }

    public void setQpay_pin3(String qpay_pin3) {
        this.qpay_pin3 = qpay_pin3;
    }

    public String getQpay_merchantId() {
        return qpay_merchantId;
    }

    public void setQpay_merchantId(String qpay_merchantId) {
        this.qpay_merchantId = qpay_merchantId;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }
}