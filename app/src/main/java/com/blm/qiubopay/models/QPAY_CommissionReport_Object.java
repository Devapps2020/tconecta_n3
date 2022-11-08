package com.blm.qiubopay.models;

import java.io.Serializable;
import java.util.List;

public class QPAY_CommissionReport_Object implements Serializable {

   private String date;
   private String totalCommissions;
   private List<QPAY_CommissionReport_Tae> TAE;
    private List<QPAY_CommissionReport_Tae> BP;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalCommissions() {
        return totalCommissions;
    }

    public void setTotalCommissions(String totalCommissions) {
        this.totalCommissions = totalCommissions;
    }

    public List<QPAY_CommissionReport_Tae> getTAE() {
        return TAE;
    }

    public void setTAE(List<QPAY_CommissionReport_Tae> TAE) {
        this.TAE = TAE;
    }

    public List<QPAY_CommissionReport_Tae> getBP() {
        return BP;
    }

    public void setBP(List<QPAY_CommissionReport_Tae> BP) {
        this.BP = BP;
    }
}