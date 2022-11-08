package com.blm.qiubopay.models.visa.response;

import java.io.Serializable;

public class CspHeader implements Serializable {

    private String rspCode;
    private String rspMsg;
    private String rspId;

    private String merchantId;
    private String productId;
    private String txTypeId;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public String getRspId() {
        return rspId;
    }

    public void setRspId(String rspId) {
        this.rspId = rspId;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTxTypeId() {
        return txTypeId;
    }

    public void setTxTypeId(String txTypeId) {
        this.txTypeId = txTypeId;
    }
}
