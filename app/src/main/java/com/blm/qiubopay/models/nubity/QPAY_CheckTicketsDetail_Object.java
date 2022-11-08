package com.blm.qiubopay.models.nubity;

import java.io.Serializable;

public class QPAY_CheckTicketsDetail_Object implements Serializable {
    private String product_code;
    private String product_desc;
    private String sale_quantity;
    private String sale_price;
    private String tax;
    private String tax_perc;
    private String invoice_line_amt;
    private String tax_code;
    private String tax_type;


    // Getter Methods

    public String getProduct_code() {
        return product_code;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public String getSale_quantity() {
        return sale_quantity;
    }

    public String getSale_price() {
        return sale_price;
    }

    public String getTax() {
        return tax;
    }

    public String getTax_perc() {
        return tax_perc;
    }

    public String getInvoice_line_amt() {
        return invoice_line_amt;
    }

    public String getTax_code() {
        return tax_code;
    }

    public String getTax_type() {
        return tax_type;
    }

    // Setter Methods

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public void setSale_quantity(String sale_quantity) {
        this.sale_quantity = sale_quantity;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setTax_perc(String tax_perc) {
        this.tax_perc = tax_perc;
    }

    public void setInvoice_line_amt(String invoice_line_amt) {
        this.invoice_line_amt = invoice_line_amt;
    }

    public void setTax_code(String tax_code) {
        this.tax_code = tax_code;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

}