
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Solicitud {

    @SerializedName("num_cliente")
    @Expose
    private String numCliente;
    @SerializedName("tipo_credito")
    @Expose
    private String tipoCredito;
    @SerializedName("cuota")
    @Expose
    private Double cuota;
    @SerializedName("solicitud")
    @Expose
    private String solicitud;
    @SerializedName("monto_prestamo")
    @Expose
    private Double montoPrestamo;
    @SerializedName("folio")
    @Expose
    private String folio;
    @SerializedName("fecha_creacion")
    @Expose
    private String fechaCreacion;
    @SerializedName("comentario")
    @Expose
    private Comentario comentario;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("nombre_credito")
    @Expose
    private String nombreCredito;

    public String getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(String numCliente) {
        this.numCliente = numCliente;
    }

    public String getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(String tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public Double getCuota() {
        return cuota;
    }

    public void setCuota(Double cuota) {
        this.cuota = cuota;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

    public Double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(Double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCredito() {
        return nombreCredito;
    }

    public void setNombreCredito(String nombreCredito) {
        this.nombreCredito = nombreCredito;
    }

}
