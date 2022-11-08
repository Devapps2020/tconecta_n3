package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.operative.restaurant.QPAY_TipOrder;

public interface IRestaurantOperative {
    public void saveTipDetail(QPAY_TipOrder order);
}
