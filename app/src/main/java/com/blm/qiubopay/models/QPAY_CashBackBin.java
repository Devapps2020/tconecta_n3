package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_CashBackBin implements Serializable {
    private String Bin;
    private String Name;
    private String Aid;
    private String Fee;
    private String MinAmount;
    private String MaxAmount;

    public QPAY_CashBackBin(){

    }

    public QPAY_CashBackBin(String Bin, String Name, String Aid, String Fee, String MinAmount, String MaxAmount){
        this.setBin(Bin);
        this.setName(Name);
        this.setAid(Aid);
        this.setFee(Fee);
        this.setMinAmount(MinAmount);
        this.setMaxAmount(MaxAmount);
    }

    public String getBin() {
        return Bin;
    }

    public void setBin(String bin) {
        Bin = bin;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAid() {
        return Aid;
    }

    public void setAid(String aid) {
        Aid = aid;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }

    public String getMinAmount() {
        return MinAmount;
    }

    public void setMinAmount(String minAmount) {
        MinAmount = minAmount;
    }

    public String getMaxAmount() {
        return MaxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        MaxAmount = maxAmount;
    }
}
