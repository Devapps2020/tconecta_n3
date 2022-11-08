package com.blm.qiubopay.models.financial_vas;

import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.tools.Tools;

import java.io.Serializable;

public class QPAY_VasInfo implements Serializable {
    private String updateBalanceFlag;
    private Double balance;
    private String currentDay;
    private String alreadyUsedVasToday;
    private String updateVasAmountsToday;
    private QPAY_FinancialVasAmounts[][] amounts;

    public QPAY_VasInfo(Boolean defaultValues){
        if(defaultValues){
            this.balance = 0.0;
            this.updateBalanceFlag = "0";
            this.currentDay = Tools.getTodayDate();
            this.alreadyUsedVasToday = "0";
            this.updateVasAmountsToday = "0";
            this.amounts = AppPreferences.getFinancialVasAmounts().getQpay_object();
        }
    }

    public String getUpdateBalanceFlag() {
        return updateBalanceFlag;
    }

    public void setUpdateBalanceFlag(String updateBalanceFlag) {
        this.updateBalanceFlag = updateBalanceFlag;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getAlreadyUsedVasToday() {
        return alreadyUsedVasToday;
    }

    public void setAlreadyUsedVasToday(String alreadyUsedVasToday) {
        this.alreadyUsedVasToday = alreadyUsedVasToday;
    }

    public QPAY_FinancialVasAmounts[][] getAmounts() {
        return amounts;
    }

    public String getUpdateVasAmountsToday() {
        return updateVasAmountsToday;
    }

    public void setUpdateVasAmountsToday(String updateVasAmountsToday) {
        this.updateVasAmountsToday = updateVasAmountsToday;
    }

    public void setAmounts(QPAY_FinancialVasAmounts[][] amounts) {
        this.amounts = amounts;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
