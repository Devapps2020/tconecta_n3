package com.blm.qiubopay.models;

import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;

import java.io.Serializable;

public class QPAY_DeviceInfo implements Serializable {

    private String qpay_smartphone_os;//":"IOS",
    private String qpay_smartphone_os_version;//":"10.10.10",
    private String qpay_smartphone_model;//":"5s",
    private String qpay_smartphone_sn;//":"1234567890",
    private String qpay_app_version;//":"09.09.09",
    private String qpay_dongle_model;//":"model",
    private String qpay_manufacter;//":"model",
    private String qpay_dongle_firmware_version;//":"123.123.123.123",
    private String qpay_dongle_sn;//":"0987654321"
    private String qpay_dongle_mac;
    private String qpay_icc_id;
    //20200226. Improvements. Geolocalizacion
    private String qpay_geo_x; //Longitude
    private String qpay_geo_y; //Latitude
    private String qpay_w3w; // w3w Geolocalizacion

    public QPAY_DeviceInfo(){
        this.qpay_smartphone_os             = "1";//1.- Android 2.- iOS
        this.qpay_smartphone_os_version     = Tools.getOsVersion();
        this.qpay_smartphone_model          = Tools.getModel();
        this.qpay_manufacter                = Tools.getManufacter();
        this.qpay_smartphone_sn             = Tools.getUUID();
        this.qpay_app_version               = Globals.VERSION;
        this.qpay_icc_id                    = "ND";
        if(Tools.isN3Terminal()){
            this.qpay_dongle_model              = Tools.getModel();//"N3";
            this.qpay_dongle_firmware_version   = "ND";
            this.qpay_dongle_sn                 = "ND";
        } else {
            if(AppPreferences.isDeviceRegistered()){
                BL_Device bl_device = AppPreferences.getDevice();
                this.qpay_dongle_model = "ND";
                this.qpay_dongle_firmware_version = "ND";
                this.qpay_dongle_sn = bl_device.getName();
                this.qpay_dongle_mac = bl_device.getMacAddress();
            } else {
                this.qpay_dongle_model = "ND";
                this.qpay_dongle_firmware_version = "ND";
                this.qpay_dongle_sn = "ND";
                this.qpay_dongle_mac = "ND";
            }
        }

    }

    public void setNewSN(String sn){
        this.setQpay_dongle_sn(sn);
        //this.setQpay_smartphone_sn(sn);
    }

    public String getQpay_smartphone_os() {
        return qpay_smartphone_os;
    }

    public void setQpay_smartphone_os(String qpay_smartphone_os) {
        this.qpay_smartphone_os = qpay_smartphone_os;
    }

    public String getQpay_smartphone_os_version() {
        return qpay_smartphone_os_version;
    }

    public void setQpay_smartphone_os_version(String qpay_smartphone_os_version) {
        this.qpay_smartphone_os_version = qpay_smartphone_os_version;
    }

    public String getQpay_smartphone_model() {
        return qpay_smartphone_model;
    }

    public void setQpay_smartphone_model(String qpay_smartphone_model) {
        this.qpay_smartphone_model = qpay_smartphone_model;
    }

    public String getQpay_manufacter() {
        return qpay_manufacter;
    }

    public void setQpay_manufacter(String qpay_manufacter) {
        this.qpay_manufacter = qpay_manufacter;
    }

    public String getQpay_smartphone_sn() {
        return qpay_smartphone_sn;
    }

    public void setQpay_smartphone_sn(String qpay_smartphone_sn) {
        this.qpay_smartphone_sn = qpay_smartphone_sn;
    }

    public String getQpay_app_version() {
        return qpay_app_version;
    }

    public void setQpay_app_version(String qpay_app_version) {
        this.qpay_app_version = qpay_app_version;
    }

    public String getQpay_dongle_model() {
        return qpay_dongle_model;
    }

    public void setQpay_dongle_model(String qpay_dongle_model) {
        this.qpay_dongle_model = qpay_dongle_model;
    }

    public String getQpay_dongle_firmware_version() {
        return qpay_dongle_firmware_version;
    }

    public void setQpay_dongle_firmware_version(String qpay_dongle_firmware_version) {
        this.qpay_dongle_firmware_version = qpay_dongle_firmware_version;
    }

    public String getQpay_dongle_sn() {
        return qpay_dongle_sn;
    }

    public void setQpay_dongle_sn(String qpay_dongle_sn) {
        this.qpay_dongle_sn = qpay_dongle_sn;
    }

    public String getQpay_dongle_mac() {
        return qpay_dongle_mac;
    }

    public void setQpay_dongle_mac(String qpay_dongle_mac) {
        this.qpay_dongle_mac = qpay_dongle_mac;
    }

    public String getQpay_icc_id() {
        return qpay_icc_id;
    }

    public void setQpay_icc_id(String qpay_icc_id) {
        this.qpay_icc_id = qpay_icc_id;
    }

    public String getQpay_geo_x() {
        return qpay_geo_x;
    }

    public void setQpay_geo_x(String qpay_geo_x) {
        this.qpay_geo_x = qpay_geo_x;
    }

    public String getQpay_geo_y() {
        return qpay_geo_y;
    }

    public void setQpay_geo_y(String qpay_geo_y) {
        this.qpay_geo_y = qpay_geo_y;
    }

    public String getQpay_w3w() {
        return qpay_w3w;
    }

    public void setQpay_w3w(String qpay_w3w) {
        this.qpay_w3w = qpay_w3w;
    }
}
