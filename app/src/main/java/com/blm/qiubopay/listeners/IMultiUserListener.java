package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_LinkCode;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUserStatus;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_Privileges;
import com.blm.qiubopay.models.QPAY_Seed;

public interface IMultiUserListener {

    void getLinkCode(QPAY_Seed object);

    void getLinkedUsers(QPAY_Seed object);

    void getLinkedUsers(QPAY_LinkedUserStatus object);

    void validateLinkCode(QPAY_LinkCode object);

    void confirmLinkUser(QPAY_LinkCode object);

    void linkUser(QPAY_LinkedUser object);

    void unlinkUser(QPAY_Seed object);

    void unlinkUserByAdmin(QPAY_LinkedUser object);

    void getUserPrivileges(QPAY_LinkedUser object);

    void createUserPrivileges(QPAY_Privileges object);

    void createUserDevice(QPAY_Login object);

    void getTodayTaeSalesByUser(QPAY_LinkedUser object);

    void getTodayServicePaymentsByUser(QPAY_LinkedUser object);

    void getTodayTransactionsCompletedByUser(QPAY_LinkedUser object);

    void getTodayTransactions(QPAY_LinkedUser object);

    void getTodayChargesAndPaysByUser(QPAY_LinkedUser object);

}
