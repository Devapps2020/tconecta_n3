package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.migration_kineto.QPAY_UserKineto;

public interface IMigrateKineto {

    void doMigrateKinetoUser(QPAY_UserKineto object);

}
