package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_Pago_Qiubo_object implements Serializable {

    private String id;

    private String status;

    private String timestamp;

    private String requestId;

    private String concode;

    private String amount;

    private String screen;

    private String terminalRef;

    private String claveQiubo;

    private String trxIdChanel;

    private String trxIdChannel;

    private String tranRef;

    private String transRef;

    private String rspCode;

    private String rspDescription;

    private String rspBody;

    private String responseAt;

    private String createdAt;

    private String updateAt;

    private String qpay_administrator_id;

    public QPAY_Pago_Qiubo_object(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getConcode() {
        return concode;
    }

    public void setConcode(String concode) {
        this.concode = concode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getTerminalRef() {
        return terminalRef;
    }

    public void setTerminalRef(String terminalRef) {
        this.terminalRef = terminalRef;
    }

    public String getClaveQiubo() {
        return claveQiubo;
    }

    public void setClaveQiubo(String claveQiubo) {
        this.claveQiubo = claveQiubo;
    }

    public String getTrxIdChanel() {
        return trxIdChanel;
    }

    public void setTrxIdChanel(String trxIdChanel) {
        this.trxIdChanel = trxIdChanel;
    }

    public String getTranRef() {
        return tranRef;
    }

    public void setTranRef(String tranRef) {
        this.tranRef = tranRef;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDescription() {
        return rspDescription;
    }

    public void setRspDescription(String rspDescription) {
        this.rspDescription = rspDescription;
    }

    public String getRspBody() {
        return rspBody;
    }

    public void setRspBody(String rspBody) {
        this.rspBody = rspBody;
    }

    public String getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(String responseAt) {
        this.responseAt = responseAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getTrxIdChannel() {
        return trxIdChannel;
    }

    public void setTrxIdChannel(String trxIdChannel) {
        this.trxIdChannel = trxIdChannel;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getQpay_administrator_id() {
        return qpay_administrator_id;
    }

    public void setQpay_administrator_id(String qpay_administrator_id) {
        this.qpay_administrator_id = qpay_administrator_id;
    }
}