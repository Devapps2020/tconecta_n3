package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_StartTxn_item implements Serializable {

    private String name;

    private String value;

    private String fieldType;

    private String text;

    private String returnn;

    public QPAY_StartTxn_item() {

    }

    public QPAY_StartTxn_item(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReturnn() {
        return returnn;
    }

    public void setReturnn(String returnn) {
        this.returnn = returnn;
    }
}