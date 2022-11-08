package com.blm.qiubopay.models.reportes;

import java.io.Serializable;
import java.util.List;

public class FinancialReport implements Serializable {

    private Double importeNeto;
    private Double tasaDescuento;
    private Double transferenciaSaldo;
    private Double liquidacionBimbonet;
    private List<FinancialReportTxn> finTrxList;
    private String previosPage;
    private String nextPage;
    private String pageSize;

    public Double getImporteNeto() {
        return importeNeto;
    }

    public void setImporteNeto(Double importeNeto) {
        this.importeNeto = importeNeto;
    }

    public Double getTasaDescuento() {
        return tasaDescuento;
    }

    public void setTasaDescuento(Double tasaDescuento) {
        this.tasaDescuento = tasaDescuento;
    }

    public Double getTransferenciaSaldo() {
        return transferenciaSaldo;
    }

    public void setTransferenciaSaldo(Double transferenciaSaldo) {
        this.transferenciaSaldo = transferenciaSaldo;
    }

    public Double getLiquidacionBimbonet() {
        return liquidacionBimbonet;
    }

    public void setLiquidacionBimbonet(Double liquidacionBimbonet) {
        this.liquidacionBimbonet = liquidacionBimbonet;
    }

    public List<FinancialReportTxn> getFinTrxList() {
        return finTrxList;
    }

    public void setFinTrxList(List<FinancialReportTxn> finTrxList) {
        this.finTrxList = finTrxList;
    }

    public String getPreviosPage() {
        return previosPage;
    }

    public void setPreviosPage(String previosPage) {
        this.previosPage = previosPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

}
