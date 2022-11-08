package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.apuestas.QPAY_GetBetDetailsPetition;
import com.blm.qiubopay.models.apuestas.QPAY_GetUrlPetition;

public interface IApuestasDeportivas {
    void getUrl(QPAY_GetUrlPetition object);
    void getBetDetails(QPAY_GetBetDetailsPetition object);
    void requestFolio(QPAY_GetBetDetailsPetition object);
    void payBet(QPAY_GetBetDetailsPetition object);
}

