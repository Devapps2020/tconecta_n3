package com.blm.qiubopay.models.remesas;

import java.io.Serializable;

public class TC_Beneficiario2 implements Serializable {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;

    private int idTipoIdentificacion;
    private String numeroIdentificacion;
    private String imagenFrenteIdentificacion;
    private String imagenReversoIdentificacion;
    private String imagenFirmaIdentificacion;
    private int idGenero;
    private String fechaNacimiento;

    public TC_Beneficiario2(){
        this.setPrimerNombre("");
        this.setSegundoNombre("");
        this.setPrimerApellido("");
        this.setSegundoApellido("");
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    //--

    public int getIdTipoIdentificacion() {
        return idTipoIdentificacion;
    }

    public void setIdTipoIdentificacion(int idTipoIdentificacion) {
        this.idTipoIdentificacion = idTipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getImagenFrenteIdentificacion() {
        return imagenFrenteIdentificacion;
    }

    public void setImagenFrenteIdentificacion(String imagenFrenteIdentificacion) {
        this.imagenFrenteIdentificacion = imagenFrenteIdentificacion;
    }

    public String getImagenReversoIdentificacion() {
        return imagenReversoIdentificacion;
    }

    public void setImagenReversoIdentificacion(String imagenReversoIdentificacion) {
        this.imagenReversoIdentificacion = imagenReversoIdentificacion;
    }

    public String getImagenFirmaIdentificacion() {
        return imagenFirmaIdentificacion;
    }

    public void setImagenFirmaIdentificacion(String imagenFirmaIdentificacion) {
        this.imagenFirmaIdentificacion = imagenFirmaIdentificacion;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}