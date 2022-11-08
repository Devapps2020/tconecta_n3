package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_Category2 implements Serializable {
    private String qpay_seed;
    private String qpay_commerce_name;
    private String qpay_commerce_address;
    private String qpay_commerce_city;
    private String qpay_commerce_state;
    private String qpay_commerce_postal_code;
    private String qpay_commerce_number;
    private String qpay_commerce_activity;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_Category2(){
        qpay_device_info = new QPAY_DeviceInfo[1];
        QPAY_DeviceInfo deviceInfo = new QPAY_DeviceInfo();
        this.qpay_device_info[0] = deviceInfo;

        this.qpay_seed = "";
        this.qpay_commerce_name = "";
        this.qpay_commerce_address = "";
        this.qpay_commerce_city = "";
        this.qpay_commerce_state = "";
        this.qpay_commerce_postal_code = "";
        this.qpay_commerce_number = "";
        this.qpay_commerce_activity = "";
    }

    public Boolean isComplete(){
        Boolean response = true;

        if(this.qpay_commerce_name.equals("")
                || this.qpay_commerce_address.equals("")
                || this.qpay_commerce_city.equals("")
                || this.qpay_seed.equals("")
                || this.qpay_commerce_state.equals("")
                || this.qpay_commerce_postal_code.equals("")
                || this.qpay_commerce_number.equals("")
                || this.qpay_commerce_activity.equals(""))
            response = false;

        return response;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_commerce_name() {
        return qpay_commerce_name;
    }

    public void setQpay_commerce_name(String qpay_commerce_name) {
        this.qpay_commerce_name = qpay_commerce_name;
    }

    public String getQpay_commerce_address() {
        return qpay_commerce_address;
    }

    public void setQpay_commerce_address(String qpay_commerce_address) {
        this.qpay_commerce_address = qpay_commerce_address;
    }

    public String getQpay_commerce_city() {
        return qpay_commerce_city;
    }

    public void setQpay_commerce_city(String qpay_commerce_city) {
        this.qpay_commerce_city = qpay_commerce_city;
    }

    public String getQpay_commerce_state() {
        return qpay_commerce_state;
    }

    public void setQpay_commerce_state(String qpay_commerce_state) {
        this.qpay_commerce_state = qpay_commerce_state;
    }

    public String getQpay_commerce_postal_code() {
        return qpay_commerce_postal_code;
    }

    public void setQpay_commerce_postal_code(String qpay_commerce_postal_code) {
        this.qpay_commerce_postal_code = qpay_commerce_postal_code;
    }

    public String getQpay_commerce_number() {
        return qpay_commerce_number;
    }

    public void setQpay_commerce_number(String qpay_commerce_number) {
        this.qpay_commerce_number = qpay_commerce_number;
    }

    public String getQpay_commerce_activity() {
        return qpay_commerce_activity;
    }

    public void setQpay_commerce_activity(String qpay_commerce_activity) {
        this.qpay_commerce_activity = qpay_commerce_activity;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }
}
