package com.blm.qiubopay.models.ahorros;

import java.io.Serializable;

public class QPAY_GetUserSaving_Object implements Serializable {
    private float id;
    private float saving_percentage;
    private float saving_percentage_new;
    private String new_percentage_date;


    // Getter Methods

    public float getId() {
        return id;
    }

    public float getSaving_percentage() {
        return saving_percentage;
    }

    public float getSaving_percentage_new() {
        return saving_percentage_new;
    }

    public String getNew_percentage_date() {
        return new_percentage_date;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setSaving_percentage(float saving_percentage) {
        this.saving_percentage = saving_percentage;
    }

    public void setSaving_percentage_new(float saving_percentage_new) {
        this.saving_percentage_new = saving_percentage_new;
    }

    public void setNew_percentage_date(String new_percentage_date) {
        this.new_percentage_date = new_percentage_date;
    }

}