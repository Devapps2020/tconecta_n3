package com.blm.qiubopay.models.pedidos;

import java.io.Serializable;

public class QPAY_GetCategories_Object implements Serializable {
    private int id;
    private String idCategory;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}