package com.blm.qiubopay.database.model;

import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.models.QPAY_StartTxn_object;

import java.io.Serializable;

public class QP_BD_ROW implements Serializable {
    private String date;
    private String type;
    private QPAY_StartTxn_object response1;
    private QPAY_Pago_Qiubo_object response2;

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

    public QPAY_StartTxn_object getResponse1() {
        return response1;
    }

    public void setResponse1(QPAY_StartTxn_object response1) {
        this.response1 = response1;
    }

    public QPAY_Pago_Qiubo_object getResponse2() {
        return response2;
    }

    public void setResponse2(QPAY_Pago_Qiubo_object response2) {
        this.response2 = response2;
    }
}
