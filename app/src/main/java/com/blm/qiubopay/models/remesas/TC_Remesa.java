package com.blm.qiubopay.models.remesas;

import java.io.Serializable;

public class TC_Remesa implements Serializable {

    private String remesador;
    private String monedaOrigen;
    private Float montoMonedaOrigen;
    private String monedaPago;
    private Float montoMonedaPago;
    private Float tipoCambio;
    private int idRemesa;
    private String estatusRemesa;
    private String accountNumber;

    public String getRemesador() {
        return remesador;
    }

    public void setRemesador(String remesador) {
        this.remesador = remesador;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public Float getMontoMonedaOrigen() {
        return montoMonedaOrigen;
    }

    public void setMontoMonedaOrigen(Float montoMonedaOrigen) {
        this.montoMonedaOrigen = montoMonedaOrigen;
    }

    public String getMonedaPago() {
        return monedaPago;
    }

    public void setMonedaPago(String monedaPago) {
        this.monedaPago = monedaPago;
    }

    public Float getMontoMonedaPago() {
        return montoMonedaPago;
    }

    public void setMontoMonedaPago(Float montoMonedaPago) {
        this.montoMonedaPago = montoMonedaPago;
    }

    public Float getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(Float tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public int getIdRemesa() {
        return idRemesa;
    }

    public void setIdRemesa(int idRemesa) {
        this.idRemesa = idRemesa;
    }

    public String getEstatusRemesa() {
        return estatusRemesa;
    }

    public void setEstatusRemesa(String estatusRemesa) {
        this.estatusRemesa = estatusRemesa;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
