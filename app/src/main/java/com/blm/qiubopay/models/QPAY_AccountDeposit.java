package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_AccountDeposit implements Serializable {

    private String qpay_seed;

    private String qpay_branch;
    private String qpay_reference;
    private String qpay_date;
    private String qpay_amount;
    private String qpay_product;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_AccountDeposit() {
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

    public String getQpay_branch() {
        return qpay_branch;
    }

    public void setQpay_branch(String qpay_branch) {
        this.qpay_branch = qpay_branch;
    }

    public String getQpay_reference() {
        return qpay_reference;
    }

    public void setQpay_reference(String qpay_reference) {
        this.qpay_reference = qpay_reference;
    }

    public String getQpay_date() {
        return qpay_date;
    }

    public void setQpay_date(String qpay_date) {
        this.qpay_date = qpay_date;
    }

    public String getQpay_amount() {
        return qpay_amount;
    }

    public void setQpay_amount(String qpay_amount) {
        this.qpay_amount = qpay_amount;
    }

    public String getQpay_product() {
        return qpay_product;
    }

    public void setQpay_product(String qpay_product) {
        this.qpay_product = qpay_product;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }
}