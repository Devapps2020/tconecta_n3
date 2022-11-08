package com.blm.qiubopay.models.bimbo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class QrDTO {

    @DatabaseField(generatedId = true)
    private Integer qrId;

    @DatabaseField
    private String folio;

    @DatabaseField
    private String qrJson;

    @DatabaseField
    private Integer status;

    @DatabaseField
    private Date date;

    public QrDTO() {

    }

    public Integer getQrId() {
        return qrId;
    }

    public void setQrId(Integer qrId) {
        this.qrId = qrId;
    }

    public String getQrJson() {
        return qrJson;
    }

    public void setQrJson(String qrJson) {
        this.qrJson = qrJson;
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

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
}
