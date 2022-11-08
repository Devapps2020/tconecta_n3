package com.blm.qiubopay.database.model;

import com.blm.qiubopay.models.mitec.CustomMitecTransaction;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;

import java.io.Serializable;

public class FINANCIAL_BD_ROW implements Serializable {

    private String date;
    private String type;
    private CustomMitecTransaction response1;
    private QPAY_VisaEmvResponse response2;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CustomMitecTransaction getResponse1() {
        return response1;
    }

    public void setResponse1(CustomMitecTransaction response1) {
        this.response1 = response1;
    }

    public QPAY_VisaEmvResponse getResponse2() {
        return response2;
    }

    public void setResponse2(QPAY_VisaEmvResponse response2) {
        this.response2 = response2;
    }
}
