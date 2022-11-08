package com.blm.qiubopay.models.bimbo;

public class PesitoCreditRequest {

    private Integer organization_id;
    private Integer route_id;
    private String seller_id;
    private String seller_name;
    private String retailer_id;
    private String retailer_name;
    private String request_pesito_date;

    private String invoice_date; //2021-07-07
    private String ceve_code;
    private String supervisor_code;

    public PesitoCreditRequest(){

    }

    public Integer getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Integer organization_id) {
        this.organization_id = organization_id;
    }

    public Integer getRoute_id() {
        return route_id;
    }

    public void setRoute_id(Integer route_id) {
        this.route_id = route_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getRetailer_name() {
        return retailer_name;
    }

    public void setRetailer_name(String retailer_name) {
        this.retailer_name = retailer_name;
    }

    public String getRequest_pesito_date() {
        return request_pesito_date;
    }

    public void setRequest_pesito_date(String request_pesito_date) {
        this.request_pesito_date = request_pesito_date;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getCeve_code() {
        return ceve_code;
    }

    public void setCeve_code(String ceve_code) {
        this.ceve_code = ceve_code;
    }

    public String getSupervisor_code() {
        return supervisor_code;
    }

    public void setSupervisor_code(String supervisor_code) {
        this.supervisor_code = supervisor_code;
    }
}
