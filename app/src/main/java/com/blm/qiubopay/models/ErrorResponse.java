package com.blm.qiubopay.models;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private String internalCode;

    private String message;

    private String qpay_response;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(String internalCode, String message) {
        this.internalCode = internalCode;
        this.message = message;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQpay_response() {
        return qpay_response;
    }

    public void setQpay_response(String qpay_response) {
        this.qpay_response = qpay_response;
    }
}