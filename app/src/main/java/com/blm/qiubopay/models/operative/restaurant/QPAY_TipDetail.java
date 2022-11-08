package com.blm.qiubopay.models.operative.restaurant;

import java.io.Serializable;

public class QPAY_TipDetail implements Serializable {
    private String payment_type;
    private String rsp_rrn;
    private String rsp_auth_num;
    private String rsp_tx_date;
    private Double amount;
    private Double tip_amount;
    private Double tip_percent;
    private Double total;
    private String people_amount_detail;

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getRsp_rrn() {
        return rsp_rrn;
    }

    public void setRsp_rrn(String rsp_rrn) {
        this.rsp_rrn = rsp_rrn;
    }

    public String getRsp_auth_num() {
        return rsp_auth_num;
    }

    public void setRsp_auth_num(String rsp_auth_num) {
        this.rsp_auth_num = rsp_auth_num;
    }

    public String getRsp_tx_date() {
        return rsp_tx_date;
    }

    public void setRsp_tx_date(String rsp_tx_date) {
        this.rsp_tx_date = rsp_tx_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTip_amount() {
        return tip_amount;
    }

    public void setTip_amount(Double tip_amount) {
        this.tip_amount = tip_amount;
    }

    public Double getTip_percent() {
        return tip_percent;
    }

    public void setTip_percent(Double tip_percent) {
        this.tip_percent = tip_percent;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPeople_amount_detail() {
        return people_amount_detail;
    }

    public void setPeople_amount_detail(String people_amount_detail) {
        this.people_amount_detail = people_amount_detail;
    }
}
