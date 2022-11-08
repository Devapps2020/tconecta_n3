package com.blm.qiubopay.models.chambitas.campañas;

import com.blm.qiubopay.models.coupons.CouponDetailsAlterListResponse;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;

import java.io.Serializable;
import java.util.List;

public class QPAY_ActiveCampaign_Object implements Serializable {

    private String id;
    private String status;
    private String name;
    private String description;
    private String activation_date;
    private String expiration_date;
    private String approved_date;
    private String prize_amount;
    private CouponDetailsAlterListResponse coupon;

    private List<QPAY_ActiveCampaign_Challenges> challenges;

    public List<QPAY_ActiveCampaign_Challenges> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<QPAY_ActiveCampaign_Challenges> challenges) {
        this.challenges = challenges;
    }

    // Getter Methods

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getActivation_date() {
        return activation_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public String getApproved_date() {
        return approved_date;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation_date(String activation_date) {
        this.activation_date = activation_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    public void setPrize_amount(String prize_amount) {
        this.prize_amount = prize_amount;
    }

    public CouponDetailsAlterListResponse getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponDetailsAlterListResponse coupon) {
        this.coupon = coupon;
    }

}