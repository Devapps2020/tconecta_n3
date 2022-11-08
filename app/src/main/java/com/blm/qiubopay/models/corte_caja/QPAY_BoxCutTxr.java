package com.blm.qiubopay.models.corte_caja;

import java.io.Serializable;

public class QPAY_BoxCutTxr implements Serializable {
    private String maskedPan;
    private int id;
    private String txTypeName;
    private String txCreatedAt;
    private String txAmount;

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxTypeName() {
        return txTypeName;
    }

    public void setTxTypeName(String txTypeName) {
        this.txTypeName = txTypeName;
    }

    public String getTxCreatedAt() {
        return txCreatedAt;
    }

    public void setTxCreatedAt(String txCreatedAt) {
        this.txCreatedAt = txCreatedAt;
    }

    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }
}
