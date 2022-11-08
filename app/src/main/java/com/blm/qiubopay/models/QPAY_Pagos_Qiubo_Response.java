package com.blm.qiubopay.models;

import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_Pagos_Qiubo_Response extends QPAY_BaseResponse implements Serializable {

    private QPAY_Pago_Qiubo_object[] qpay_object;

    public QPAY_Pagos_Qiubo_Response() {

    }

    public QPAY_Pago_Qiubo_object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Pago_Qiubo_object[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}

/*public class QPAY_Pagos_Qiubo_Response extends QPAY_BaseResponse implements Serializable {

    private String qpay_response;

    private String qpay_code;

    private String qpay_description;

    private QPAY_Pago_Qiubo_object[] qpay_object;

    public QPAY_Pagos_Qiubo_Response() {

    }

    @Override
    public String getQpay_response() {
        return qpay_response;
    }

    @Override
    public void setQpay_response(String qpay_response) {
        this.qpay_response = qpay_response;
    }

    @Override
    public String getQpay_code() {
        return qpay_code;
    }

    @Override
    public void setQpay_code(String qpay_code) {
        this.qpay_code = qpay_code;
    }

    @Override
    public String getQpay_description() {
        return qpay_description;
    }

    @Override
    public void setQpay_description(String qpay_description) {
        this.qpay_description = qpay_description;
    }

    public QPAY_Pago_Qiubo_object[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(QPAY_Pago_Qiubo_object[] qpay_object) {
        this.qpay_object = qpay_object;
    }
}*/
