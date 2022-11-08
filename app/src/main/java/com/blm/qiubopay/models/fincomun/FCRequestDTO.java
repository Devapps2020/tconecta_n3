package com.blm.qiubopay.models.fincomun;

import com.blm.qiubopay.utils.Globals;

import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCOfertaSeleccionadaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCPreOfertaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAdicionalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAnalisisRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAvisoPrivacidadRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBeneficiariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBuscarSolicitudesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComprobantesNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCConsultaBuroRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosBancariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosPersonalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCGastosNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCImagenFirmaRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCReferenciasRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSMSRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCValidaSMSRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCVentaCompraRequest;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosTelefonicos;

public class FCRequestDTO {

    private String folio;
    private String token;
    private String numCliente;
    private Integer step;
    private String genero;
    private boolean medico;

    private Integer validSMS;

    private Double tasa;
    private FCSimuladorRequest simulador;
    private FCAvisoPrivacidadRequest privacidad;
    private FCDatosNegocioRequest datos_negocio;
    private FCGastosNegocioRequest gastos_negocio;
    private FCImagenFirmaRequest imagen_firma;
    private FCPreOfertaCreditoRequest pre_oferta_credito;
    private FCVentaCompraRequest venta_compra;
    private FCConsultaBuroRequest consulta_buro;
    private FCComprobantesNegocioRequest comprobante_negocio;
    private FCDatosPersonalesRequest datos_personales;
    private FCIdentificacionRequest identificacion;
    private List<DHDatosTelefonicos> datosTelefonicos;
    private String emailAnalisis;
    private FCSMSRequest sms;
    private FCBuscarSolicitudesRequest buscar_solicitudes;
    private FCAnalisisRequest analisis;

    private FCOfertaSeleccionadaCreditoRequest ofertas_seleccionada_credito;
    private FCReferenciasRequest referencias;
    private FCBeneficiariosRequest beneficiarios;
    private FCValidaSMSRequest validacion_sms;
    private FCAdicionalesRequest adicionales;

    private FCDatosBancariosRequest fcDatosBancariosRequest;

    public FCSimuladorRequest getSimulador() {
        return simulador;
    }

    public void setSimulador(FCSimuladorRequest simulador) {
        this.simulador = simulador;
    }

    public FCAvisoPrivacidadRequest getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(FCAvisoPrivacidadRequest privacidad) {
        this.privacidad = privacidad;
    }

    public FCDatosNegocioRequest getDatos_negocio() {
        return datos_negocio;
    }

    public void setDatos_negocio(FCDatosNegocioRequest datos_negocio) {
        this.datos_negocio = datos_negocio;
    }

    public FCGastosNegocioRequest getGastos_negocio() {
        return gastos_negocio;
    }

    public void setGastos_negocio(FCGastosNegocioRequest gastos_negocio) {
        this.gastos_negocio = gastos_negocio;
    }

    public FCImagenFirmaRequest getImagen_firma() {
        return imagen_firma;
    }

    public void setImagen_firma(FCImagenFirmaRequest imagen_firma) {
        this.imagen_firma = imagen_firma;
    }

    public FCPreOfertaCreditoRequest getPre_oferta_credito() {
        return pre_oferta_credito;
    }

    public void setPre_oferta_credito(FCPreOfertaCreditoRequest pre_oferta_credito) {
        this.pre_oferta_credito = pre_oferta_credito;
    }

    public FCVentaCompraRequest getVenta_compra() {
        return venta_compra;
    }

    public void setVenta_compra(FCVentaCompraRequest venta_compra) {
        this.venta_compra = venta_compra;
    }

    public FCConsultaBuroRequest getConsulta_buro() {
        return consulta_buro;
    }

    public void setConsulta_buro(FCConsultaBuroRequest consulta_buro) {
        this.consulta_buro = consulta_buro;
    }

    public FCComprobantesNegocioRequest getComprobante_negocio() {
        return comprobante_negocio;
    }

    public void setComprobante_negocio(FCComprobantesNegocioRequest comprobante_negocio) {
        this.comprobante_negocio = comprobante_negocio;
    }

    public FCDatosPersonalesRequest getDatos_personales() {
        return datos_personales;
    }

    public void setDatos_personales(FCDatosPersonalesRequest datos_personales) {
        this.datos_personales = datos_personales;
    }

    public FCIdentificacionRequest getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(FCIdentificacionRequest identificacion) {
        this.identificacion = identificacion;
    }

    public FCSMSRequest getSms() {
        return sms;
    }

    public void setSms(FCSMSRequest sms) {
        this.sms = sms;
    }

    public FCBuscarSolicitudesRequest getBuscar_solicitudes() {
        return buscar_solicitudes;
    }

    public void setBuscar_solicitudes(FCBuscarSolicitudesRequest buscar_solicitudes) {
        this.buscar_solicitudes = buscar_solicitudes;
    }

    public FCAnalisisRequest getAnalisis() {
        return analisis;
    }

    public void setAnalisis(FCAnalisisRequest analisis) {
        this.analisis = analisis;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Globals.NUMBER  step) {
        this.step = step.ordinal();
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNumCliente() {
        return numCliente;
    }

    public void setNumCliente(String numCliente) {
        this.numCliente = numCliente;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public FCOfertaSeleccionadaCreditoRequest getOfertas_seleccionada_credito() {
        return ofertas_seleccionada_credito;
    }

    public void setOfertas_seleccionada_credito(FCOfertaSeleccionadaCreditoRequest ofertas_seleccionada_credito) {
        this.ofertas_seleccionada_credito = ofertas_seleccionada_credito;
    }

    public FCReferenciasRequest getReferencias() {
        return referencias;
    }

    public void setReferencias(FCReferenciasRequest referencias) {
        this.referencias = referencias;
    }

    public FCBeneficiariosRequest getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(FCBeneficiariosRequest beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public FCValidaSMSRequest getValidacion_sms() {
        return validacion_sms;
    }

    public void setValidacion_sms(FCValidaSMSRequest validacion_sms) {
        this.validacion_sms = validacion_sms;
    }

    public Boolean getValidSMS() {

        if (validSMS == null)
            return false;

        return validSMS == 1;
    }

    public void setValidSMS(Integer validSMS) {
        this.validSMS = validSMS;
    }

    public FCAdicionalesRequest getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(FCAdicionalesRequest adicionales) {
        this.adicionales = adicionales;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getTasa() {
        return tasa;
    }

    public void setTasa(Double tasa) {
        this.tasa = tasa;
    }

    public FCDatosBancariosRequest getFcDatosBancariosRequest() {
        return fcDatosBancariosRequest;
    }

    public void setFcDatosBancariosRequest(FCDatosBancariosRequest fcDatosBancariosRequest) {
        this.fcDatosBancariosRequest = fcDatosBancariosRequest;
    }

    public boolean isMedico() {
        return medico;
    }

    public void setMedico(boolean medico) {
        this.medico = medico;
    }

    public List<DHDatosTelefonicos> getDatosTelefonicos() {
        return datosTelefonicos;
    }

    public void setDatosTelefonicos(List<DHDatosTelefonicos> datosTelefonicos) {
        this.datosTelefonicos = datosTelefonicos;
    }

    public String getEmailAnalisis() {
        return emailAnalisis;
    }

    public void setEmailAnalisis(String emailAnalisis) {
        this.emailAnalisis = emailAnalisis;
    }
}
