package com.blm.qiubopay.models.remesas;

import java.io.Serializable;

public class TC_RemittanceTrx implements Serializable {

    private String surcharge;
    private String amount;
    private String product;
    private String vendorReference;
    private float tipoCambio;
    private String monedaPago;
    private String accountNumber;
    private String trxId;
    private String transactionId;
    private float montoMonedaOrigen;
    private float montoMonedaPago;
    private String monedaOrigen;
    private int requestId;
    private String commission;
    private String remesador;
    private String flatFee;
    private String datosFiscales;
    private String totalLetra;
    private String createdAt;

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVendorReference() {
        return vendorReference;
    }

    public void setVendorReference(String vendorReference) {
        this.vendorReference = vendorReference;
    }

    public float getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(float tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getMonedaPago() {
        return monedaPago;
    }

    public void setMonedaPago(String monedaPago) {
        this.monedaPago = monedaPago;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public float getMontoMonedaOrigen() {
        return montoMonedaOrigen;
    }

    public void setMontoMonedaOrigen(float montoMonedaOrigen) {
        this.montoMonedaOrigen = montoMonedaOrigen;
    }

    public float getMontoMonedaPago() {
        return montoMonedaPago;
    }

    public void setMontoMonedaPago(float montoMonedaPago) {
        this.montoMonedaPago = montoMonedaPago;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getRemesador() {
        return remesador;
    }

    public void setRemesador(String remesador) {
        this.remesador = remesador;
    }

    public String getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(String flatFee) {
        this.flatFee = flatFee;
    }

    public String getDatosFiscales() {
        return datosFiscales;
    }

    public void setDatosFiscales(String datosFiscales) {
        this.datosFiscales = datosFiscales;
    }

    public String getTotalLetra() {
        return totalLetra;
    }

    public void setTotalLetra(String totalLetra) {
        this.totalLetra = totalLetra;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
