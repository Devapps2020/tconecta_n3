package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsPetition;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasPetition;

public interface IFinancialVas {
    public void getMinimumAmounts(QPAY_FinancialVasGetAmountsPetition object);
    public void checkIfFinancialVasIsAvailable(QPAY_FinancialVasPetition object);
    public void processFinancialVas(QPAY_FinancialVasPetition object);
}
