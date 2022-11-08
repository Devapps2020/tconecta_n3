package com.blm.qiubopay.models.transactions;

import java.io.Serializable;

public class BaSTransactionsModel implements Serializable {
    private int totales = 0;
    private int exitosos = 0;
    private int noExitosos = 0;

    public int getTotales() {
        return totales;
    }

    public void setTotales(int totales) {
        this.totales = totales;
    }

    public int getExitosos() {
        return exitosos;
    }

    public void setExitosos(int exitosos) {
        this.exitosos = exitosos;
    }

    public int getNoExitosos() {
        return noExitosos;
    }

    public void setNoExitosos(int noExitosos) {
        this.noExitosos = noExitosos;
    }
}
