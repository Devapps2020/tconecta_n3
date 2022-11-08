package com.blm.qiubopay.models.nubity;

import java.io.Serializable;

public class QPAY_CheckTicketDetail implements Serializable {

    private String retailer_id;
    private String folio_number;


    // Getter Methods

    public String getRetailer_id() {
        return retailer_id;
    }

    public String getFolio_number() {
        return folio_number;
    }

    // Setter Methods

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public void setFolio_number(String folio_number) {
        this.folio_number = folio_number;
    }

}