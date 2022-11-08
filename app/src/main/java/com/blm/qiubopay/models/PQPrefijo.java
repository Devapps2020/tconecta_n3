package com.blm.qiubopay.models;

public class PQPrefijo {

    private String empresa;

    private String prefijo;

    private Integer image;

    public PQPrefijo() {

    }

    public PQPrefijo(String empresa, String prefijo, Integer image) {
        this.empresa = empresa;
        this.prefijo = prefijo;
        //this.image = image;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
