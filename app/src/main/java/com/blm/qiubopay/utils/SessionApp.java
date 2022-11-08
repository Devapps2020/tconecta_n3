package com.blm.qiubopay.utils;

import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.bimbo.CatProducts;
import com.blm.qiubopay.models.bimbo.FcCatalog;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.bimbo.SellerUserResponse;
import com.blm.qiubopay.models.fincomun.FCRequestDTO;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailer_Object;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecordResponse;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnlineResponse;
import com.blm.qiubopay.models.proceedings.QPAY_UserDataRecordResponse;

import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Response.Apertura.FCConsultaBanderaFCILAperturaResponse;
import mx.com.fincomun.origilib.Http.Response.Catalogos.FCCatalogosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBuscarSolicitudesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCConsultaOfertaBimboResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCConsultaCreditosResponse;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosNegocio;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosPersonales;
import mx.com.fincomun.origilib.Objects.Bancos.DHBancos;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaCreditos;
import mx.com.fincomun.origilib.Objects.Recompras.DHMovimientosRecompras;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;

public class SessionApp {

    private boolean stopHome = false;

    private boolean loginHome = false;

    private QPAY_FinancialInformation financialInformation;

    private FCRequestDTO fcRequestDTO;

    private FCCatalogosResponse catalog;

    private CatProducts catProducts;

    private PedidoRequest orderRequest;

    private Globals.MARCAS marca;

    private Globals.ORGANIZACION organizacion;

    private String categoria;


    private boolean requiredEdoCuenta;
    private String clabeEdoCuenta;
    private int idBancoEdoCuenta;

    private ArrayList<DHBancos> bancos;

    private FCConsultaCreditosResponse fcConsultaCreditosResponse;

    private FCConsultaOfertaBimboResponse fcConsultaOfertaBimboResponse;

    private FCBuscarSolicitudesResponse fcBuscarSolicitudesResponse;
    private FCConsultaBanderaFCILAperturaResponse fcBanderas;

    private DHMovimientosRecompras recompra;
    private List<DHMovimientosRecompras> listaRecompras;
    private String numClienteRecompra;
    private DHDatosPersonales datosPersonales;
    private DHDatosNegocio datosNegocio;

    private SellerUserResponse sellerUserResponse;

    private DHListaCreditos dhListaCreditos;

    private ArrayList<DHCuenta> cuentasCoDi;

    private QPAY_UserDataRecordResponse userDataRecord;

    private QPAY_AllDataRecordResponse allDataRecordResponse;

    private QPAY_DataRecordOnlineResponse dataRecordOnlineResponse;

    private ArrayList<QPAY_SalesRetailer_Object> compras;

    private static SessionApp ourInstance = new SessionApp();

    public static SessionApp getInstance() {
        return ourInstance;
    }

    public static void clearInstance() {
        ourInstance = new SessionApp();
    }

    private SessionApp() {

    }

    public boolean isStopHome() {
        return stopHome;
    }

    public void setStopHome(boolean stopHome) {
        this.stopHome = stopHome;
    }

    public QPAY_FinancialInformation getFinancialInformation() {

        if(financialInformation == null)
            financialInformation = new QPAY_FinancialInformation();

        return financialInformation;
    }

    public FCRequestDTO getFcRequestDTO() {

        if(fcRequestDTO == null)
            fcRequestDTO = AppPreferences.getFCRequest();

        return fcRequestDTO;
    }

    public void setFcRequestDTO(FCRequestDTO fcRequestDTO) {
        this.fcRequestDTO = fcRequestDTO;
    }

    public void setFinancialInformation(QPAY_FinancialInformation financialInformation) {
        this.financialInformation = financialInformation;
    }

    public boolean isLoginHome() {
        return loginHome;
    }

    public void setLoginHome(boolean loginHome) {
        this.loginHome = loginHome;
    }

    public FCCatalogosResponse getCatalog() {
        return catalog;
    }

    public void setCatalog(FCCatalogosResponse catalog) {
        this.catalog = catalog;
    }

    public FCConsultaCreditosResponse getFcConsultaCreditosResponse() {
        return fcConsultaCreditosResponse;
    }

    public void setFcConsultaCreditosResponse(FCConsultaCreditosResponse fcConsultaCreditosResponse) {
        this.fcConsultaCreditosResponse = fcConsultaCreditosResponse;
    }

    public CatProducts getCatProducts() {
        return catProducts;
    }

    public void setCatProducts(CatProducts catProducts) {
        this.catProducts = catProducts;
    }

    public Globals.MARCAS getMarca() {
        return marca;
    }

    public Globals.ORGANIZACION getOrganizacion() {

        if(organizacion == null)
            return  Globals.ORGANIZACION.Bimbo;

        return organizacion;
    }

    public void setOrganizacion(Globals.ORGANIZACION organizacion) {
        this.organizacion = organizacion;
    }

