package com.blm.qiubopay.database.model;

import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.models.tae.TaeSale;

import java.io.Serializable;

public class TAE_BD_ROW implements Serializable {

    private String date;
    private String carrier;
    private QPAY_TaeSaleResponseFirst response1;
    private TaeSale response2;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public QPAY_TaeSaleResponseFirst getResponse1() {
        return response1;
    }

    public void setResponse1(QPAY_TaeSaleResponseFirst response1) {
        this.response1 = response1;
    }

    public TaeSale getResponse2() {
        return response2;
    }

    public void setResponse2(TaeSale response2) {
        this.response2 = response2;
    }
}
