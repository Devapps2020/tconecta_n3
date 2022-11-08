package com.blm.qiubopay.responses.dynamicdata;

public class DynamicDataCancelOrder {

    private int codigo;
    private String message;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
