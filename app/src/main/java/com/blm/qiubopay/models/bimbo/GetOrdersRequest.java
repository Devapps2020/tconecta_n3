package com.blm.qiubopay.models.bimbo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOrdersRequest {

    @SerializedName("retailer_id")
    @Expose
    private String retailerId;
    @SerializedName("initDate")
    @Expose
    private String initDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


}
