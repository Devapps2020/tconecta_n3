package com.blm.qiubopay.listeners.fiado;

import com.blm.qiubopay.models.fiado.QPAY_List_Clientes_Request;

public interface IListClients {

    public void listClients(QPAY_List_Clientes_Request object, Boolean isDebt);
}
