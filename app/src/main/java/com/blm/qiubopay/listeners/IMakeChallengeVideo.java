package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQR;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeVideo;

public interface IMakeChallengeVideo {
    public void makeChallengeVideo(QPAY_ChallengeVideo seed);

}
