package com.blm.qiubopay.models.financial_vas;

import com.blm.qiubopay.models.QPAY_DeviceInfo;

import java.io.Serializable;

public class QPAY_FinancialVasPetition implements Serializable {
    private String qpay_seed;
    private String qpay_amount;
    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_FinancialVasPetition() {
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

    public String getQpay_amount() {
        return qpay_amount;
    }

    public void setQpay_amount(String qpay_amount) {
        this.qpay_amount = qpay_amount;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }
}
