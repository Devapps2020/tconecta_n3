package com.blm.qiubopay.models.visa.response;

import java.io.Serializable;

public class QPAY_VisaEmvResponse implements Serializable {

    private String message;
    private CspHeader cspHeader;
    private CspBody cspBody;
    private String name;

    private String createdAt;
    private String responseAt;
    private String qpay_administrator_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CspHeader getCspHeader() {
        return cspHeader;
    }

    public void setCspHeader(CspHeader cspHeader) {
        this.cspHeader = cspHeader;
    }

    public CspBody getCspBody() {
        return cspBody;
    }

    public void setCspBody(CspBody cspBody) {
        this.cspBody = cspBody;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getQpay_administrator_id() {
        return qpay_administrator_id;
    }

    public void setQpay_administrator_id(String qpay_administrator_id) {
        this.qpay_administrator_id = qpay_administrator_id;
    }

}
