package com.blm.qiubopay.models.nubity;

import java.io.Serializable;

public class QPAY_CheckTicketsClient_Object implements Serializable {

    private String sales_center_code;
    private String legal_entity;
    private String route_identifier;
    private String invoice_number;
    private String doc_date;
    private String non_taxed_net;
    private String taxed_net;
    private String inv_tot_tax;
    private String inv_total;
    private String doc_type;


    // Getter Methods

    public String getSales_center_code() {
        return sales_center_code;
    }

    public String getLegal_entity() {
        return legal_entity;
    }

    public String getRoute_identifier() {
        return route_identifier;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public String getNon_taxed_net() {
        return non_taxed_net;
    }

    public String getTaxed_net() {
        return taxed_net;
    }

    public String getInv_tot_tax() {
        return inv_tot_tax;
    }

    public String getInv_total() {
        return inv_total;
    }

    public String getDoc_type() {
        return doc_type;
    }

    // Setter Methods

    public void setSales_center_code(String sales_center_code) {
        this.sales_center_code = sales_center_code;
    }

    public void setLegal_entity(String legal_entity) {
        this.legal_entity = legal_entity;
    }

    public void setRoute_identifier(String route_identifier) {
        this.route_identifier = route_identifier;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }

    public void setNon_taxed_net(String non_taxed_net) {
        this.non_taxed_net = non_taxed_net;
    }

    public void setTaxed_net(String taxed_net) {
        this.taxed_net = taxed_net;
    }

    public void setInv_tot_tax(String inv_tot_tax) {
        this.inv_tot_tax = inv_tot_tax;
    }

    public void setInv_total(String inv_total) {
        this.inv_total = inv_total;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

}