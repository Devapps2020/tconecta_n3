package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_NewUser implements Serializable {

    private String qpay_seed;

    private String qpay_name;
    private String qpay_father_surname;
    private String qpay_mother_surname;
    private String qpay_gender;
    private String qpay_birth_date;
    private String qpay_cellphone;
    private String qpay_mail;
    private String qpay_password;

    private String qpay_bimbo_id;
    private String qpay_qiubo_id;
    private String qpay_merchant_name;
    private String qpay_merchant_street;
    private String qpay_merchant_external_number;
    private String qpay_merchant_internal_number;
    private String qpay_merchant_postal_code;
    private String qpay_merchant_suburb;
    private String qpay_merchant_state;
    private String qpay_merchant_municipality;
    private String qpay_sepomex;
    private String qpay_primary_agency;

    private String qpay_promoter_id;
    private String qpay_fcmId;

    private String qpay_accepted_conditions_date;
    private String qpay_accepted_privacy_date;

    private String operative_type;

    private String login_type;
    private String social_token;
    private String client_channel_id;

    //Variables swap t1000
    private String qpay_folio;
    private String qpay_promotor;
    private String qpay_blm_id;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_NewUser(){
        qpay_device_info = new QPAY_DeviceInfo[1];
        QPAY_DeviceInfo deviceInfo = new QPAY_DeviceInfo();
        this.qpay_device_info[0] = deviceInfo;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
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

    public String getQpay_gender() {
        return qpay_gender;
    }

    public void setQpay_gender(String qpay_gender) {
        this.qpay_gender = qpay_gender;
    }

    public String getQpay_birth_date() {
        return qpay_birth_date;
    }

    public void setQpay_birth_date(String qpay_birth_date) {
        this.qpay_birth_date = qpay_birth_date;
    }

    public String getQpay_cellphone() {
        return qpay_cellphone;
    }

    public void setQpay_cellphone(String qpay_cellphone) {
        this.qpay_cellphone = qpay_cellphone;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_password() {
        return qpay_password;
    }

    public void setQpay_password(String qpay_password) {
        this.qpay_password = qpay_password;
    }

    public String getQpay_bimbo_id() {
        return qpay_bimbo_id;
    }

    public void setQpay_bimbo_id(String qpay_bimbo_id) {
        this.qpay_bimbo_id = qpay_bimbo_id;
    }

    public String getQpay_qiubo_id() {
        return qpay_qiubo_id;
    }

    public void setQpay_qiubo_id(String qpay_qiubo_id) {
        this.qpay_qiubo_id = qpay_qiubo_id;
    }

    public String getQpay_merchant_name() {
        return qpay_merchant_name;
    }

    public void setQpay_merchant_name(String qpay_merchant_name) {
        this.qpay_merchant_name = qpay_merchant_name;
    }

    public String getQpay_merchant_street() {
        return qpay_merchant_street;
    }

    public void setQpay_merchant_street(String qpay_merchant_street) {
        this.qpay_merchant_street = qpay_merchant_street;
    }

    public String getQpay_merchant_external_number() {
        return qpay_merchant_external_number;
    }

    public void setQpay_merchant_external_number(String qpay_merchant_external_number) {
        this.qpay_merchant_external_number = qpay_merchant_external_number;
    }

    public String getQpay_merchant_internal_number() {
        return qpay_merchant_internal_number;
    }

    public void setQpay_merchant_internal_number(String qpay_merchant_internal_number) {
        this.qpay_merchant_internal_number = qpay_merchant_internal_number;
    }

    public String getQpay_merchant_postal_code() {
        return qpay_merchant_postal_code;
    }

    public void setQpay_merchant_postal_code(String qpay_merchant_postal_code) {
        this.qpay_merchant_postal_code = qpay_merchant_postal_code;
    }

    public String getQpay_merchant_suburb() {
        return qpay_merchant_suburb;
    }

    public void setQpay_merchant_suburb(String qpay_merchant_suburb) {
        this.qpay_merchant_suburb = qpay_merchant_suburb;
    }

    public String getQpay_merchant_state() {
        return qpay_merchant_state;
    }

    public void setQpay_merchant_state(String qpay_merchant_state) {
        this.qpay_merchant_state = qpay_merchant_state;
    }

    public String getQpay_merchant_municipality() {
        return qpay_merchant_municipality;
    }

    public void setQpay_merchant_municipality(String qpay_merchant_municipality) {
        this.qpay_merchant_municipality = qpay_merchant_municipality;
    }

    public String getQpay_sepomex() {
        return qpay_sepomex;
    }

    public void setQpay_sepomex(String qpay_sepomex) {
        this.qpay_sepomex = qpay_sepomex;
    }

    public String getQpay_primary_agency() {
        return qpay_primary_agency;
    }

    public void setQpay_primary_agency(String qpay_primary_agency) {
        this.qpay_primary_agency = qpay_primary_agency;
    }

    /*public String getQpay_blm_id() {
        return qpay_blm_id;
    }

    public void setQpay_blm_id(String qpay_blm_id) {
        this.qpay_blm_id = qpay_blm_id;
    }

    public String getQpay_sugar_id() {
        return qpay_sugar_id;
    }

    public void setQpay_sugar_id(String qpay_sugar_id) {
        this.qpay_sugar_id = qpay_sugar_id;
    }
    */

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }

    public String getQpay_promoter_id() {
        return qpay_promoter_id;
    }

    public void setQpay_promoter_id(String qpay_promoter_id) {
        this.qpay_promoter_id = qpay_promoter_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getQpay_fcmId() {
        return qpay_fcmId;
    }

    public void setQpay_fcmId(String qpay_fcmId) {
        this.qpay_fcmId = qpay_fcmId;
    }

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }

    public String getClient_channel_id() {
        return client_channel_id;
    }

    public void setClient_channel_id(String client_channel_id) {
        this.client_channel_id = client_channel_id;
    }

    public String getQpay_accepted_conditions_date() {
        return qpay_accepted_conditions_date;
    }

    public void setQpay_accepted_conditions_date(String qpay_accepted_conditions_date) {
        this.qpay_accepted_conditions_date = qpay_accepted_conditions_date;
    }

    public String getQpay_accepted_privacy_date() {
        return qpay_accepted_privacy_date;
    }

    public void setQpay_accepted_privacy_date(String qpay_accepted_privacy_date) {
        this.qpay_accepted_privacy_date = qpay_accepted_privacy_date;
    }

    public String getOperative_type() {
        return operative_type;
    }

    public void setOperative_type(String operative_type) {
        this.operative_type = operative_type;
    }

    public String getQpay_folio() {
        return qpay_folio;
    }

    public void setQpay_folio(String qpay_folio) {
        this.qpay_folio = qpay_folio;
    }

    public String getQpay_promotor() {
        return qpay_promotor;
    }

    public void setQpay_promotor(String qpay_promotor) {
        this.qpay_promotor = qpay_promotor;
    }

    public String getQpay_blm_id() {
        return qpay_blm_id;
    }

    public void setQpay_blm_id(String qpay_blm_id) {
        this.qpay_blm_id = qpay_blm_id;
    }
}
