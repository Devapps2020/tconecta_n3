package com.blm.qiubopay.listeners.fiado;

import com.blm.qiubopay.models.fiado.QPAY_Detail_Cliente_Request;

public interface INewClient {
    public void newCliente(QPAY_Detail_Cliente_Request object);
}
