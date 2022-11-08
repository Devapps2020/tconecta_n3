package com.blm.qiubopay.models.services;

import com.blm.qiubopay.models.QPAY_DeviceInfo;

import java.io.Serializable;

public class QPAY_ServicePayment implements Serializable {

    private String qpay_seed;
    private String qpay_name_product;
    private String qpay_name_client;

    private String qpay_account_number;
    private String qpay_verification_digit;
    private String qpay_amount;
    private String qpay_product;
    private String qpay_product_id;
    private String qpay_account_number1;
    private String qpay_account_number2;
    private String qpay_account_number3;

    private String qpay_reference_number;
    private String qpay_type_search;

    private String qpay_vendorReference;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_ServicePayment(){

        qpay_seed               = null;
        qpay_account_number     = null;
        qpay_verification_digit = null;
        qpay_amount             = null;
        qpay_product            = null;
        qpay_account_number1    = null;
        qpay_account_number2    = null;
        qpay_account_number3    = null;
        qpay_reference_number   = null;
        qpay_type_search        = null;

        qpay_device_info = new  QPAY_DeviceInfo[1];
        QPAY_DeviceInfo deviceInfo = new QPAY_DeviceInfo();
        this.qpay_device_info[0] = deviceInfo;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_account_number() {
        return qpay_account_number;
    }

    public void setQpay_account_number(String qpay_account_number) {
        this.qpay_account_number = qpay_account_number;
    }

    public String getQpay_verification_digit() {
        return qpay_verification_digit;
    }

    public void setQpay_verification_digit(String qpay_verification_digit) {
        this.qpay_verification_digit = qpay_verification_digit;
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

    public String getQpay_account_number1() {
        return qpay_account_number1;
    }

    public void setQpay_account_number1(String qpay_account_number1) {
        this.qpay_account_number1 = qpay_account_number1;
    }

    public String getQpay_account_number2() {
        return qpay_account_number2;
    }

    public void setQpay_account_number2(String qpay_account_number2) {
        this.qpay_account_number2 = qpay_account_number2;
    }

    public String getQpay_account_number3() {
        return qpay_account_number3;
    }

    public void setQpay_account_number3(String qpay_account_number3) {
        this.qpay_account_number3 = qpay_account_number3;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }

    public String getQpay_reference_number() {
        return qpay_reference_number;
    }

    public void setQpay_reference_number(String qpay_reference_number) {
        this.qpay_reference_number = qpay_reference_number;
    }

    public String getQpay_type_search() {
        return qpay_type_search;
    }

    public void setQpay_type_search(String qpay_type_search) {
        this.qpay_type_search = qpay_type_search;
    }

    public String getQpay_vendorReference() {
        return qpay_vendorReference;
    }

    public void setQpay_vendorReference(String qpay_vendorReference) {
        this.qpay_vendorReference = qpay_vendorReference;
    }

    public String getQpay_name_product() {
        return qpay_name_product;
    }

    public void setQpay_name_product(String qpay_name_product) {
        this.qpay_name_product = qpay_name_product;
    }

    public String getQpay_name_client() {
        return qpay_name_client;
    }

    public void setQpay_name_client(String qpay_name_client) {
        this.qpay_name_client = qpay_name_client;
    }

    public String getQpay_product_id() {
        return qpay_product_id;
    }

    public void setQpay_product_id(String qpay_product_id) {
        this.qpay_product_id = qpay_product_id;
    }

    public void reset(){
        qpay_seed               = null;
        qpay_account_number     = null;
        qpay_verification_digit = null;
        qpay_amount             = null;
        qpay_product            = null;
        qpay_account_number1    = null;
        qpay_account_number2    = null;
        qpay_account_number3    = null;
        qpay_reference_number   = null;
        qpay_type_search        = null;
    }
}
