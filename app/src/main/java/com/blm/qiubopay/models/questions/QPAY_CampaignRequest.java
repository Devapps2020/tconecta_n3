package com.blm.qiubopay.models.questions;

import java.io.Serializable;

public class QPAY_CampaignRequest implements Serializable {

    private String qpay_seed;
    private String type;
    private String client_channel_id;
    private String qpay_mail;

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient_channel_id() {
        return client_channel_id;
    }

    public void setClient_channel_id(String client_channel_id) {
        this.client_channel_id = client_channel_id;
    }

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }
}