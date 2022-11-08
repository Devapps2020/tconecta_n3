package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.migration_sp530.QPAY_ConfirmEmail;

public interface ISp530ConfirmEmail {
    public void doConfirmEmail(QPAY_ConfirmEmail object);
}
