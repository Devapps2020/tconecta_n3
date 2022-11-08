package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_FinancialInformation implements Serializable {

    private String qpay_seed;

    private String qpay_account_action;//1 es cuenta existente //2 es nueva cuenta.
    private String qpay_name;
    private String qpay_father_surname;
    private String qpay_mother_surname;
    private String qpay_rfc;
    private String qpay_curp;
    private String qpay_gender;
    private String qpay_birth_date;
    private String qpay_cellphone;
    private String qpay_mail;

    private String qpay_bank_name;

    private String qpay_street;
    private String qpay_external_number;
    private String qpay_internal_number;
    private String qpay_postal_code;
    private String qpay_suburb;
    private String qpay_state;
    private String qpay_municipality;
    private String qpay_sepomex; //0 no se uso //1 si se uso

    private String qpay_birth_country;
    private String qpay_nationality;
    private String qpay_identification_type;
    private String qpay_identification;

    private String qpay_image_name_1;
    private String qpay_image_1;
    private String qpay_image_name_2;
    private String qpay_image_2;
    private String qpay_image_name_3;
    private String qpay_image_3;
    private String qpay_image_name_4;
    private String qpay_image_4;

    private String qpay_account_number;

    private QPAY_DeviceInfo[] qpay_device_info;

    public QPAY_FinancialInformation() {
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

    public String getQpay_account_action() {
        return qpay_account_action;
    }

    public void setQpay_account_action(String qpay_account_action) {
        this.qpay_account_action = qpay_account_action;
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

    public String getQpay_rfc() {
        return qpay_rfc;
    }

    public void setQpay_rfc(String qpay_rfc) {
        this.qpay_rfc = qpay_rfc;
    }

    public String getQpay_curp() {
        return qpay_curp;
    }

    public void setQpay_curp(String qpay_curp) {
        this.qpay_curp = qpay_curp;
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

    public String getQpay_street() {
        return qpay_street;
    }

    public void setQpay_street(String qpay_street) {
        this.qpay_street = qpay_street;
    }

    public String getQpay_external_number() {
        return qpay_external_number;
    }

    public void setQpay_external_number(String qpayexternal_number) {
        this.qpay_external_number = qpayexternal_number;
    }

    public String getQpay_internal_number() {
        return qpay_internal_number;
    }

    public void setQpay_internal_number(String qpay_internal_number) {
        this.qpay_internal_number = qpay_internal_number;
    }

    public String getQpay_postal_code() {
        return qpay_postal_code;
    }

    public void setQpay_postal_code(String qpay_postal_code) {
        this.qpay_postal_code = qpay_postal_code;
    }

    public String getQpay_suburb() {
        return qpay_suburb;
    }

    public void setQpay_suburb(String qpay_suburb) {
        this.qpay_suburb = qpay_suburb;
    }

    public String getQpay_state() {
        return qpay_state;
    }

    public void setQpay_state(String qpay_state) {
        this.qpay_state = qpay_state;
    }

    public String getQpay_municipality() {
        return qpay_municipality;
    }

    public void setQpay_municipality(String qpay_municipality) {
        this.qpay_municipality = qpay_municipality;
    }

    public String getQpay_sepomex() {
        return qpay_sepomex;
    }

    public void setQpay_sepomex(String qpay_sepomex) {
        this.qpay_sepomex = qpay_sepomex;
    }

    public String getQpay_birth_country() {
        return qpay_birth_country;
    }

    public void setQpay_birth_country(String qpay_birth_country) {
        this.qpay_birth_country = qpay_birth_country;
    }

    public String getQpay_nationality() {
        return qpay_nationality;
    }

    public void setQpay_nationality(String qpay_nationality) {
        this.qpay_nationality = qpay_nationality;
    }

    public String getQpay_identification_type() {
        return qpay_identification_type;
    }

    public void setQpay_identification_type(String qpay_identification_type) {
        this.qpay_identification_type = qpay_identification_type;
    }

    public String getQpay_identification() {
        return qpay_identification;
    }

    public void setQpay_identification(String qpay_identification) {
        this.qpay_identification = qpay_identification;
    }

    public String getQpay_image_name_1() {
        return qpay_image_name_1;
    }

    public void setQpay_image_name_1(String qpay_image_name_1) {
        this.qpay_image_name_1 = qpay_image_name_1;
    }

    public String getQpay_image_1() {
        return qpay_image_1;
    }

    public void setQpay_image_1(String qpay_image_1) {
        this.qpay_image_1 = qpay_image_1;
    }

    public String getQpay_image_name_2() {
        return qpay_image_name_2;
    }

    public void setQpay_image_name_2(String qpay_image_name_2) {
        this.qpay_image_name_2 = qpay_image_name_2;
    }

    public String getQpay_image_2() {
        return qpay_image_2;
    }

    public void setQpay_image_2(String qpay_image_2) {
        this.qpay_image_2 = qpay_image_2;
    }

    public String getQpay_image_name_3() {
        return qpay_image_name_3;
    }

    public void setQpay_image_name_3(String qpay_image_name_3) {
        this.qpay_image_name_3 = qpay_image_name_3;
    }

    public String getQpay_image_3() {
        return qpay_image_3;
    }

    public void setQpay_image_3(String qpay_image_3) {
        this.qpay_image_3 = qpay_image_3;
    }

    public String getQpay_image_name_4() {
        return qpay_image_name_4;
    }

    public void setQpay_image_name_4(String qpay_image_name_4) {
        this.qpay_image_name_4 = qpay_image_name_4;
    }

    public String getQpay_image_4() {
        return qpay_image_4;
    }

    public void setQpay_image_4(String qpay_image_4) {
        this.qpay_image_4 = qpay_image_4;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }

    public String getQpay_account_number() {
        return qpay_account_number;
    }

    public void setQpay_account_number(String qpay_account_number) {
        this.qpay_account_number = qpay_account_number;
    }

    public String getQpay_bank_name() {
        return qpay_bank_name;
    }

    public void setQpay_bank_name(String qpay_bank_name) {
        this.qpay_bank_name = qpay_bank_name;
    }
}
