package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.restore_password.QPAY_RestorePassword;

public interface ISendRestorePassword {
    public void restorePassword(QPAY_RestorePassword object, Boolean isStep2);
}
