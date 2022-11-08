package com.blm.qiubopay.models.soporte;

import java.io.Serializable;

public class QPAY_CommonError implements Serializable {

    private int id;
    private int position;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
