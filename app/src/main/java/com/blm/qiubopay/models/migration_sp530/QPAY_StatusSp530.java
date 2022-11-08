package com.blm.qiubopay.models.migration_sp530;

import java.io.Serializable;

public class QPAY_StatusSp530 implements Serializable {

    private String qpay_cellphone;
    private String qpay_folio;

    public String getQpay_cellphone() {
        return qpay_cellphone;
    }

    public void setQpay_cellphone(String qpay_cellphone) {
        this.qpay_cellphone = qpay_cellphone;
    }

    public String getQpay_folio() {
        return qpay_folio;
    }

    public void setQpay_folio(String qpay_folio) {
        this.qpay_folio = qpay_folio;
    }
}
