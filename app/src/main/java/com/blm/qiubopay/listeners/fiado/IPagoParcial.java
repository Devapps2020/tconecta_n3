package com.blm.qiubopay.listeners.fiado;

import com.blm.qiubopay.models.fiado.QPAY_Pago_Parcial_Request;

public interface IPagoParcial {
    public void pagoParcial(QPAY_Pago_Parcial_Request object);
}
