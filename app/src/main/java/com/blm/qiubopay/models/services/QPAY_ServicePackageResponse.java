package com.blm.qiubopay.models.services;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class QPAY_ServicePackageResponse  extends QPAY_BaseResponse implements Serializable {

    private String createdAt;
    private String responseAt;
    private ArrayList<ServicePackageResponse> qpay_object;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public ArrayList<ServicePackageResponse> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(ArrayList<ServicePackageResponse> qpay_object) {
        this.qpay_object = qpay_object;
    }
}