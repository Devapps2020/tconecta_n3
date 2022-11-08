package com.blm.qiubopay.models.profile;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.blm.qiubopay.models.QPAY_DeviceInfo;

import java.io.Serializable;

public class QPAY_Profile implements Serializable {

    private String qpay_status;
    private String qpay_username;
    private String qpay_mail;
    private String qpay_cellphone;
    private String qpay_name;
    private String qpay_father_surname;
    private String qpay_mother_surname;
    private String qpay_gender;
    private String qpay_birth_date;
    private String tooltip_chambitas;

    private String qpay_merchant_name;
    private String qpay_merchant_street;
    private String qpay_merchant_external_number;
    private String qpay_merchant_internal_number;
    private String qpay_merchant_postal_code;
    private String qpay_merchant_suburb;
    private String qpay_merchant_state;
    private String qpay_merchant_municipality;
    private String qpay_blm_id;
    private String qpay_bimbo_id;//0 no se dio de alta, 1 si se dio de alta
    //private String qpay_sugar_id;
    private String qpay_sepomex;
    //private String qpay_entityid;
    private String qpay_category_status_1;
    private String qpay_category_status_2;
    private String qpay_category_status_3;
    private String qpay_activation_1;//Sugar creation account 0.- inactivo 1.- activo 2.- Error //Cuenta creada sin servicio básico
    private String qpay_activation_2;//Sugar update created basic service //Servicio básico creado
    private String qpay_activation_3;//Sugar financial service created
    private String qpay_activation_4;//Kineto creation account
    private String qpay_activation_5;//Kineto accept terms and conditions
    private String qpay_activation_6;//Archivo MAS 0.- No se ha enviado 1.- Envío exitoso 2.-Error
    private String qpay_activation_7;//Alta de merchant Visa 0.- no se ha enviado 3.- Existe pero inactivo 2.- Error 1.- Merchant activo, se puede transaccionar.
    private String qpay_activation_8;//
    private String qpay_activation_12;//Campo que valida el estado de la migración del SP530

    private String qpay_cash_collection;

    private String qpay_praga_data1;//BUSINESS_ID
    private String qpay_praga_data2;//API_KEY
    private String qpay_praga_data3;//ENCRIPTION_KEY

    //private String qpay_visa_user;
    //private String qpay_kineto_user;

    private String qpay_seed;

    private String qpay_gateway_user;

    //Giro del negocio
    private String operative_type;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Variables Multiusuario
    private String qpay_user_type;
    private String qpay_link_status;
    private String qpay_link_code;
    private String qpay_link_alias;
    private Integer qpay_administrator_id;
    private String qpay_administrator_name;

    private String analytics_activation;

    //RSB 20200128. ENKO
    private String qpay_enko_user;
    private String qpay_enko_activation;

    private String qpay_account_number;

    //20200811 RSB. Validar Pidelo
    private String qpay_activation_pidelo;
    //20200813 RSB. Validar VAS con Financiero
    private String qpay_activation_vas_saldo;

    private String qpay_group;

    private boolean qpay_remember_user;

    private String qpay_enko_url;

    private String chatbotEnabled;

    //Apuestas deportivas
    private String qpay_activation_jazz_deportes;

    //Abono con tarjeta no presente
    private String ecommerce_activation;

    private String bo_order;

    private String b2c_activation;

    //remesas
    private String remittance_activation;

    //220506 RSB. App2Terminal Migration
    private String qpay_isSpartanMigration;

    public boolean userHaveTheCompleteRegistration(){
        boolean back = true;
        if(this.qpay_merchant_street == null
        && this.qpay_merchant_external_number == null
        && this.qpay_merchant_postal_code == null
        && this.qpay_merchant_municipality == null)
            back = false;
        return back;
    }

    public String getTooltip_chambitas() {
        return tooltip_chambitas;
    }

    public void setTooltip_chambitas(String tooltip_chambitas) {
        this.tooltip_chambitas = tooltip_chambitas;
    }

