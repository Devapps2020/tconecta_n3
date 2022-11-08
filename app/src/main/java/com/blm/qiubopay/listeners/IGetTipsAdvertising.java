package com.blm.qiubopay.listeners;


import com.blm.qiubopay.models.QPAY_Seed;

public interface IGetTipsAdvertising {

    void getTipsAdvertising(QPAY_Seed seed);

    void getAllTipsAdvertising(QPAY_Seed seed);

    void getActiveTipsCampaignCount(QPAY_Seed seed);

}
