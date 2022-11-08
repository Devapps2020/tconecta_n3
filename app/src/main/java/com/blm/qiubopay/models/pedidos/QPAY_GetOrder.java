package com.blm.qiubopay.models.pedidos;

import java.io.Serializable;

public class QPAY_GetOrder implements Serializable {

    private String idBimbo;
    private String idCategory;
    private String shortName;
    private String sku;
    private String idOrganization;
    private String minDate;
    private String maxDate;

    public String getIdBimbo() {
        return idBimbo;
    }

    public void setIdBimbo(String idBimbo) {
        this.idBimbo = idBimbo;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(String idOrganization) {
        this.idOrganization = idOrganization;
    }

    public String getMinDate() {
        return minDate;
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }
}