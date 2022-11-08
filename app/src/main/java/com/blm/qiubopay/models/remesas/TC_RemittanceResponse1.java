package com.blm.qiubopay.models.remesas;

import java.io.Serializable;

public class TC_RemittanceResponse1 implements Serializable {
    private TC_Remesa remesa;
    private TC_Remitente remitente;
    private TC_Beneficiario beneficiario;

    public TC_Remesa getRemesa() {
        return remesa;
    }

    public void setRemesa(TC_Remesa remesa) {
        this.remesa = remesa;
    }

    public TC_Remitente getRemitente() {
        return remitente;
    }

    public void setRemitente(TC_Remitente remitente) {
        this.remitente = remitente;
    }

    public TC_Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(TC_Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }
}
