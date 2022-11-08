package com.blm.qiubopay.models.bimbo;

public class ReedemPointsRequest {

    private String retailer_id;
    private Integer idPremio;


    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public Integer getId_premio() {
        return idPremio;
    }

    public void setId_premio(Integer id_premio) {
        this.idPremio = id_premio;
    }
}
