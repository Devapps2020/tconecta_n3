package com.blm.qiubopay.responses;

import com.blm.qiubopay.responses.dynamicdata.DynamicDataCancelOrder;

import java.util.List;

public class ResponseCancelOrder {

    private String qpay_response, qpay_code, qpay_description;
    private List<DynamicDataCancelOrder> qpay_object;

    public String getQpay_response() {
        return qpay_response;
    }

    public void setQpay_response(String qpay_response) {
        this.qpay_response = qpay_response;
    }

    public String getQpay_code() {
        return qpay_code;
    }

    public void setQpay_code(String qpay_code) {
        this.qpay_code = qpay_code;
    }

    public String getQpay_description() {
        return qpay_description;
    }

    public void setQpay_description(String qpay_description) {
        this.qpay_description = qpay_description;
    }

    public List<DynamicDataCancelOrder> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<DynamicDataCancelOrder> qpay_object) {
        this.qpay_object = qpay_object;
    }

    /*private String responseCode, responseDescription;
    private DynamicDataCancelOrder dynamicData;
    private CustomerData customerData;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public DynamicDataCancelOrder getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicDataCancelOrder dynamicData) {
        this.dynamicData = dynamicData;
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    public void setCustomerData(CustomerData customerData) {
        this.customerData = customerData;
    }*/
}

