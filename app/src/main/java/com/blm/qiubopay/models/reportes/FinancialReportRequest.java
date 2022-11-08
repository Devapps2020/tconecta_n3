package com.blm.qiubopay.models.reportes;

import java.io.Serializable;

public class FinancialReportRequest implements Serializable {

    private String qpay_seed;
    private String startDate;
    private String endDate;
    private String pageAfter;
    private String pageBefore;
    private String pageSize;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPageAfter() {
        return pageAfter;
    }

    public void setPageAfter(String pageAfter) {
        this.pageAfter = pageAfter;
    }

    public String getPageBefore() {
        return pageBefore;
    }

    public void setPageBefore(String pageBefore) {
        this.pageBefore = pageBefore;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
