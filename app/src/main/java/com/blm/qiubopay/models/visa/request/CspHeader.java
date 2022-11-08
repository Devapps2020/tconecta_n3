package com.blm.qiubopay.models.visa.request;

import com.blm.qiubopay.helpers.AppPreferences;

import java.io.Serializable;

public class CspHeader implements Serializable {

    private String qpay_seed;
    private String qpay_entryMode;
    private String qpay_tdc;
    private String qpay_cardHolder;
    private String qpay_longitude;
    private String qpay_latitude;
    private String qpay_pin;
    private String qpay_application_label;
    private String merchantId;
    private String deviceId;
    private String deviceTypeId;
    private String productId;
    private String txTypeId;
    private String timeout;
    private String reqId;
    private String userId;

    public CspHeader(){
        qpay_seed    = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();
        merchantId   = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();//"484833";//"2097581";//"1000074";//"484833";//"107291";
        deviceId     = "ApiTest";//"API Test POS";
        deviceTypeId = "3";
        productId    = "";//"13005";//"13001";
        txTypeId     = "139";
        timeout      = "30";
        reqId        = "0";
        userId       = "0001";
    }

    public CspHeader(Boolean flag){
        if(flag)
            qpay_seed    = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();
    }

    public void setNullObjects()
    {
        this.qpay_entryMode     = null;
        this.qpay_tdc           = null;
        this.qpay_cardHolder    = null;
        this.qpay_longitude     = null;
        this.qpay_latitude      = null;
        this.merchantId         = null;
        this.deviceId           = null;
        this.deviceTypeId       = null;
        this.productId          = null;
        this.txTypeId           = null;
        this.timeout            = null;
        this.reqId              = null;
        this.userId             = null;
    }


    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_entryMode() {
        return qpay_entryMode;
    }

    public void setQpay_entryMode(String qpay_entryMode) {
        this.qpay_entryMode = qpay_entryMode;
    }

    public String getQpay_tdc() {
        return qpay_tdc;
    }

    public void setQpay_tdc(String qpay_tdc) {
        this.qpay_tdc = qpay_tdc;
    }

    public String getQpay_cardHolder() {
        return qpay_cardHolder;
    }

    public void setQpay_cardHolder(String qpay_cardHolder) {
        this.qpay_cardHolder = qpay_cardHolder;
    }

    public String getQpay_longitude() {
        return qpay_longitude;
    }

    public void setQpay_longitude(String qpay_longitude) {
        this.qpay_longitude = qpay_longitude;
    }

    public String getQpay_latitude() {
        return qpay_latitude;
    }

    public void setQpay_latitude(String qpay_latitude) {
        this.qpay_latitude = qpay_latitude;
    }

    public String getQpay_pin() {
        return qpay_pin;
    }

    public void setQpay_pin(String qpay_pin) {
        this.qpay_pin = qpay_pin;
    }

    public String getQpay_application_label() {
        return qpay_application_label;
    }

    public void setQpay_application_label(String qpay_application_label) {
        this.qpay_application_label = qpay_application_label;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTxTypeId() {
        return txTypeId;
    }

    public void setTxTypeId(String txTypeId) {
        this.txTypeId = txTypeId;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
