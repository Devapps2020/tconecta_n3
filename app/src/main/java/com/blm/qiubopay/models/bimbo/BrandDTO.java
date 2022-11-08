package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.utils.Globals;

public class BrandDTO {

    private Integer brand_id;
    private String brand_name;
    private Globals.MARCAS marca;

    public BrandDTO() {

    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public Globals.MARCAS getMarca() {
        return marca;
    }

    public void setMarca(Globals.MARCAS marca) {
        this.marca = marca;
    }
}
