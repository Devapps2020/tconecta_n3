package com.blm.qiubopay.models.order;

import com.google.gson.annotations.SerializedName;

public class OrderIds {

    @SerializedName("id")
    private Integer id;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    @SerializedName("client_name")
    private String client_name;

    @SerializedName("client_cellphone")
    private String client_cellphone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_cellphone() {
        return client_cellphone;
    }

    public void setClient_cellphone(String client_cellphone) {
        this.client_cellphone = client_cellphone;
    }

}
