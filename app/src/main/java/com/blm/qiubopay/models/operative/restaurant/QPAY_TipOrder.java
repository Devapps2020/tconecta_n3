package com.blm.qiubopay.models.operative.restaurant;

import java.io.Serializable;

public class QPAY_TipOrder implements Serializable {
    private String qpay_seed;
    private String sell_number;
    //private long user_id;
    private String creation_date;
    private Double total;
    private QPAY_TipDetail[] tip_detail;
    private String people_amount;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getSell_number() {
        return sell_number;
    }

    public void setSell_number(String sell_number) {
        this.sell_number = sell_number;
    }

    /*public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }*/

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public QPAY_TipDetail[] getTip_detail() {
        return tip_detail;
    }

    public void setTip_detail(QPAY_TipDetail[] tip_detail) {
        this.tip_detail = tip_detail;
    }

    public String getPeople_amount() {
        return people_amount;
    }

    public void setPeople_amount(String people_amount) {
        this.people_amount = people_amount;
    }
}