    public void setMarca(Globals.MARCAS marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public PedidoRequest getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(PedidoRequest orderRequest) {
        this.orderRequest = orderRequest;
    }

    public SellerUserResponse getSellerUserResponse() {
        return sellerUserResponse;
    }

    public void setSellerUserResponse(SellerUserResponse sellerUserResponse) {
        this.sellerUserResponse = sellerUserResponse;
    }

    public FCConsultaOfertaBimboResponse getFcConsultaOfertaBimboResponse() {
        return fcConsultaOfertaBimboResponse;
    }

    public void setFcConsultaOfertaBimboResponse(FCConsultaOfertaBimboResponse fcConsultaOfertaBimboResponse) {
        this.fcConsultaOfertaBimboResponse = fcConsultaOfertaBimboResponse;
    }

    public FCBuscarSolicitudesResponse getFcBuscarSolicitudesResponse() {
        return fcBuscarSolicitudesResponse;
    }

    public void setFcBuscarSolicitudesResponse(FCBuscarSolicitudesResponse fcBuscarSolicitudesResponse) {
        this.fcBuscarSolicitudesResponse = fcBuscarSolicitudesResponse;
    }

    public FCConsultaBanderaFCILAperturaResponse getFcBanderas() {
        return fcBanderas;
    }

    public void setFcBanderas(FCConsultaBanderaFCILAperturaResponse fcBanderas) {
        this.fcBanderas = fcBanderas;
    }

    public DHListaCreditos getDhListaCreditos() {
        return dhListaCreditos;
    }

    public void setDhListaCreditos(DHListaCreditos dhListaCreditos) {
        this.dhListaCreditos = dhListaCreditos;
    }

    public DHMovimientosRecompras getRecompra() {
        return recompra;
    }

    public void setRecompra(DHMovimientosRecompras recompra) {
        this.recompra = recompra;
    }

    public List<DHMovimientosRecompras> getListaRecompras() {
        return listaRecompras;
    }

    public void setListaRecompras(List<DHMovimientosRecompras> listaRecompras) {
        this.listaRecompras = listaRecompras;
    }

    public String getNumClienteRecompra() {
        return numClienteRecompra;
    }

    public void setNumClienteRecompra(String numClienteRecompra) {
        this.numClienteRecompra = numClienteRecompra;
    }

    public DHDatosPersonales getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(DHDatosPersonales datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    public DHDatosNegocio getDatosNegocio() {
        return datosNegocio;
    }

    public void setDatosNegocio(DHDatosNegocio datosNegocio) {
        this.datosNegocio = datosNegocio;
    }

    public ArrayList<DHCuenta> getCuentasCoDi() {
        if (cuentasCoDi != null)
            return cuentasCoDi;
        else
            return new ArrayList<>();
    }

    public void setCuentasCoDi(ArrayList<DHCuenta> cuentasCoDi) {
        this.cuentasCoDi = cuentasCoDi;
    }

    public QPAY_UserDataRecordResponse getUserDataRecord() {
        return userDataRecord;
    }

    public void setUserDataRecord(QPAY_UserDataRecordResponse userDataRecord) {
        this.userDataRecord = userDataRecord;
    }

    public QPAY_AllDataRecordResponse getAllDataRecordResponse() {
        return allDataRecordResponse;
    }

    public void setAllDataRecordResponse(QPAY_AllDataRecordResponse allDataRecordResponse) {
        this.allDataRecordResponse = allDataRecordResponse;
    }

    public QPAY_DataRecordOnlineResponse getDataRecordOnlineResponse() {
        return dataRecordOnlineResponse;
    }

    public void setDataRecordOnlineResponse(QPAY_DataRecordOnlineResponse dataRecordOnlineResponse) {
        this.dataRecordOnlineResponse = dataRecordOnlineResponse;
    }

    public List<QPAY_SalesRetailer_Object> getCompras() {
        return compras;
    }

    public boolean isRequiredEdoCuenta() {
        return requiredEdoCuenta;
    }

    public void setRequiredEdoCuenta(boolean requiredEdoCuenta) {
        this.requiredEdoCuenta = requiredEdoCuenta;
    }

    public String getClabeEdoCuenta() {
        return clabeEdoCuenta;
    }

    public void setClabeEdoCuenta(String clabeEdoCuenta) {
        this.clabeEdoCuenta = clabeEdoCuenta;
    }

    public int getIdBancoEdoCuenta() {
        return idBancoEdoCuenta;
    }

    public void setIdBancoEdoCuenta(int idBancoEdoCuenta) {
        this.idBancoEdoCuenta = idBancoEdoCuenta;
    }

    public void setBancos(ArrayList<DHBancos> bancos) {
        this.bancos = bancos;
    }

    public ArrayList<DHBancos> getBancos() {
        return bancos;
    }
}
