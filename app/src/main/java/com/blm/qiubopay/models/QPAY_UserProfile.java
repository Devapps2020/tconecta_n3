package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.profile.QPAY_Profile;

import java.io.Serializable;

public class QPAY_UserProfile extends QPAY_BaseResponse implements Serializable {

    private QPAY_Profile[] qpay_object;

    public QPAY_Profile[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Profile[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}
