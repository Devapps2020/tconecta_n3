package com.blm.qiubopay.models;

public class QPAY_Notification {

    private String messageId;

    private String title;

    private String body;

    private String date;

    private boolean status;

    public QPAY_Notification(){

    }

    public QPAY_Notification(String messageId, String title, String body, int status, String date) {
        this.messageId = messageId;
        this.title = title;
        this.body = body;
        this.status = (status == 1);
        this.date = date;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
