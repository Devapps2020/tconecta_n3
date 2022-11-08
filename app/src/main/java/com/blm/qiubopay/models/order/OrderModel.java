package com.blm.qiubopay.models.order;

import java.io.Serializable;

public class OrderModel implements Serializable {

    private String productName;
    private int productQuantity;
    private String productPrice;
    private String similarProductName;
    private int similarProductQuantity;
    private Boolean isBoxChecked;

    public OrderModel() {}

    public OrderModel(String productName, int productQuantity, Boolean isBoxChecked) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.isBoxChecked = isBoxChecked;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getSimilarProductName() {
        return similarProductName;
    }

    public void setSimilarProductName(String similarProductName) {
        this.similarProductName = similarProductName;
    }

    public int getSimilarProductQuantity() {
        return similarProductQuantity;
    }

    public void setSimilarProductQuantity(int similarProductQuantity) {
        this.similarProductQuantity = similarProductQuantity;
    }

    public Boolean getBoxChecked() {
        return isBoxChecked;
    }

    public void setBoxChecked(Boolean boxChecked) {
        isBoxChecked = boxChecked;
    }

}
