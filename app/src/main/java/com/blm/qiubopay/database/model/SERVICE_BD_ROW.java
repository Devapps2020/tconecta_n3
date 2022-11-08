package com.blm.qiubopay.database.model;

import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;

import java.io.Serializable;

public class SERVICE_BD_ROW implements Serializable {
    private String date;
    private String type;
    private QPAY_TaeSaleResponseFirst response1;
    private ServicePayment response2;

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

    public QPAY_TaeSaleResponseFirst getResponse1() {
        return response1;
    }

    public void setResponse1(QPAY_TaeSaleResponseFirst response1) {
        this.response1 = response1;
    }

    public ServicePayment getResponse2() {
        return response2;
    }

    public void setResponse2(ServicePayment response2) {
        this.response2 = response2;
    }
}
