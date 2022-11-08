package com.blm.qiubopay.models.bimbo;

public class PromotionDTO {

    private String id;
    private String promotion_code;
    private String promotion_name;
    private String ini_date;
    private String exp_date;
    private Integer bimbo_points;
    private String promo_detail;
    private Integer promo_type;
    private Double piece_priece;

    public PromotionDTO() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }

    public String getPromotion_name() {
        return promotion_name;
    }

    public void setPromotion_name(String promotion_name) {
        this.promotion_name = promotion_name;
    }

    public String getIni_date() {
        return ini_date;
    }

    public void setIni_date(String ini_date) {
        this.ini_date = ini_date;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public Integer getBimbo_points() {
        return bimbo_points;
    }

    public void setBimbo_points(Integer bimbo_points) {
        this.bimbo_points = bimbo_points;
    }

    public String getPromo_detail() {
        return promo_detail;
    }

    public void setPromo_detail(String promo_detail) {
        this.promo_detail = promo_detail;
    }

    public Integer getPromo_type() {
        return promo_type;
    }

    public void setPromo_type(Integer promo_type) {
        this.promo_type = promo_type;
    }

    public Double getPiece_priece() {
        return piece_priece;
    }

    public void setPiece_priece(Double piece_priece) {
        this.piece_priece = piece_priece;
    }

}
