package com.blm.qiubopay.listeners;

import com.blm.qiubopay.models.QPAY_SeedDeviceRequest;
import com.blm.qiubopay.models.product.QPAY_BuyEquipmentRequest;

public interface IBuyProduct {

    void getEquipmentCost(QPAY_SeedDeviceRequest object);

    void buyEquipment(QPAY_BuyEquipmentRequest object);

}
