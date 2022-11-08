package com.blm.qiubopay.models.bimbo;

public class PesitoCatalogRequest {

    private String seller_id;
    private String retailer_id;
    private Integer route_id;

    public PesitoCatalogRequest() {

    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public Integer getRoute_id() {
        return route_id;
    }

    public void setRoute_id(Integer route_id) {
        this.route_id = route_id;
    }
}
