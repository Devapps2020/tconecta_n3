package com.blm.qiubopay.models.carousel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QPayBaseResponse {

    @SerializedName("qpay_response")
    private String qpayResponse;
    @SerializedName("qpay_code")
    private String qpayCode;
    @SerializedName("qpay_description")
    private String qpayDescription;
    @SerializedName("qpay_object")
    private ArrayList<QPayObjectResponse> qpayObject;

    public QPayBaseResponse(String qpayResponse, String qpayCode, String qpayDescription, ArrayList<QPayObjectResponse> qpayObject) {
        this.qpayResponse = qpayResponse;
        this.qpayCode = qpayCode;
        this.qpayDescription = qpayDescription;
        this.qpayObject = qpayObject;
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

    public ArrayList<QPayObjectResponse> getQpayObject() {
        return qpayObject;
    }

    public void setQpayObject(ArrayList<QPayObjectResponse> qpayObject) {
        this.qpayObject = qpayObject;
    }
}