    public String getQpay_status() {
        return qpay_status;
    }

    public void setQpay_status(String qpay_status) {
        this.qpay_status = qpay_status;
    }

    public String getQpay_username() {
        return qpay_username;
    }

    public void setQpay_username(String qpay_username) {
        this.qpay_username = qpay_username;
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
        return qpay_merchant_external_number == null || qpay_merchant_external_number.trim().equals("null") ? "" : qpay_merchant_external_number.trim();
    }

    public void setQpay_merchant_external_number(String qpay_merchant_external_number) {
        this.qpay_merchant_external_number = qpay_merchant_external_number;
    }

    public String getQpay_merchant_internal_number() {
        return qpay_merchant_internal_number==null || qpay_merchant_internal_number.trim().equals("null") ? "" : qpay_merchant_internal_number.trim();
    }

    public void setQpay_merchant_internal_number(String qpay_merchant_internal_number) {
        this.qpay_merchant_internal_number = qpay_merchant_internal_number;
    }

    public String getQpay_merchant_postal_code() {
        return qpay_merchant_postal_code==null || qpay_merchant_postal_code.trim().equals("null") ? "" : qpay_merchant_postal_code.trim();
    }

    public void setQpay_merchant_postal_code(String qpay_merchant_postal_code) {
        this.qpay_merchant_postal_code = qpay_merchant_postal_code;
    }

    public String getQpay_merchant_suburb() {
        return qpay_merchant_suburb==null || qpay_merchant_suburb.trim().equals("null") ? "" : qpay_merchant_suburb.trim();
    }

    public void setQpay_merchant_suburb(String qpay_merchant_suburb) {
        this.qpay_merchant_suburb = qpay_merchant_suburb;
    }

    public String getQpay_merchant_state() {
        return qpay_merchant_state == null || qpay_merchant_state.trim().equals("null") ? "" : qpay_merchant_state.trim();
    }

    public void setQpay_merchant_state(String qpay_merchant_state) {
        this.qpay_merchant_state = qpay_merchant_state;
    }

    public String getQpay_merchant_municipality() {
        return qpay_merchant_municipality==null || qpay_merchant_municipality.trim().equals("null") ? "" : qpay_merchant_municipality.trim();
    }

    public void setQpay_merchant_municipality(String qpay_merchant_municipality) {
        this.qpay_merchant_municipality = qpay_merchant_municipality;
    }

    public String getQpay_bimbo_id() {
        return qpay_bimbo_id;
    }

    public void setQpay_bimbo_id(String qpay_bimbo_id) {
        this.qpay_bimbo_id = qpay_bimbo_id;
    }

    /*public String getQpay_sugar_id() {
        return qpay_sugar_id;
    }

    public void setQpay_sugar_id(String qpay_sugar_id) {
        this.qpay_sugar_id = qpay_sugar_id;
    }*/

    public String getQpay_sepomex() {
        return qpay_sepomex;
    }

    public void setQpay_sepomex(String qpay_sepomex) {
        this.qpay_sepomex = qpay_sepomex;
    }

    /*public String getQpay_entityid() {
        return qpay_entityid;
    }

    public void setQpay_entityid(String qpay_entityid) {
        this.qpay_entityid = qpay_entityid;
    }*/

    public String getQpay_category_status_1() {
        return qpay_category_status_1;
    }

    public void setQpay_category_status_1(String qpay_category_status_1) {
        this.qpay_category_status_1 = qpay_category_status_1;
    }

    public String getQpay_category_status_2() {
        return qpay_category_status_2;
    }

    public void setQpay_category_status_2(String qpay_category_status_2) {
        this.qpay_category_status_2 = qpay_category_status_2;
    }

    public String getQpay_category_status_3() {
        return qpay_category_status_3;
    }

    public void setQpay_category_status_3(String qpay_category_status_3) {
        this.qpay_category_status_3 = qpay_category_status_3;
    }

    public String getQpay_activation_1() {
        return qpay_activation_1;
    }

