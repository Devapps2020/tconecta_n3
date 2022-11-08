package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeOrder;

public interface IChallengeDoneOrder {
    public void challengeDoneOrder(QPAY_ChallengeOrder request);
}
