package com.blm.qiubopay.models.dongle;

public class DongleCost {
    private String updatedAt;
    private float cost;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /*public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }*/

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
