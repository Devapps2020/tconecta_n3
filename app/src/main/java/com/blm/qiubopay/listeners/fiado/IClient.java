package com.blm.qiubopay.listeners.fiado;

import com.blm.qiubopay.models.fiado.QPAY_Cliente_Request;

public interface IClient {
    public void client(QPAY_Cliente_Request object);
}
