package com.blm.qiubopay.models.operative.restaurant;

import com.blm.qiubopay.models.operative.PayType;

import java.io.Serializable;

public class DbDetail implements Serializable {
    private int fk_order;
    private String date;
    private PayType payment_type;
    private int commensals_no;
    private String folio;
    private String auth;
    private Double amount;
    private Double tip_amount;
    private Double tip_percent;
    private Double total;

    public int getFk_order() {
        return fk_order;
    }

    public void setFk_order(int fk_order) {
        this.fk_order = fk_order;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PayType getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(PayType payment_type) {
        this.payment_type = payment_type;
    }

    public int getCommensals_no() {
        return commensals_no;
    }

    public void setCommensals_no(int commensals_no) {
        this.commensals_no = commensals_no;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
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

    public String getStringPaymentType()
    {
        String back = "";
        if(getPayment_type() == PayType.CREDIT)
            back = "Credito";
        else if(getPayment_type() == PayType.DEBIT)
            back = "Debito";
        else if(getPayment_type() == PayType.CASH)
            back = "Efectivo";

        return back;
    }

    public QPAY_TipDetail toTipDetail(){
        QPAY_TipDetail tipDetail = new QPAY_TipDetail();

        tipDetail.setAmount(getAmount());
        tipDetail.setTip_percent(getTip_percent());
        tipDetail.setTip_amount(getTip_amount());
        tipDetail.setTotal(getTotal());
        tipDetail.setRsp_tx_date(getDate());
        tipDetail.setRsp_rrn(getFolio().equals("ND")?"0":getFolio());
        tipDetail.setRsp_auth_num(getAuth().equals("ND")?"0":getAuth());
        tipDetail.setPayment_type(getStringPaymentType());
        //20210115 RSB. Improvements 0121. Ticket restaurante
        tipDetail.setPeople_amount_detail(""+getCommensals_no());

        return tipDetail;
    }
}
