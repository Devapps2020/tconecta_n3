package com.blm.qiubopay.models;

import java.io.Serializable;

public class QPAY_ScreenTxn implements Serializable {

    private String qpay_seed;

    private String qpay_seedtimestamp;

    private String qpay_requestId;

    private QPAY_StartTxn_object dynamicData;

    public QPAY_ScreenTxn() {

    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_seedtimestamp() {
        return qpay_seedtimestamp;
    }

    public void setQpay_seedtimestamp(String qpay_seedtimestamp) {
        this.qpay_seedtimestamp = qpay_seedtimestamp;
    }

    public String getQpay_requestId() {
        return qpay_requestId;
    }

    public void setQpay_requestId(String qpay_requestId) {
        this.qpay_requestId = qpay_requestId;
    }

    public QPAY_StartTxn_object getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(QPAY_StartTxn_object dynamicData) {
        this.dynamicData = dynamicData;
    }
}