package com.blm.qiubopay.models.swap;

import java.io.Serializable;

public class QPAY_LinkT1000Request implements Serializable {

    private String qpay_blm_id;
    private String qpay_folio;

    public String getQpay_blm_id() {
        return qpay_blm_id;
    }

    public void setQpay_blm_id(String qpay_blm_id) {
        this.qpay_blm_id = qpay_blm_id;
    }

    public String getQpay_folio() {
        return qpay_folio;
    }

    public void setQpay_folio(String qpay_folio) {
        this.qpay_folio = qpay_folio;
    }
}
