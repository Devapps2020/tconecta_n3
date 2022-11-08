package com.blm.qiubopay.models;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class Balance extends GenericResponse implements Serializable {
    private String balance;
    private String requestId;

    private String qpay_seed;
    private String qpay_praga_data1;//BUSINESS_ID
    private String qpay_praga_data2;//API_KEY
    private String qpay_praga_data3;//ENCRIPTION_KEY
    private String qpay_gateway_user;

    private String qpay_balance;
    private String qpay_total_txn;
    private String qpay_gainings;

    private String qpay_max_limit;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getQpay_balance() {
        return qpay_balance;
    }

    public void setQpay_balance(String qpay_balance) {
        this.qpay_balance = qpay_balance;
    }

    public String getQpay_total_txn() {
        return qpay_total_txn;
    }

    public void setQpay_total_txn(String qpay_total_txn) {
        this.qpay_total_txn = qpay_total_txn;
    }

    public String getQpay_gainings() {
        return qpay_gainings;
    }

    public void setQpay_gainings(String qpay_gainings) {
        this.qpay_gainings = qpay_gainings;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_praga_data1() {
        return qpay_praga_data1;
    }

    public void setQpay_praga_data1(String qpay_praga_data1) {
        this.qpay_praga_data1 = qpay_praga_data1;
    }

    public String getQpay_praga_data2() {
        return qpay_praga_data2;
    }

    public void setQpay_praga_data2(String qpay_praga_data2) {
        this.qpay_praga_data2 = qpay_praga_data2;
    }

    public String getQpay_praga_data3() {
        return qpay_praga_data3;
    }

    public void setQpay_praga_data3(String qpay_praga_data3) {
        this.qpay_praga_data3 = qpay_praga_data3;
    }

    public String getQpay_gateway_user() {
        return qpay_gateway_user;
    }

    public void setQpay_gateway_user(String qpay_gateway_user) {
        this.qpay_gateway_user = qpay_gateway_user;
    }

    public static class BalanceExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(Balance.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    //public DynamicDataBalance dynamicData;

    /*public DynamicDataBalance getDynamicData() {
        return dynamicData;
    }*/

    /*public void setDynamicData(DynamicDataBalance dynamicData) {
        this.dynamicData = dynamicData;
    }*/

    public String getQpay_max_limit() {
        return qpay_max_limit;
    }

    public void setQpay_max_limit(String qpay_max_limit) {
        this.qpay_max_limit = qpay_max_limit;
    }
}
