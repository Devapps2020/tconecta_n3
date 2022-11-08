package com.blm.qiubopay.models.bimbo;

public class SellerUserDTO {

    private String seller_id;
    private String seller_name;
    private Double seller_qualify;
    private Integer seller_qualify_count;
    private String seller_visit_days;

    private String sales_center_code;
    private String sales_center_name;

    private BrandDTO brand;
    private OrganizacionDTO organization;
    private RouteDTO route;
    private SupervisorDTO supervisor;

    public SellerUserDTO() {

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

    public Double getSeller_qualify() {
        return seller_qualify;
    }

    public void setSeller_qualify(Double seller_qualify) {
        this.seller_qualify = seller_qualify;
    }

    public Integer getSeller_qualify_count() {
        return seller_qualify_count;
    }

    public void setSeller_qualify_count(Integer seller_qualify_count) {
        this.seller_qualify_count = seller_qualify_count;
    }

    public String getSeller_visit_days() {
        return seller_visit_days;
    }

    public void setSeller_visit_days(String seller_visit_days) {
        this.seller_visit_days = seller_visit_days;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public OrganizacionDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizacionDTO organization) {
        this.organization = organization;
    }

    public RouteDTO getRoute() {
        return route;
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }

    public SupervisorDTO getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorDTO supervisor) {
        this.supervisor = supervisor;
    }

    public String getSales_center_code() {
        return sales_center_code;
    }

    public void setSales_center_code(String sales_center_code) {
        this.sales_center_code = sales_center_code;
    }

    public String getSales_center_name() {
        return sales_center_name;
    }

    public void setSales_center_name(String sales_center_name) {
        this.sales_center_name = sales_center_name;
    }
}
