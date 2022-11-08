package com.blm.qiubopay.responses;

import com.blm.qiubopay.responses.dynamicdata.DynamicDataDistributor;

import java.util.List;

public class ResponseDistribuitor {

    private String qpay_response, qpay_code, qpay_description;
    private List<DynamicDataDistributor> qpay_object;

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

    public List<DynamicDataDistributor> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<DynamicDataDistributor> qpay_object) {
        this.qpay_object = qpay_object;
    }

    /*private String qpay_response, qpay_code, qpay_description;
    private List<List<Distributor>> qpay_object;

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

    public List<List<Distributor>> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<List<Distributor>> qpay_object) {
        this.qpay_object = qpay_object;
    }*/

    /*private String responseCode, responseDescription;
    private DynamicDataDistributor dynamicData;

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

    public DynamicDataDistributor getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicDataDistributor dynamicData) {
        this.dynamicData = dynamicData;
    }*/
}
