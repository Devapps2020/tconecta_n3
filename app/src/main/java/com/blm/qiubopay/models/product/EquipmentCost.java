package com.blm.qiubopay.models.product;

import java.io.Serializable;

public class EquipmentCost implements Serializable {

    private Integer id;
    private String name;
    private String description2;
    private Double commonPrice;
    private Double totalPrice;
    private String productType;
    private String[] description;
    private String[] images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public Double getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(Double commonPrice) {
        this.commonPrice = commonPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
