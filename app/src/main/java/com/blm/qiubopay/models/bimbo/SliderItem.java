package com.blm.qiubopay.models.bimbo;

public class SliderItem {

    private String texto;

    private Integer imagen;

    private Boolean activar;


    public SliderItem() {

    }

    public SliderItem(String texto, Integer imagen, Boolean activar) {
        this.texto = texto;
        this.imagen = imagen;
        this.activar = activar;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getImagen() {
        return imagen;
    }

    public void setImagen(Integer imagen) {
        this.imagen = imagen;
    }

    public Boolean getActivar() {
        return activar;
    }

    public void setActivar(Boolean activar) {
        this.activar = activar;
    }
}
