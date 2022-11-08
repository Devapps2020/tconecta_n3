package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_Privileges implements Serializable {

    private Integer userId;
    private String day1;
    private String day2;
    private String day3;
    private String day4;
    private String day5;
    private String day6;
    private String day7;
    private String startTime;
    private String endTime;
    private Double amountLimit;
    private String linkAlias;
    private String cashCollection;
    private String fiadoApp;
    private String jazzDeportes;

    private String qpay_seed;

    public String[] getDaysArray() {
        String[] dayArray = {getDay1(),getDay2(),getDay3(),getDay4(),getDay5(),getDay6(),getDay7()};
        return dayArray;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getDay3() {
        return day3;
    }

    public void setDay3(String day3) {
        this.day3 = day3;
    }

    public String getDay4() {
        return day4;
    }

    public void setDay4(String day4) {
        this.day4 = day4;
    }

    public String getDay5() {
        return day5;
    }

    public void setDay5(String day5) {
        this.day5 = day5;
    }

    public String getDay6() {
        return day6;
    }

    public void setDay6(String day6) {
        this.day6 = day6;
    }

    public String getDay7() {
        return day7;
    }

    public void setDay7(String day7) {
        this.day7 = day7;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(Double amountLimit) {
        this.amountLimit = amountLimit;
    }

    public String getLinkAlias() {
        return linkAlias;
    }

    public void setLinkAlias(String linkAlias) {
        this.linkAlias = linkAlias;
    }

    public String getCashCollection() {
        return cashCollection;
    }

    public void setCashCollection(String cashCollection) {
        this.cashCollection = cashCollection;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getFiadoApp() {
        return fiadoApp;
    }

    public void setFiadoApp(String fiadoApp) {
        this.fiadoApp = fiadoApp;
    }

    public String getJazzDeportes() {
        return jazzDeportes;
    }

    public void setJazzDeportes(String jazzDeportes) {
        this.jazzDeportes = jazzDeportes;
    }
}
