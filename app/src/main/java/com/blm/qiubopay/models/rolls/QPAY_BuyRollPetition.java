package com.blm.qiubopay.models.rolls;

import java.io.Serializable;

public class QPAY_BuyRollPetition implements Serializable {
    private String id;
    private String productType;
    private String qpay_seed;
    private QPAY_DeliveryAddress deliveryAddress;

    public QPAY_BuyRollPetition(){
        deliveryAddress = new QPAY_DeliveryAddress();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public QPAY_DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(QPAY_DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
