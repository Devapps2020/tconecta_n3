package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.mitec.QPAY_MitErrorFinancialTxrRequest;

public interface IMitDebugTxrListener {

    void sendDebugInfoMitTxr(QPAY_MitErrorFinancialTxrRequest object);
}
