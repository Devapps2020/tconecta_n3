package com.blm.qiubopay.models.corte_caja;

import java.io.Serializable;

public class QPAY_BoxCutInfo implements Serializable {

    private String totalAmount;
    private int salesCount;
    private int voidsCount;
    private String voidsAmount;
    private String salesAmount;
    private QPAY_BoxCutTxr[] txns;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public int getVoidsCount() {
        return voidsCount;
    }

    public void setVoidsCount(int voidsCount) {
        this.voidsCount = voidsCount;
    }

    public String getVoidsAmount() {
        return voidsAmount;
    }

    public void setVoidsAmount(String voidsAmount) {
        this.voidsAmount = voidsAmount;
    }

    public String getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    public QPAY_BoxCutTxr[] getTxns() {
        return txns;
    }

    public void setTxns(QPAY_BoxCutTxr[] txns) {
        this.txns = txns;
    }
}
