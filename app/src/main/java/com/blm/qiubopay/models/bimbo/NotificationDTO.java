package com.blm.qiubopay.models.bimbo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class NotificationDTO {

    @DatabaseField(generatedId = true)
    private Integer notifivationId;

    @DatabaseField
    private Integer type;

    @DatabaseField
    public String title;

    @DatabaseField
    public String body;

    @DatabaseField
    public String cr;

    @DatabaseField
    public String infCif;

    @DatabaseField
    public String id;

    @DatabaseField
    public Long s;

    @DatabaseField
    public String mc;

    @DatabaseField
    public String notificationJSON;

    @DatabaseField
    public String decodeJSON;

    @DatabaseField
    private Integer status;

    @DatabaseField
    public Date date;

    public NotificationDTO() {

    }

    public Integer getNotifivationId() {
        return notifivationId;
    }

    public void setNotifivationId(Integer notifivationId) {
        this.notifivationId = notifivationId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getInfCif() {
        return infCif;
    }

    public void setInfCif(String infCif) {
        this.infCif = infCif;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getS() {
        return s;
    }

    public void setS(Long s) {
        this.s = s;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getNotificationJSON() {
        return notificationJSON;
    }

    public void setNotificationJSON(String notificationJSON) {
        this.notificationJSON = notificationJSON;
    }

    public String getDecodeJSON() {
        return decodeJSON;
    }

    public void setDecodeJSON(String decodeJSON) {
        this.decodeJSON = decodeJSON;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public enum TYPE {
        INFORMATION,
        ACCOUNT_VALIDATION,
        PAYMENT_REQUEST,
        REPAYMENT,
        NONE,
        QR,
    }

    public enum STATUS {
        ACTIVE,
        PAID,
        POSTPONED,
        REJECTED
    }
}
