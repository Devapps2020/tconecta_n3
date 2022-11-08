package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.remesas.TC_PayRemittancePetition;
import com.blm.qiubopay.models.remesas.TC_QueryRemittancePetition;

public interface IRemesas {
    public void queryRemittance(TC_QueryRemittancePetition object);
    public void payRemittance(TC_PayRemittancePetition object);
    public void getRemittances(QPAY_Seed object);
}
