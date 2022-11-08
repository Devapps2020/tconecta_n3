package com.blm.qiubopay.models.bimbo;

import java.util.List;

public class GetPointRequest {

    private String retailer_id;

    private List<GetPointDTO> points;

    public String getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(String retailer_id) {
        this.retailer_id = retailer_id;
    }

    public List<GetPointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<GetPointDTO> points) {
        this.points = points;
    }
}
