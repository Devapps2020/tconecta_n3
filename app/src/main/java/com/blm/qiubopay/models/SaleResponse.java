package com.blm.qiubopay.models;

public class SaleResponse extends GenericResponse {

    public DynamicDataSaleResponse dynamicData;

    public DynamicDataSaleResponse getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicDataSaleResponse dynamicData) {
        this.dynamicData = dynamicData;
    }
}
