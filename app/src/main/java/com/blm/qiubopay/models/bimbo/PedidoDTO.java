package com.blm.qiubopay.models.bimbo;

public class PedidoDTO {

    /*private String product_id;
    private Integer quantity_pc;
    private Double price_amount;
    private Double final_amount;
    private String brand_id;*/

    private String idProductInt;
    private String idOrganization;
    private String sku;
    private String idCategory;
    private Integer quantity;
    private Double totalAmount;
    private Double priceInit;

    public PedidoDTO() {

    }

    /*public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity_pc() {
        return quantity_pc;
    }

    public void setQuantity_pc(Integer quantity_pc) {
        this.quantity_pc = quantity_pc;
    }

    public Double getPrice_amount() {
        return price_amount;
    }

    public void setPrice_amount(Double price_amount) {
        this.price_amount = price_amount;
    }

    public Double getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(Double final_amount) {
        this.final_amount = final_amount;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }*/

    public String getIdProductInt() {
        return idProductInt;
    }

    public void setIdProductInt(String idProductInt) {
        this.idProductInt = idProductInt;
    }

    public String getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(String idOrganization) {
        this.idOrganization = idOrganization;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPriceInit() {
        return priceInit;
    }

    public void setPriceInit(Double priceInit) {
        this.priceInit = priceInit;
    }
}
