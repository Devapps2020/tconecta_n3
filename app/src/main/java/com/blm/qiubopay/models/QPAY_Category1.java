package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_Category1 implements Serializable {
    private String qpay_seed;//":"n8v7xR+/Nz/O7F6DDNq2Ug==",

    private String qpay_birth_date;//":"123",
    private String qpay_age;//":"33",
    private String qpay_identification;//":"abc",
    private String qpay_identification_type;//":"efg",
    private String qpay_gender;//":"M",
    private String qpay_birth_state;//":"01-01-01",
    private String qpay_rfc;//":"aaa-bbb-ccc",
    private String qpay_curp;//":"ddd-eee-fff",
    private String qpay_birth_country;//":"02-02-02",
    private String qpay_nationality;//":"MX",
    private String qpay_occupation;//":"Dev",
    private String qpay_profession;//":"Lead",

    private String qpay_image_name_1;//":"ife_frente.jpg",
    private String qpay_image_1;
    private String qpay_image_name_2;//":"ife_detras.jpg",
    private String qpay_image_2;
    private String qpay_image_name_3;//":"comprobante_domicilio.jpg",
    private String qpay_image_3;

    private QPAY_DeviceInfo[] qpay_device_info;

    //Constructor
    public QPAY_Category1(){
        qpay_device_info = new QPAY_DeviceInfo[1];
        QPAY_DeviceInfo deviceInfo = new QPAY_DeviceInfo();
        this.qpay_device_info[0] = deviceInfo;

        this.qpay_image_name_1 = "identification_front.png";
        this.qpay_image_name_2 = "identification_behind.png";
        this.qpay_image_name_3 = "proof_of_address.png";

        this.qpay_seed = "";
        this.qpay_birth_date = "";
        this.qpay_age = "";
        this.qpay_identification = "";
        this.qpay_identification_type = "";
        this.qpay_gender = "";
        this.qpay_birth_state = "";
        this.qpay_rfc = "";
        this.qpay_curp = "";
        this.qpay_birth_country = "";
        this.qpay_nationality = "";
        this.qpay_occupation = "";
        this.qpay_profession = "";

        this.qpay_image_1 = "";
        this.qpay_image_2 = "";
        this.qpay_image_3 = "";
}

public Boolean isComplete(){
Boolean response = true;

if(this.qpay_image_1.equals("")
        || this.qpay_image_2.equals("")
        || this.qpay_image_3.equals("")
        || this.qpay_seed.equals("")
        || this.qpay_birth_date.equals("")
        || this.qpay_age.equals("")
        || this.qpay_identification.equals("")
        || this.qpay_identification_type.equals("")
        || this.qpay_gender.equals("")
        || this.qpay_birth_state.equals("")
        || this.qpay_rfc.equals("")
        || this.qpay_curp.equals("")
        || this.qpay_birth_country .equals("")
        || this.qpay_nationality.equals("")
        || this.qpay_occupation .equals("")
        || this.qpay_profession.equals("")
        || this.qpay_image_1.equals("")
        || this.qpay_image_2.equals("")
        || this.qpay_image_3.equals(""))
    response = false;

return response;
}

public String getQpay_seed() {
return qpay_seed;
}

public void setQpay_seed(String qpay_seed) {
this.qpay_seed = qpay_seed;
}

public String getQpay_birth_date() {
return qpay_birth_date;
}

public void setQpay_birth_date(String qpay_birth_date) {
this.qpay_birth_date = qpay_birth_date;
}

public String getQpay_age() {
return qpay_age;
}

public void setQpay_age(String qpay_age) {
this.qpay_age = qpay_age;
}

public String getQpay_identification() {
return qpay_identification;
}

public void setQpay_identification(String qpay_identification) {
this.qpay_identification = qpay_identification;
}

public String getQpay_identification_type() {
return qpay_identification_type;
}

public void setQpay_identification_type(String qpay_identification_type) {
this.qpay_identification_type = qpay_identification_type;
}

public String getQpay_gender() {
return qpay_gender;
}

public void setQpay_gender(String qpay_gender) {
this.qpay_gender = qpay_gender;
}

public String getQpay_birth_state() {
return qpay_birth_state;
}

public void setQpay_birth_state(String qpay_birth_state) {
this.qpay_birth_state = qpay_birth_state;
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

public String getQpay_occupation() {
return qpay_occupation;
}

public void setQpay_occupation(String qpay_occupation) {
this.qpay_occupation = qpay_occupation;
}

public String getQpay_profession() {
return qpay_profession;
}

public void setQpay_profession(String qpay_profession) {
this.qpay_profession = qpay_profession;
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

public QPAY_DeviceInfo[] getQpay_device_info() {
return qpay_device_info;
}

public void setQpay_device_info(QPAY_DeviceInfo[] qpay_device_info) {
this.qpay_device_info = qpay_device_info;
}
}
