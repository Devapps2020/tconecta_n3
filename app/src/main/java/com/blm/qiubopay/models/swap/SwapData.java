package com.blm.qiubopay.models.swap;

import java.io.Serializable;

public class SwapData implements Serializable {

    private String folio;
    private String promotor;

    public SwapData(String folio, String promotor) {
        this.folio = folio;
        this.promotor = promotor;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPromotor() {
        return promotor;
    }

    public void setPromotor(String promotor) {
        this.promotor = promotor;
    }
}
