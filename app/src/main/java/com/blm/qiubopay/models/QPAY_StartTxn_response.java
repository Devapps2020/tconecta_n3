package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_StartTxn_response implements Serializable {

    private String qpay_response;

    private String qpay_code;

    private String qpay_description;

    private QPAY_StartTxn_object[] qpay_object;

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

    public QPAY_StartTxn_object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_StartTxn_object[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}