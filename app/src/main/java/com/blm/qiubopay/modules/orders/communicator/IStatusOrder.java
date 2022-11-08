package com.blm.qiubopay.modules.orders.communicator;

public interface IStatusOrder {

    void onSearchClickListener(String phone);
    void onStatusOrderClickListener(String enabled);
    void onListSelected(Boolean isPendingList);
    void onScrollEnd(Boolean isPendingList);

}
