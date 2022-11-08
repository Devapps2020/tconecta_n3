package com.blm.qiubopay.models.enko;

public class EnkoUser {

    private String first_name = "";
    private String last_name = "";
    private String ally_code = "";
    private String phone_number = "";
    private String token = "";
    private String detail = "";

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAlly_code() {
        return ally_code;
    }

    public void setAlly_code(String ally_code) {
        this.ally_code = ally_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

