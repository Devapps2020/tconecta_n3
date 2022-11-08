package com.blm.qiubopay.models.publicity;



import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TipsAdvertising_object extends QPAY_BaseResponse implements Serializable {

    private Integer id;
    private String name;
    private String activation_date;
    private String expiration_date;
    private String status;

    private QPAY_TipsAdvertising_publicities[] publicities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivation_date() {
        return activation_date;
    }

    public void setActivation_date(String activation_date) {
        this.activation_date = activation_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public QPAY_TipsAdvertising_publicities[] getPublicities() {
        return publicities;
    }

    public void setPublicities(QPAY_TipsAdvertising_publicities[] publicities) {
        this.publicities = publicities;
    }

}