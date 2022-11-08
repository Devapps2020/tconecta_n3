package com.blm.qiubopay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QPAY_ReportRecarga implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("tae_saleId")
    @Expose
    private Integer taeSaleId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("carrier")
    @Expose
    private String carrier;
    @SerializedName("operation_number")
    @Expose
    private String operationNumber;
    @SerializedName("date_operation")
    @Expose
    private String dateOperation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_description")
    @Expose
    private String statusDescription;
    @SerializedName("award_id")
    @Expose
    private Integer awardId;
    @SerializedName("award_description")
    @Expose
    private String awardDescription;
    @SerializedName("retailer_id")
    @Expose
    private String retailerId;
    @SerializedName("route_id")
    @Expose
    private Object routeId;
    @SerializedName("authorization")
    @Expose
    private String authorization;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getTaeSaleId() {
        return taeSaleId;
    }

    public void setTaeSaleId(Integer taeSaleId) {
        this.taeSaleId = taeSaleId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Integer getAwardId() {
        return awardId;
    }

    public void setAwardId(Integer awardId) {
        this.awardId = awardId;
    }

    public String getAwardDescription() {
        return awardDescription;
    }

    public void setAwardDescription(String awardDescription) {
        this.awardDescription = awardDescription;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public Object getRouteId() {
        return routeId;
    }

    public void setRouteId(Object routeId) {
        this.routeId = routeId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
