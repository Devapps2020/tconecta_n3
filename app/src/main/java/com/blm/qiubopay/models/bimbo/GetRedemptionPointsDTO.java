package com.blm.qiubopay.models.bimbo;

public class GetRedemptionPointsDTO {

    private Integer id;
    private Integer retailer_id;
    private Integer idPremio;
    private Integer puntosRed;
    private Integer openingBalance;
    private Integer closingBalance;
    private String createdAt;

    public GetRedemptionPointsDTO() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(Integer retailer_id) {
        this.retailer_id = retailer_id;
    }

    public Integer getIdPremio() {
        return idPremio;
    }

    public void setIdPremio(Integer idPremio) {
        this.idPremio = idPremio;
    }

    public Integer getPuntosRed() {
        return puntosRed;
    }

    public void setPuntosRed(Integer puntosRed) {
        this.puntosRed = puntosRed;
    }

    public Integer getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Integer openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Integer getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Integer closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}