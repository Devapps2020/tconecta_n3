package com.blm.qiubopay.models.questions;


import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;
import java.util.List;

public class QPAY_ChannelsResponse extends QPAY_BaseResponse implements Serializable {

    private List<List<QPAY_Channels_object>> qpay_object;

    public List<List<QPAY_Channels_object>> getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(List<List<QPAY_Channels_object>> qpay_object) {
        this.qpay_object = qpay_object;
    }

}