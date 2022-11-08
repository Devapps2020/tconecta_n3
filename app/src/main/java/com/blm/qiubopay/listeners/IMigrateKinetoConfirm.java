package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.migration_kineto.QPAY_ConfirmUser;

public interface IMigrateKinetoConfirm {

    void doMigrateKinetoUserConfirm(QPAY_ConfirmUser object);

}
