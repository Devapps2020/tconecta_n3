package com.blm.qiubopay.models.nubity;

import java.io.Serializable;

public class QPAY_CheckTicketsClient implements Serializable {

    private String retailer_id;
    private String organization_id;


    // Getter Methods

    public String getRetailer_id() {
        return retailer_id;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    // Setter Methods

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

}