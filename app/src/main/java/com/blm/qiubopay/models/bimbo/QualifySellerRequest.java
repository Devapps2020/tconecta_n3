package com.blm.qiubopay.models.bimbo;

public class QualifySellerRequest {

    private String route_type;
    private String sale_center_code;
    private String seller_id;
    private String retailer_id;
    private Integer seller_qualify;
    private String visit_date;
    private String comments;

    private String route_id;
    private String seller_name;
    private String supervisor_id;
    private String supervisor_name;

    public QualifySellerRequest() {

    }

    public String getRoute_type() {
        return route_type;
    }

    public void setRoute_type(String route_type) {
        this.route_type = route_type;
    }

    public String getSale_center_code() {
        return sale_center_code;
    }

    public void setSale_center_code(String sale_center_code) {
        this.sale_center_code = sale_center_code;
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

    public Integer getSeller_qualify() {
        return seller_qualify;
    }

    public void setSeller_qualify(Integer seller_qualify) {
        this.seller_qualify = seller_qualify;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(String supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }
}
