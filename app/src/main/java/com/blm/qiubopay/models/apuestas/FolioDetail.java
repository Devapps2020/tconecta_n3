package com.blm.qiubopay.models.apuestas;

import java.io.Serializable;

public class FolioDetail implements Serializable {
    private int StatusCode;
    private String Message;
    private  FolioData Data;

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public FolioData getData() {
        return Data;
    }

    public void setData(FolioData data) {
        Data = data;
    }
}
