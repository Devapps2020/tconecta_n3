package com.blm.qiubopay.models.operative.restaurant;

import java.io.Serializable;

public class QPAY_SaveTip implements Serializable {

    private String ticket_number;

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }
}
