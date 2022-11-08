package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.util.ArrayList;

public class ProductoResponse extends QPAY_BaseResponse {

    private Integer version;

    private ArrayList<ProductoDTO> products;

    public ProductoResponse(){}

    public ArrayList<ProductoDTO> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductoDTO> products) {
        this.products = products;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
