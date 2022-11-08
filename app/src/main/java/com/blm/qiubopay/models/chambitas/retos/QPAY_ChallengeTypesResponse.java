package com.blm.qiubopay.models.chambitas.retos;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_ChallengeTypesResponse extends QPAY_BaseResponse implements Serializable {

    private String qpay_response;
    private String qpay_code;
    private String qpay_description;



    // Getter Methods

    public String getQpay_response() {
        return qpay_response;
    }

    public String getQpay_code() {
        return qpay_code;
    }

    public String getQpay_description() {
        return qpay_description;
    }

    // Setter Methods

    public void setQpay_response(String qpay_response) {
        this.qpay_response = qpay_response;
    }

    public void setQpay_code(String qpay_code) {
        this.qpay_code = qpay_code;
    }

    public void setQpay_description(String qpay_description) {
        this.qpay_description = qpay_description;
    }

}