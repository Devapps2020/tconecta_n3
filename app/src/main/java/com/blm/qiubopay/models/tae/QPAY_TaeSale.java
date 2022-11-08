package com.blm.qiubopay.models.tae;

import com.blm.qiubopay.models.QPAY_DeviceInfo;

import java.io.Serializable;

public class QPAY_TaeSale implements Serializable {

    private String qpay_seed;
    private String qpay_carrier;
    private String qpay_mobile_number;
    private String qpay_amount;
    private String qpay_routeId;

    private String idOffer;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_TaeSale(){
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

    public String getQpay_carrier() {
        return qpay_carrier;
    }

    public void setQpay_carrier(String qpay_carrier) {
        this.qpay_carrier = qpay_carrier;
    }

    public String getQpay_mobile_number() {
        return qpay_mobile_number;
    }

    public void setQpay_mobile_number(String qpay_mobile_number) {
        this.qpay_mobile_number = qpay_mobile_number;
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

    public String getQpay_routeId() {
        return qpay_routeId;
    }

    public void setQpay_routeId(String qpay_routeId) {
        this.qpay_routeId = qpay_routeId;
    }

    public String getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(String idOffer) {
        this.idOffer = idOffer;
    }
}
