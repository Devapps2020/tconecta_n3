package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.pedidos.QPAY_GetInventory_Object;

import java.util.ArrayList;
import java.util.List;

public class PedidoRequest {

    private String idBimbo;
    private String blmId;

    private List<QPAY_GetInventory_Object> product;

    public List<QPAY_GetInventory_Object> getProduct() {
        return product;
    }

    public void setProduct(List<QPAY_GetInventory_Object> products) {
        this.product = products;
    }

    public QPAY_GetInventory_Object getProduct(String productId) {

        if(product == null)
            product = new ArrayList();

        for (QPAY_GetInventory_Object ped: product)
            if (ped.getIdProductInt().equals(productId))
                return ped;

        return null;
    }

    public String getIdBimbo() {
        return idBimbo;
    }

    public void setIdBimbo(String idBimbo) {
        this.idBimbo = idBimbo;
    }

    public String getBlmId() {
        return blmId;
    }

    public void setBlmId(String blmId) {
        this.blmId = blmId;
    }
}

