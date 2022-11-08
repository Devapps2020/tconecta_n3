
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Matriz {

    @SerializedName("codigo_sibamex")
    @Expose
    private String codigoSibamex;
    @SerializedName("id_producto_credito")
    @Expose
    private Integer idProductoCredito;
    @SerializedName("monto_minimo")
    @Expose
    private Double montoMinimo;
    @SerializedName("monto_maximo")
    @Expose
    private String montoMaximo;
    @SerializedName("tasa_minima")
    @Expose
    private String tasaMinima;
    @SerializedName("tasa_maxima")
    @Expose
    private String tasaMaxima;
    @SerializedName("plazo_semanas")
    @Expose
    private List<String> plazoSemanas = null;
    @SerializedName("frecuencia_pago")
    @Expose
    private List<String> frecuenciaPago = null;
    @SerializedName("id_tipo_producto")
    @Expose
    private Integer idTipoProducto;
    @SerializedName("origen_actual")
    @Expose
    private String origenActual;
    @SerializedName("descripcion_tecnica")
    @Expose
    private String descripcionTecnica;
    @SerializedName("descripcion_comercial")
    @Expose
    private String descripcionComercial;
    @SerializedName("id_mod_credito")
    @Expose
    private Integer idModCredito;

    public String getCodigoSibamex() {
        return codigoSibamex;
    }

    public void setCodigoSibamex(String codigoSibamex) {
        this.codigoSibamex = codigoSibamex;
    }

    public Integer getIdProductoCredito() {
        return idProductoCredito;
    }

    public void setIdProductoCredito(Integer idProductoCredito) {
        this.idProductoCredito = idProductoCredito;
    }

    public Double getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(Double montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public String getMontoMaximo() {
        return montoMaximo;
    }

    public void setMontoMaximo(String montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    public String getTasaMinima() {
        return tasaMinima;
    }

    public void setTasaMinima(String tasaMinima) {
        this.tasaMinima = tasaMinima;
    }

    public String getTasaMaxima() {
        return tasaMaxima;
    }

    public void setTasaMaxima(String tasaMaxima) {
        this.tasaMaxima = tasaMaxima;
    }

    public List<String> getPlazoSemanas() {
        return plazoSemanas;
    }

    public void setPlazoSemanas(List<String> plazoSemanas) {
        this.plazoSemanas = plazoSemanas;
    }

    public List<String> getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(List<String> frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

    public Integer getIdTipoProducto() {
        return idTipoProducto;
    }

    public void setIdTipoProducto(Integer idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }

    public String getOrigenActual() {
        return origenActual;
    }

    public void setOrigenActual(String origenActual) {
        this.origenActual = origenActual;
    }

    public String getDescripcionTecnica() {
        return descripcionTecnica;
    }

    public void setDescripcionTecnica(String descripcionTecnica) {
        this.descripcionTecnica = descripcionTecnica;
    }

    public String getDescripcionComercial() {
        return descripcionComercial;
    }

    public void setDescripcionComercial(String descripcionComercial) {
        this.descripcionComercial = descripcionComercial;
    }

    public Integer getIdModCredito() {
        return idModCredito;
    }

    public void setIdModCredito(Integer idModCredito) {
        this.idModCredito = idModCredito;
    }

}
