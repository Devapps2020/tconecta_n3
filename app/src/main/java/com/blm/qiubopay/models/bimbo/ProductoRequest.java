package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class ProductoRequest extends QPAY_BaseResponse {

    private String id_bimbo;
    private String id_brand;
    private String brand_name;

    public ProductoRequest(){

    }

    public String getId_brand() {
        return id_brand;
    }

    public void setId_brand(String id_brand) {
        this.id_brand = id_brand;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getId_bimbo() {
        return id_bimbo;
    }

    public void setId_bimbo(String id_bimbo) {
        this.id_bimbo = id_bimbo;
    }
}
