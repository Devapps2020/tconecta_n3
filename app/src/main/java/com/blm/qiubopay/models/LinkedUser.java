package com.blm.qiubopay.models;

import java.io.Serializable;

public class LinkedUser implements Serializable {

    private Integer qpay_id;
    private String qpay_status;
    private String qpay_mail;
    private String qpay_cellphone;
    private String qpay_name;
    private String qpay_father_surname;
    private String qpay_mother_surname;
    private String qpay_merchant_name;
    private String qpay_user_type;
    private String qpay_link_status;
    private String qpay_link_alias;


    public Integer getQpay_id() {
        return qpay_id;
    }

    public void setQpay_id(Integer qpay_id) {
        this.qpay_id = qpay_id;
    }

    public String getQpay_status() {
        return qpay_status;
    }

    public void setQpay_status(String qpay_status) {
        this.qpay_status = qpay_status;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_cellphone() {
        return qpay_cellphone;
    }

    public void setQpay_cellphone(String qpay_cellphone) {
        this.qpay_cellphone = qpay_cellphone;
    }

    public String getQpay_name() {
        return qpay_name;
    }

    public void setQpay_name(String qpay_name) {
        this.qpay_name = qpay_name;
    }

    public String getQpay_father_surname() {
        return qpay_father_surname;
    }

    public void setQpay_father_surname(String qpay_father_surname) {
        this.qpay_father_surname = qpay_father_surname;
    }

    public String getQpay_mother_surname() {
        return qpay_mother_surname;
    }

    public void setQpay_mother_surname(String qpay_mother_surname) {
        this.qpay_mother_surname = qpay_mother_surname;
    }

    public String getQpay_merchant_name() {
        return qpay_merchant_name;
    }

    public void setQpay_merchant_name(String qpay_merchant_name) {
        this.qpay_merchant_name = qpay_merchant_name;
    }

    public String getQpay_user_type() {
        return qpay_user_type;
    }

    public void setQpay_user_type(String qpay_user_type) {
        this.qpay_user_type = qpay_user_type;
    }

    public String getQpay_link_status() {
        return qpay_link_status;
    }

    public void setQpay_link_status(String qpay_link_status) {
        this.qpay_link_status = qpay_link_status;
    }


    public String getQpay_link_alias(){return qpay_link_alias;}

    public void setQpay_link_alias(String qpay_link_alias) {
        this.qpay_link_alias = qpay_link_alias;
    }

}
