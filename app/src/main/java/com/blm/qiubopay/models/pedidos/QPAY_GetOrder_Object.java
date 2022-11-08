package com.blm.qiubopay.models.pedidos;

import java.io.Serializable;
import java.util.List;

public class QPAY_GetOrder_Object implements Serializable {

    private String idOrder;
    private String idTicket;
    private Integer quantity;
    private Double totalAmount;

    private List<QPAY_GetInventory_Object> products;


    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public List<QPAY_GetInventory_Object> getProducts() {
        return products;
    }

    public void setProducts(List<QPAY_GetInventory_Object> products) {
        this.products = products;
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
}