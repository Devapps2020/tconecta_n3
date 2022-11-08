package com.blm.qiubopay.models.bimbo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ProductoDTO {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String id_product;

    @DatabaseField
    private String id_brand;

    @DatabaseField
    private String product_name;

    @DatabaseField
    private Integer product_size;

    @DatabaseField
    private Double product_price;

    @DatabaseField
    private String product_image;

    @DatabaseField
    private String category_name;

    @DatabaseField
    private String type_register;

    private Integer status;

    public ProductoDTO(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getProduct_size() {
        return product_size;
    }

    public void setProduct_size(Integer product_size) {
        this.product_size = product_size;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image() {
        return "img_" + id_product.trim()
                .replace(".JPG","")
                .replace(".PGN","")
                .replace(".jpg","")
                .replace(".png","");
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getId_brand() {
        return id_brand;
    }

    public void setId_brand(String id_brand) {
        this.id_brand = id_brand;
    }

    public String getType_register() {
        return type_register;
    }

    public void setType_register(String type_register) {
        this.type_register = type_register;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
