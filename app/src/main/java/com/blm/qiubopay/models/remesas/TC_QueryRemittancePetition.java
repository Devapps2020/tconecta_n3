package com.blm.qiubopay.models.remesas;

import java.io.Serializable;

public class TC_QueryRemittancePetition implements Serializable {

    private String qpay_seed;
    private String accountNumber;
    private TC_Beneficiario beneficiario;

    public TC_QueryRemittancePetition(){
        this.setBeneficiario(new TC_Beneficiario());
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

    public TC_Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(TC_Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }
}
