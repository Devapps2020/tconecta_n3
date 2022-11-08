package com.blm.qiubopay.models.transactions;

import com.blm.qiubopay.helpers.AppPreferences;

import java.io.Serializable;

public class TransactionsModel implements Serializable {
    private String seed;
    private String fecha;
    private BaSTransactionsModel vasTransactions = new BaSTransactionsModel();
    private FinancialTransactionsModel financialTransactions = new FinancialTransactionsModel();
    private RsTransactionsModel rsTransactions = new RsTransactionsModel();

    public TransactionsModel() {

    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BaSTransactionsModel getVasTransactions() {
        return vasTransactions;
    }

    public void setVasTransactions(BaSTransactionsModel vasTransactions) {
        this.vasTransactions = vasTransactions;
    }

    public FinancialTransactionsModel getFinancialTransactions() {
        return financialTransactions;
    }

    public void setFinancialTransactions(FinancialTransactionsModel financialTransactions) {
        this.financialTransactions = financialTransactions;
    }

    public RsTransactionsModel getRsTransactions() {
        return rsTransactions;
    }

    public void setRsTransactions(RsTransactionsModel rsTransactions) {
        this.rsTransactions = rsTransactions;
    }
}