    public void setQpay_activation_1(String qpay_activation_1) {
        this.qpay_activation_1 = qpay_activation_1;
    }

    public String getQpay_activation_2() {
        return qpay_activation_2;
    }

    public void setQpay_activation_2(String qpay_activation_2) {
        this.qpay_activation_2 = qpay_activation_2;
    }

    public String getQpay_activation_3() {
        return qpay_activation_3;
    }

    public void setQpay_activation_3(String qpay_activation_3) {
        this.qpay_activation_3 = qpay_activation_3;
    }

    public String getQpay_activation_4() {
        return qpay_activation_4;
    }

    public void setQpay_activation_4(String qpay_activation_4) {
        this.qpay_activation_4 = qpay_activation_4;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public QPAY_DeviceInfo[] getQpay_device_info() {
        return qpay_device_info;
    }

    public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
        this.qpay_device_info = qpay_device_info;
    }

    public String getQpay_activation_5() {
        return qpay_activation_5;
    }

    public void setQpay_activation_5(String qpay_activation_5) {
        this.qpay_activation_5 = qpay_activation_5;
    }

    public String getQpay_activation_6() {
        return qpay_activation_6;
    }

    public void setQpay_activation_6(String qpay_activation_6) {
        this.qpay_activation_6 = qpay_activation_6;
    }

    public String getQpay_activation_7() {
        return qpay_activation_7;
    }

    public void setQpay_activation_7(String qpay_activation_7) {
        this.qpay_activation_7 = qpay_activation_7;
    }

    public String getQpay_activation_8() {
        return qpay_activation_8;
    }

    public void setQpay_activation_8(String qpay_activation_8) {
        this.qpay_activation_8 = qpay_activation_8;
    }

    public String getQpay_blm_id() {
        return qpay_blm_id;
    }

    public void setQpay_blm_id(String qpay_blm_id) {
        this.qpay_blm_id = qpay_blm_id;
    }

    /*public String getQpay_visa_user() {
        return qpay_visa_user;
    }

    public void setQpay_visa_user(String qpay_visa_user) {
        this.qpay_visa_user = qpay_visa_user;
    }

    public String getQpay_kineto_user() {
        return qpay_kineto_user;
    }

    public void setQpay_kineto_user(String qpay_kineto_user) {
        this.qpay_kineto_user = qpay_kineto_user;
    }*/

    public String getQpay_cash_collection() {
        return qpay_cash_collection;
    }

    public void setQpay_cash_collection(String qpay_cash_collection) {
        this.qpay_cash_collection = qpay_cash_collection;
    }

    public String getQpay_praga_data1() {
        return qpay_praga_data1;
    }

    public void setQpay_praga_data1(String qpay_praga_data1) {
        this.qpay_praga_data1 = qpay_praga_data1;
    }

    public String getQpay_praga_data2() {
        return qpay_praga_data2;
    }

    public void setQpay_praga_data2(String qpay_praga_data2) {
        this.qpay_praga_data2 = qpay_praga_data2;
    }

    public String getQpay_praga_data3() {
        return qpay_praga_data3;
    }

    public void setQpay_praga_data3(String qpay_praga_data3) {
        this.qpay_praga_data3 = qpay_praga_data3;
    }

    public String getQpay_gateway_user() {
        return qpay_gateway_user;
    }

    public void setQpay_gateway_user(String qpay_gateway_user) {
        this.qpay_gateway_user = qpay_gateway_user;
    }

    public String getQpay_activation_12() {
        return qpay_activation_12;
    }

    public void setQpay_activation_12(String qpay_activation_12) {
        this.qpay_activation_12 = qpay_activation_12;
    }

    public String getQpay_group() {
        return qpay_group;
    }

    public void setQpay_group(String qpay_group) {
        this.qpay_group = qpay_group;
    }

    public String getB2c_activation() {
        return b2c_activation;
    }

    public void setB2c_activation(String b2c_activation) {
        this.b2c_activation = b2c_activation;
    }

    public static class QPAY_ProfileExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_Profile.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
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

