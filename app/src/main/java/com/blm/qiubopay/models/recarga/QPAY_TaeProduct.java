package com.blm.qiubopay.models.recarga;

import java.io.Serializable;

public class QPAY_TaeProduct implements Serializable {

    private String dateTime;
    private String requestId;
    private PaqueteDTO[] products;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public PaqueteDTO[] getProducts() {
        return products;
    }

    public void setProducts(PaqueteDTO[] products) {
        this.products = products;
    }
}
