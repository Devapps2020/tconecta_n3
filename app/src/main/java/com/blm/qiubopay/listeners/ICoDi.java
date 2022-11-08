package com.blm.qiubopay.listeners;


import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaDispositivoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaUsuarioRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAperturaCuentaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCEnvioTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCGuardarDocumentoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCPLDRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaCredencialRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Pago.AccionTransaccion;
import mx.com.fincomun.origilib.Http.Response.Devoluciones.ObjetoPagoDevolucion;
import mx.com.fincomun.origilib.Model.Apertura.CargaDocumento;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.com.fincomun.origilib.Objects.Notificaciones.Devoluciones.InfoDevolucion;
import mx.com.fincomun.origilib.Objects.Notificaciones.Pago.InfoSolicitudPago;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.interfaces.IFunction;

public interface ICoDi {

    void registroInicial(IFunction function);

    void crearKeysource(String codR,IFunction function);

    void guardarKeysource(IFunction function);

    void cargarKeysource(IFunction function);

    void registroSubsecuente(IFunction function);

    void registroAppPorOmision(IFunction function);

    void validacionCuenta(DHCuenta cuenta, IFunction function);

    void statusValidacionCuenta(IFunction function);

    void decifrarQR(String qr, IFunction function);

    void login(String user, String pass, IFunction function);

    void pago(AccionTransaccion type, String referencia, String token, ModelObjetoCobro objetoCobro, DHCuenta cuenta, IFunction function);

    void pago(AccionTransaccion type, String referencia, String token, InfoSolicitudPago objetoCobro, DHCuenta cuenta, IFunction function);

    void pago(AccionTransaccion type, String referencia, String token, InfoDevolucion objetoCobro, DHCuenta cuenta, IFunction function);

    void pago(AccionTransaccion type, String referencia, String token, ObjetoPagoDevolucion objetoPagoDevolucion, DHCuenta cuenta, IFunction function);

    void crearQR(Double monto, String clabe, String referencia, String concepto, IFunction function);

    void confirmSMS(IFunction function);

    void selectCuenta(IFunction<DHCuenta>... function);

    void transferencias(String token, IFunction iFunction);

    void consultaBandera(String bimboId, String token, IFunction function);

    void validaCliente(String celular, String email, IFunction function);

    void envioToken(FCEnvioTokenRequest request, IFunction function);

    void validaToken(FCValidaTokenRequest request, IFunction function);

    void altaUsuario(FCAltaUsuarioRequest request, IFunction function);

    void altaDispositivo(FCAltaDispositivoRequest request, IFunction function);

    void confirmToken(String title, IFunction function);

    void consultaCuentas(String token, IFunction function);



    void avisoPrivacidad(String token, IFunction function);

    void nuevaSolicitud(IFunction function);

    void validacionCURP(String folio, String curp, IFunction function);

    void validaCredencial(FCValidaCredencialRequest request, IFunction function);

    void pld(FCPLDRequest request, IFunction function);

    void cargaDocumento(String archivo, CargaDocumento.TipoCargaDocumento tipo, FCGuardarDocumentoRequest request, IFunction function);

    void aperturaCuenta(FCAperturaCuentaRequest request, IFunction function);

    void actualizaBandera(String bimboId, String token, IFunction function);

    void registraCliente(String celular, String email, String token, IFunction function);

    void devoluciones(String inicio, String fin, String token, IFunction function);

    void consultaMensajeCobro(DHDatosTransferencia trans, IFunction function);

    void consultaMovimientos (String token,String numCuenta ,IFunction function);



}