    public String getQpay_link_code() {
        return qpay_link_code;
    }

    public void setQpay_link_code(String qpay_link_code) {
        this.qpay_link_code = qpay_link_code;
    }

    public String getQpay_link_alias() {
        return qpay_link_alias;
    }

    public void setQpay_link_alias(String qpay_link_alias) {
        this.qpay_link_alias = qpay_link_alias;
    }

    public Integer getQpay_administrator_id() {
        return qpay_administrator_id;
    }

    public void setQpay_administrator_id(Integer qpay_administrator_id) {
        this.qpay_administrator_id = qpay_administrator_id;
    }

    public String getQpay_administrator_name() {
        return qpay_administrator_name;
    }

    public void setQpay_administrator_name(String qpay_administrator_name) {
        this.qpay_administrator_name = qpay_administrator_name;
    }

    public String getQpay_enko_user() {
        return qpay_enko_user;
    }

    public void setQpay_enko_user(String qpay_enko_user) {
        this.qpay_enko_user = qpay_enko_user;
    }

    public String getQpay_enko_activation() {
        return qpay_enko_activation;
    }

    public void setQpay_enko_activation(String qpay_enko_activation) {
        this.qpay_enko_activation = qpay_enko_activation;
    }

    public String getQpay_activation_pidelo() {
        return qpay_activation_pidelo;
    }

    public void setQpay_activation_pidelo(String qpay_activation_pidelo) {
        this.qpay_activation_pidelo = qpay_activation_pidelo;
    }

    public String getQpay_activation_vas_saldo() {
        return qpay_activation_vas_saldo;
    }

    public void setQpay_activation_vas_saldo(String qpay_activation_vas_saldo) {
        this.qpay_activation_vas_saldo = qpay_activation_vas_saldo;
    }

    public String getQpay_account_number() {
        return qpay_account_number;
    }

    public void setQpay_account_number(String qpay_account_number) {
        this.qpay_account_number = qpay_account_number;
    }

    public String getOperative_type() {
        return operative_type;
    }

    public void setOperative_type(String operative_type) {
        this.operative_type = operative_type;
    }
    public boolean isQpay_remember_user() {
        return qpay_remember_user;
    }

    public void setQpay_remember_user(boolean qpay_remember_user) {
        this.qpay_remember_user = qpay_remember_user;
    }

    public String getQpay_enko_url() {
        return qpay_enko_url;
    }

    public void setQpay_enko_url(String qpay_enko_url) {
        this.qpay_enko_url = qpay_enko_url;
    }

    public String getChatbotEnabled() {
        return chatbotEnabled;
    }

    public void setChatbotEnabled(String chatbotEnabled) {
        this.chatbotEnabled = chatbotEnabled;
    }

    public String getQpay_activation_jazz_deportes() {
        return qpay_activation_jazz_deportes;
    }

    public void setQpay_activation_jazz_deportes(String qpay_activation_jazz_deportes) {
        this.qpay_activation_jazz_deportes = qpay_activation_jazz_deportes;
    }

    public String getAnalytics_activation() {
        return analytics_activation;
    }

    public void setAnalytics_activation(String analytics_activation) {
        this.analytics_activation = analytics_activation;
    }

    public String getEcommerce_activation() {
        return ecommerce_activation;
    }

    public void setEcommerce_activation(String ecommerce_activation) {
        this.ecommerce_activation = ecommerce_activation;
    }

    public String getBo_order() {
        return bo_order;
    }

    public void setBo_order(String bo_order) {
        this.bo_order = bo_order;
    }

    public String getRemittance_activation() {
        return remittance_activation;
    }

    public void setRemittance_activation(String remittance_activation) {
        this.remittance_activation = remittance_activation;
    }

    public String getQpay_isSpartanMigration() {
        return qpay_isSpartanMigration;
    }

    public void setQpay_isSpartanMigration(String qpay_isSpartanMigration) {
        this.qpay_isSpartanMigration = qpay_isSpartanMigration;
    }
}
