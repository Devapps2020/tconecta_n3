package com.blm.qiubopay.models;

import java.io.Serializable;

/**
 * RSB. Objeto creado para actualizar la información del dispositivo
 * en la BD de QTC
 */
public class QPAY_DeviceUpdate implements Serializable {

    private String qpay_seed;
    private QPAY_DeviceInfo[] qpay_device_info;

    public QPAY_DeviceUpdate() {
        //Se inicializa el objeto device que toma valores de sistema y verifica si hay dongle
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

}