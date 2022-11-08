package com.blm.qiubopay.models;

import java.util.ArrayList;

public class QPAY_Notifications {

    private String fcmId;

    private ArrayList<QPAY_Notification> notifications;

    public QPAY_Notifications(){
        notifications = new ArrayList();
    }

    public ArrayList<QPAY_Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<QPAY_Notification> notifications) {
        this.notifications = notifications;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getFcmId() {
        return fcmId;
    }
}
