package com.blm.qiubopay.models.fiado;

import java.io.Serializable;

public class QPAY_Cliente implements Serializable {

    private String fiado_mail;
    private String fiado_name;
    private String fiado_cellphone;

    private String fiado_host;
    private String fiado_clientid;

    private String fiado_debt_days;
    private String fiado_debt_interest;
    private String fiado_debt_id;

    private String id;
    private String maxAmount;
    private String paymentDays;
    private String interest;
    private Boolean hasDebts;
    private String createdAt;

    private String fiado_amount;
    private String fiado_payment_id;
    private String fiado_desc;

    private String fiado_new_mail;
    private String fiado_new_cellphone;

    public String getFiado_mail() {
        return fiado_mail;
    }

    public void setFiado_mail(String fiado_mail) {
        this.fiado_mail = fiado_mail;
    }

    public String getFiado_name() {
        return fiado_name;
    }

    public void setFiado_name(String fiado_name) {
        this.fiado_name = fiado_name;
    }

    public String getFiado_cellphone() {
        return fiado_cellphone;
    }

    public void setFiado_cellphone(String fiado_cellphone) {
        this.fiado_cellphone = fiado_cellphone;
    }

    public String getFiado_host() {
        return fiado_host;
    }

    public void setFiado_host(String fiado_host) {
        this.fiado_host = fiado_host;
    }

    public String getFiado_clientid() {
        return fiado_clientid;
    }

    public void setFiado_clientid(String fiado_clientid) {
        this.fiado_clientid = fiado_clientid;
    }

    public String getFiado_debt_days() {
        return fiado_debt_days;
    }

    public void setFiado_debt_days(String fiado_debt_days) {
        this.fiado_debt_days = fiado_debt_days;
    }

    public String getFiado_debt_interest() {
        return fiado_debt_interest;
    }

    public void setFiado_debt_interest(String fiado_debt_interest) {
        this.fiado_debt_interest = fiado_debt_interest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getPaymentDays() {
        return paymentDays;
    }

    public void setPaymentDays(String paymentDays) {
        this.paymentDays = paymentDays;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Boolean getHasDebts() {
        return hasDebts;
    }

    public void setHasDebts(Boolean hasDebts) {
        this.hasDebts = hasDebts;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFiado_amount() {
        return fiado_amount;
    }

    public void setFiado_amount(String fiado_amount) {
        this.fiado_amount = fiado_amount;
    }

    public String getFiado_payment_id() {
        return fiado_payment_id;
    }

    public void setFiado_payment_id(String fiado_payment_id) {
        this.fiado_payment_id = fiado_payment_id;
    }

    public String getFiado_desc() {
        return fiado_desc;
    }

    public void setFiado_desc(String fiado_desc) {
        this.fiado_desc = fiado_desc;
    }

    public String getFiado_debt_id() {
        return fiado_debt_id;
    }

    public void setFiado_debt_id(String fiado_debt_id) {
        this.fiado_debt_id = fiado_debt_id;
    }

    public String getFiado_new_mail() {
        return fiado_new_mail;
    }

    public void setFiado_new_mail(String fiado_new_mail) {
        this.fiado_new_mail = fiado_new_mail;
    }

    public String getFiado_new_cellphone() {
        return fiado_new_cellphone;
    }

    public void setFiado_new_cellphone(String fiado_new_cellphone) {
        this.fiado_new_cellphone = fiado_new_cellphone;
    }
}
