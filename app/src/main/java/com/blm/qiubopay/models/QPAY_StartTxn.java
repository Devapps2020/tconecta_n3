package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_StartTxn implements Serializable {

    private String qpay_seed;

    private String qpay_seedtimestamp;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_StartTxn() {
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

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }

    public String getQpay_seedtimestamp() {
        return qpay_seedtimestamp;
    }

    public void setQpay_seedtimestamp(String qpay_seedtimestamp) {
        this.qpay_seedtimestamp = qpay_seedtimestamp;
    }
}