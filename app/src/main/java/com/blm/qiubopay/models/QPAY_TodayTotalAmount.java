package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_TodayTotalAmount implements Serializable {

    CsHeader cspHeader;
    private String qpay_seed;

    public CsHeader getCsHeader() {
        return cspHeader;
    }

    public void setCsHeader(CsHeader csHeaderObject) {
        this.cspHeader = csHeaderObject;
    }

    public CsHeader getCsHeaderObject() {
        return cspHeader;
    }

    public void setCsHeaderObject(CsHeader csHeaderObject) {
        cspHeader = csHeaderObject;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }
}
