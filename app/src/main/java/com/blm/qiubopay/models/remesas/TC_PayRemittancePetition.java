package com.blm.qiubopay.models.remesas;

import java.io.Serializable;

public class TC_PayRemittancePetition implements Serializable {
    private String qpay_seed;
    private String accountNumber;
    private String idRemesa;
    private String amount;
    private TC_Beneficiario2 beneficiario;
    private TC_Remitente remitente;

    public TC_PayRemittancePetition(){
        beneficiario = new TC_Beneficiario2();
        remitente = new TC_Remitente();
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIdRemesa() {
        return idRemesa;
    }

    public void setIdRemesa(String idRemesa) {
        this.idRemesa = idRemesa;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TC_Beneficiario2 getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(TC_Beneficiario2 beneficiario) {
        this.beneficiario = beneficiario;
    }

    public TC_Remitente getRemitente() {
        return remitente;
    }

    public void setRemitente(TC_Remitente remitente) {
        this.remitente = remitente;
    }
}
