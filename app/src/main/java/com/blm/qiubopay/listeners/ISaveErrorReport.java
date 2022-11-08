package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.soporte.QPAY_ErrorReport;

public interface ISaveErrorReport {
    public void saveErrorReport(QPAY_ErrorReport object);
}
