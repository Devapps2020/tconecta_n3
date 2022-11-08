package com.blm.qiubopay.models.operative.restaurant;

import com.blm.qiubopay.models.operative.OperativeStatus;

import java.io.Serializable;

public class DbOrder implements Serializable {

    private int order_id;
    private String date;
    private String hour;
    private Double amount;
    private int commensal_number;
    private OperativeStatus status;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getCommensal_number() {
        return commensal_number;
    }

    public void setCommensal_number(int commensal_number) {
        this.commensal_number = commensal_number;
    }

    public OperativeStatus getStatus() {
        return status;
    }

    public void setStatus(OperativeStatus status) {
        this.status = status;
    }
}
