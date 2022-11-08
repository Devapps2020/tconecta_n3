package com.blm.qiubopay.models.base;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseResponse<T> {
    @SerializedName("qpay_response")
    private String qpayResponse;
    @SerializedName("qpay_code")
    private String qpayCode;
    @SerializedName("qpay_description")
    private String qpayDescription;
    @SerializedName("qpay_object")
    private ArrayList<T> qpayObject;

    public BaseResponse(String qpayResponse, String qpayCode, String qpayDescription, ArrayList<T> qpayObject) {
        this.qpayResponse = qpayResponse;
        this.qpayCode = qpayCode;
        this.qpayDescription = qpayDescription;
        this.setQpayObject(qpayObject);
    }

    public String getQpayResponse() {
        return qpayResponse;
    }

    public void setQpayResponse(String qpayResponse) {
        this.qpayResponse = qpayResponse;
    }

    public String getQpayCode() {
        return qpayCode;
    }

    public void setQpayCode(String qpayCode) {
        this.qpayCode = qpayCode;
    }

    public String getQpayDescription() {
        return qpayDescription;
    }

    public void setQpayDescription(String qpayDescription) {
        this.qpayDescription = qpayDescription;
    }

    public ArrayList<T> getQpayObject() {
        return qpayObject;
    }

    public void setQpayObject(ArrayList<T> qpayObject) {
        this.qpayObject = qpayObject;
    }

}
