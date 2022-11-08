package com.blm.qiubopay.responses.dynamicdata;

import com.blm.qiubopay.models.pidelo.Order;

import java.util.List;

public class DynamicDataOrder {

    private int codigo;
    private List<Order> orders;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
