package com.blm.qiubopay.models.nubity;

import java.io.Serializable;

public class QPAY_SalesRetailer_Object implements Serializable {
    private String organization_id;
    private String sale_amount;
    private String sale_amount_mtd;

    private String name;

    // Getter Methods

    public String getOrganization_id() {
        return organization_id;
    }

    public String getSale_amount() {
        return sale_amount;
    }

    public String getSale_amount_mtd() {
        return sale_amount_mtd;
    }

    // Setter Methods

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public void setSale_amount(String sale_amount) {
        this.sale_amount = sale_amount;
    }

    public void setSale_amount_mtd(String sale_amount_mtd) {
        this.sale_amount_mtd = sale_amount_mtd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}