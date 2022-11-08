package com.blm.qiubopay.models.bimbo;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

public class PesitoCatalogResponse extends QPAY_BaseResponse {


    public PesitoCatalogResponse() {

    }

    private PesitoCatalogDTO[] qpay_object;

    public PesitoCatalogDTO[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(PesitoCatalogDTO[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
