package com.blm.qiubopay.helpers;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.interfaces.ILongOperation;
import com.blm.qiubopay.listeners.ICoDi;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.utils.SessionApp;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCActualizaBanderaFCILAperturaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaDispositivoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaUsuarioRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAperturaCuentaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCConsultaBanderaFCILAperturaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCEnvioTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCGuardarDocumentoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCPLDRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCRegistraClienteRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaClienteRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaCredencialRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidacionCURPRequest;
import mx.com.fincomun.origilib.Http.Request.Devoluciones.FCMovsDevolucionRequest;
import mx.com.fincomun.origilib.Http.Request.FCil.FCUltimosMovimientosRequest;
import mx.com.fincomun.origilib.Http.Request.Login.FCLoginRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAvisoPrivacidadRequest;
import mx.com.fincomun.origilib.Http.Request.Pago.AccionTransaccion;
import mx.com.fincomun.origilib.Http.Request.Pago.FCPagoRequest;
import mx.com.fincomun.origilib.Http.Request.Pago.ModelOrdenante;
import mx.com.fincomun.origilib.Http.Request.Retiro.FCConsultaCuentasRequest;
import mx.com.fincomun.origilib.Http.Request.Transferencias.FCTransferenciasRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCActualizaBanderaFCILAperturaResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaDispositivoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaUsuarioResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAperturaCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCConsultaBanderaFCILAperturaResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCEnvioTokenResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCGuardarDocumentoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCNuevaSolicitudResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCPLDResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCRegistraClienteResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaClienteResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaCredencialResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaTokenResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidacionCURPResponse;
import mx.com.fincomun.origilib.Http.Response.Banxico.ConsultaMensajeCobro.BanxicoConsultaMCResponse;
import mx.com.fincomun.origilib.Http.Response.Banxico.ValidaCuenta.FCValidacionCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.Devoluciones.FCMovsDevolucionResponse;
import mx.com.fincomun.origilib.Http.Response.Devoluciones.ObjetoPagoDevolucion;
import mx.com.fincomun.origilib.Http.Response.FCRespuesta;
import mx.com.fincomun.origilib.Http.Response.Login.FCLoginResponse;
import mx.com.fincomun.origilib.Http.Response.Pago.FCPagoResponse;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Http.Response.Transferencias.FCTransferenciasResponse;
import mx.com.fincomun.origilib.Model.Apertura.ActualizaBanderaFCIL;
import mx.com.fincomun.origilib.Model.Apertura.AltaDispositivo;
import mx.com.fincomun.origilib.Model.Apertura.AltaUsuario;
import mx.com.fincomun.origilib.Model.Apertura.AperturaCuenta;
import mx.com.fincomun.origilib.Model.Apertura.CargaDocumento;
import mx.com.fincomun.origilib.Model.Apertura.ConsultaBanderaFCIL;
import mx.com.fincomun.origilib.Model.Apertura.NuevaSolicitud;
import mx.com.fincomun.origilib.Model.Apertura.PLD;
import mx.com.fincomun.origilib.Model.Apertura.RegistraCliente;
import mx.com.fincomun.origilib.Model.Apertura.TokenValidacion;
import mx.com.fincomun.origilib.Model.Apertura.ValidaCliente;
import mx.com.fincomun.origilib.Model.Apertura.ValidaCredencial;
import mx.com.fincomun.origilib.Model.Apertura.ValidacionCURP;
import mx.com.fincomun.origilib.Model.Banxico.CoDi;
import mx.com.fincomun.origilib.Model.Banxico.ItemStore;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ActorOperacion;
import mx.com.fincomun.origilib.Model.Banxico.Objects.BanxicoDesifradoMC;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.com.fincomun.origilib.Model.Devoluciones.Devoluciones;
import mx.com.fincomun.origilib.Model.FCil.UltimosMovimientos;
import mx.com.fincomun.origilib.Model.Login.Login;
import mx.com.fincomun.origilib.Model.Originacion.AvisoPrivacidad;
import mx.com.fincomun.origilib.Model.Pago.Pago;
import mx.com.fincomun.origilib.Model.Retiro.ConsultaCuentas;
import mx.com.fincomun.origilib.Model.Transferencias.Transferencias;
import mx.com.fincomun.origilib.Objects.Notificaciones.Devoluciones.InfoDevolucion;
import mx.com.fincomun.origilib.Objects.Notificaciones.Pago.InfoSolicitudPago;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.interfaces.IFunction;

public class HCoDi implements ICoDi {

    public static DHCuenta cuentaSeleccionada = AppPreferences.getCuentaCodi();

    public static String TAG = "Fincomún";

    public static String claveInstitucion = "90634";
    private String tipoCuenta = "40";

    public static String usuario = "";
    public static String password ="";
    public static String numCliente = "";
    public static String nomCliente = "";
    private String celular = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone();
    private String imei = "";

    private AlertDialog alertDialog;
    private MenuActivity context;
    private CoDi coDi;

    public HCoDi(MenuActivity context) {
        this.context = context;
         imei = Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    @Override
    public void registroInicial(IFunction function) {

        coDi = new CoDi(context);
        coDi.registroInicial(celular, new CoDi.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRespuesta respuesta = (FCRespuesta) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void crearKeysource(String codR , IFunction function) {

        coDi.crearKeysource(codR, celular, new CoDi.onProcess() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRespuesta respuesta = (FCRespuesta) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void guardarKeysource(IFunction function) {

        coDi.guardarKeysource(context, password, new CoDi.onProcess() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRespuesta respuesta = (FCRespuesta) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void cargarKeysource(IFunction function) {

        CoDi coDi = new CoDi(context);

        coDi.cargarKeysource(context, password, new CoDi.onProcess() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRespuesta respuesta = (FCRespuesta) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void registroSubsecuente(IFunction function) {
        CoDi coDi = new CoDi(context);

        new HLongOperation(new ILongOperation() {
            @Override
            public String doInBackground() {
                try {
                    return FirebaseInstanceId.getInstance().getToken(ItemStore.INSTANCE.getIdN(),"FCM");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "";
                }
            }

            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(int code, String result) {

                coDi.registroSubsecuente(result, new CoDi.onRequest() {
                    @Override
                    public <E> void onSuccess(E Object) {
                        FCRespuesta respuesta = (FCRespuesta) Object;
                        Logger.d(new Gson().toJson(respuesta) );
                        function.execute(respuesta);
                    }
                    @Override
                    public void onFailure(String mensaje) {
                        Logger.d(mensaje);
                        context.loading(false);
                        context.alert(TAG, mensaje);
                    }
                });

            }
        }).execute();

    }

    @Override
    public void registroAppPorOmision(IFunction function) {

        CoDi coDi = new CoDi(context);
        coDi.registroAppPorOmision(new CoDi.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRespuesta respuesta = (FCRespuesta) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void validacionCuenta(DHCuenta cuenta, IFunction function) {

        CoDi coDi = new CoDi(context);
        coDi.validarCuenta(cuenta, claveInstitucion, new CoDi.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCValidacionCuentaResponse respuesta = (FCValidacionCuentaResponse) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void statusValidacionCuenta(IFunction function) {

        CoDi coDi = new CoDi(context);
        coDi.consultaEstatusValidacion(AppPreferences.getCodiRastreo(), new CoDi.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRespuesta respuesta = (FCRespuesta) Object;
                Logger.d(new Gson().toJson(respuesta) );
                function.execute(respuesta);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void decifrarQR(String qr, IFunction function) {

        CoDi coDi = new CoDi(context);
        coDi.descifrarQR(qr, new CoDi.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                ModelObjetoCobro response = (ModelObjetoCobro) Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void login(String user, String pass, IFunction function) {

        Login login = new Login(context);

        Logger.d(user + " :::: " + pass + " :::: " + imei );
        login.login(new FCLoginRequest(user, pass, imei), new Login.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCLoginResponse response = (FCLoginResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
                function.execute(null);
            }
        });
    }

    @Override
    public void pago(AccionTransaccion type, String referencia, String token, ModelObjetoCobro objetoCobro, DHCuenta cuenta, IFunction function) {

        Pago pago = new Pago(context);

        ModelOrdenante modelOrdenante = new ModelOrdenante(numCliente, nomCliente, tipoCuenta, cuenta.getClabeCuenta(), referencia);

        FCPagoRequest obj = new FCPagoRequest(objetoCobro, modelOrdenante, cuenta, numCliente, imei, type, token, 0.0, 0.0);
        Logger.d(new Gson().toJson(obj) );

        pago.pago(obj, new Pago.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCPagoResponse response = (FCPagoResponse)Object;
                Logger.d(new Gson().toJson(response) );

                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void pago(AccionTransaccion type, String referencia, String token, InfoSolicitudPago objetoCobro, DHCuenta cuenta, IFunction function) {

        Pago pago = new Pago(context);

        ModelOrdenante modelOrdenante = new ModelOrdenante(numCliente, nomCliente, tipoCuenta, cuenta.getClabeCuenta(), referencia);

        FCPagoRequest obj = new FCPagoRequest(objetoCobro, modelOrdenante, cuenta, numCliente, imei, type, token,0.0,0.0);
        Logger.d(new Gson().toJson(obj) );

        pago.pago(obj, new Pago.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCPagoResponse response = (FCPagoResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void pago(AccionTransaccion type, String referencia, String token, InfoDevolucion objetoCobro, DHCuenta cuenta, IFunction function) {

        Pago pago = new Pago(context);

        ModelOrdenante modelOrdenante = new ModelOrdenante(numCliente, nomCliente, tipoCuenta, cuenta.getClabeCuenta(), referencia);

        FCPagoRequest obj = new FCPagoRequest(objetoCobro, modelOrdenante, cuenta, numCliente, imei, type, token, 0.0,0.0);
        Logger.d(new Gson().toJson(obj) );

        pago.pago(obj, new Pago.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCPagoResponse response = (FCPagoResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }


    @Override
    public void pago(AccionTransaccion type, String referencia, String token, ObjetoPagoDevolucion objetoPagoDevolucion, DHCuenta cuenta, IFunction function) {

        Pago pago = new Pago(context);

        ModelOrdenante modelOrdenante = new ModelOrdenante(numCliente, nomCliente, tipoCuenta, cuenta.getClabeCuenta(), referencia);
        FCPagoRequest obj = new FCPagoRequest(objetoPagoDevolucion, modelOrdenante, cuenta, numCliente, imei, type, token, 0.0,0.0);
        Logger.d(new Gson().toJson(obj) );

        pago.pago(obj, new Pago.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCPagoResponse response = (FCPagoResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void crearQR(Double monto, String clabe, String referencia, String concepto,  IFunction function) {

        CoDi coDi = new CoDi(context);

        coDi.generarQR(monto, nomCliente.toUpperCase() , clabe, claveInstitucion, referencia, concepto, new CoDi.onProcess() {
                    @Override
                    public <E> void onSuccess(E Object) {
                        String QR = String.valueOf(Object);
                        Logger.d(new Gson().toJson(QR) );
                        function.execute(QR);
                    }
                    @Override
                    public void onFailure(String mensaje) {
                        Logger.d(mensaje);
                        context.loading(false);
                        context.alert(TAG, mensaje);
                    }
                });

    }

    @Override
    public void confirmSMS(IFunction listener) {

        alertDialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert_code_codi, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);

        PinView pin = view.findViewById(R.id.edit_pin);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listener.execute(pin.getText().toString());

            }
        });

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    @Override
    public void selectCuenta(IFunction<DHCuenta>... function) {

        DHCuenta seleccionada = new DHCuenta();

        alertDialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert_cuenta_codi, null);

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);
        EditSpinner spCuenta = view.findViewById(R.id.edt_cuenta_select);
        spCuenta.setLines(2);
        spCuenta.setSingleLine(false);

        ArrayList<ModelSpinner> infoCuenta = new ArrayList();

        for(DHCuenta cue : SessionApp.getInstance().getCuentasCoDi())
            infoCuenta.add(new ModelSpinner(cue.getNombreCuenta(), "" + cue.getClabeCuenta()));

        context.setDataSpinner(spCuenta, infoCuenta, new IFunction<String>() {
            @Override
            public void execute(String... data) {

                if(data[0] == null || data[0].isEmpty())
                    return;

                for(DHCuenta cue : SessionApp.getInstance().getCuentasCoDi()) {
                    if(data[0].equals(cue.getClabeCuenta())) {
                        btn_aceptar.setEnabled(true);
                        seleccionada.setNombreCuenta(cue.getNombreCuenta());
                        seleccionada.setNumeroCuenta(cue.getNumeroCuenta());
                        seleccionada.setClabeCuenta(cue.getClabeCuenta());
                        seleccionada.setSaldo(cue.getSaldo());
                        return;
                    }
                }
            }
        });

        btn_aceptar.setEnabled(false);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if(function.length > 0)
                    function[0].execute(seleccionada);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                if(function.length > 1)
                    function[1].execute();

            }
        });

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    @Override
    public void transferencias(String token, IFunction function) {

        Transferencias transferencias = new Transferencias(context);
        FCTransferenciasRequest request = new FCTransferenciasRequest(numCliente, token);
        transferencias.getListaTransferencias(request, new Transferencias.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                FCTransferenciasResponse response = (FCTransferenciasResponse)e;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String s) {
                Logger.d(s);
                context.loading(false);
                context.alert(TAG, s);
            }
        });

    }

    @Override
    public void consultaBandera(String bimboId, String token, IFunction function) {

        ConsultaBanderaFCIL consultaBanderaFCIL = new ConsultaBanderaFCIL(context);

        FCConsultaBanderaFCILAperturaRequest request = new FCConsultaBanderaFCILAperturaRequest();
        request.setIdBimbo(bimboId);
        request.setTokenJwt(token);
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        consultaBanderaFCIL.postConsultaBanderaFCIL(request, new ConsultaBanderaFCIL.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCConsultaBanderaFCILAperturaResponse response = (FCConsultaBanderaFCILAperturaResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String s) {
                Logger.d(s);
                context.loading(false);
                context.alert(TAG, s);
            }
        });


    }

    @Override
    public void validaCliente(String celular, String email, IFunction function) {

        ValidaCliente validaCliente = new ValidaCliente(context);

        FCValidaClienteRequest request = new FCValidaClienteRequest();
        request.setCelular(celular);
        request.setEmail(email);

        validaCliente.postValidaCliente(request, new ValidaCliente.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCValidaClienteResponse response = (FCValidaClienteResponse) Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void altaUsuario(FCAltaUsuarioRequest request, IFunction function) {

        AltaUsuario altaUsuario = new AltaUsuario(context);
        altaUsuario.postAltaUsuario(request, new AltaUsuario.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCAltaUsuarioResponse response = (FCAltaUsuarioResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
                function.execute(null);
            }
        });

    }

    @Override
    public void altaDispositivo(FCAltaDispositivoRequest request, IFunction function) {

        AltaDispositivo altaDispositivo = new AltaDispositivo(context);

        altaDispositivo.postAltaDispositivo(request, new AltaDispositivo.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCAltaDispositivoResponse response = (FCAltaDispositivoResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
                function.execute(null);
            }
        });

    }

    @Override
    public void actualizaBandera(String bimboId, String token, IFunction function) {

        ActualizaBanderaFCIL actualizaBanderaFCIL = new ActualizaBanderaFCIL(context);

        FCActualizaBanderaFCILAperturaRequest request = new FCActualizaBanderaFCILAperturaRequest();
        request.setIdBimbo(bimboId);
        request.setTokenJwt(token);
        Logger.d(new Gson().toJson(request));

        actualizaBanderaFCIL.postActualizaBanderaFCIL(request, new ActualizaBanderaFCIL.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCActualizaBanderaFCILAperturaResponse response = (FCActualizaBanderaFCILAperturaResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String s) {
                Logger.d(s);
                context.loading(false);
                context.alert(TAG, s);
            }
        });

    }

    @Override
    public void confirmToken(String title, IFunction listener) {

        String titulo = "Código de verificación Correo";

        if(title == null) {
            titulo = "Código de verificación SMS";
            title ="\nEnviamos un SMS a tu celular registrado\ncon un código de verificación\n\nPor favor ingresalo aquí";
        }

        alertDialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert_code_enrolamiento, null);

        TextView text_sms = view.findViewById(R.id.text_sms);
        TextView text_title = view.findViewById(R.id.text_title);
        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);

        text_title.setText(titulo);
        text_sms.setText(title);

        PinView pin = view.findViewById(R.id.edit_pin);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listener.execute(pin.getText().toString());

            }
        });

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        builder = null;

    }

    @Override
    public void consultaCuentas(String token, IFunction function) {

        ConsultaCuentas consultaCuentas = new ConsultaCuentas(context);
        FCConsultaCuentasRequest request = new FCConsultaCuentasRequest();
        request.setNumCliente(numCliente);
        request.setTokenJwt(token);

        Logger.d(new Gson().toJson(request));

        consultaCuentas.consultaCuentas(request, new ConsultaCuentas.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCConsultaCuentasResponse response = (FCConsultaCuentasResponse)Object;
                Logger.d(new Gson().toJson(response));

                if("0".equals(response.getCodigo())) {
                    function.execute(response);
                }

                context.loading(false);

            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void registraCliente(String celular, String email, String token, IFunction function) {

        RegistraCliente registraCliente = new RegistraCliente(context);

        FCRegistraClienteRequest request = new FCRegistraClienteRequest();
        request.setCelular(celular);
        request.setEmail(email);
        request.setTokenJwt(token);

        registraCliente.postRegistraCliente(request, new RegistraCliente.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRegistraClienteResponse response = (FCRegistraClienteResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                //context.alert(TAG, mensaje);
                function.execute();
            }
        });

    }

    @Override
    public void devoluciones(String inicio, String fin, String token, IFunction function) {

        Devoluciones devoluciones = new Devoluciones(context);

        FCMovsDevolucionRequest request = new FCMovsDevolucionRequest(numCliente, imei, inicio,fin, token);
        devoluciones.getlistaDevoluciones(request, new Devoluciones.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                FCMovsDevolucionResponse response = (FCMovsDevolucionResponse) e;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void consultaMensajeCobro(DHDatosTransferencia trans, IFunction function) {

        int dv = Integer.parseInt(trans.getDigitoVerBen());
        String alias = trans.getNumCelularBen();
        String id = trans.getFolioCodi();
        int registros = 10;
        int pagina = 1;
        CoDi coDi = new CoDi(context);

        ActorOperacion actor = ActorOperacion.ORDENANTE;

        if(nomCliente.equals(trans.getNombreDelBeneficiario())) {
            dv = Integer.parseInt(trans.getDigitoVerOrd());
            alias = trans.getNumCelularOrd();
            actor = ActorOperacion.BENEFICIARIO;
            id = trans .getFolioCodi().substring(0,10);
        }

        ActorOperacion finalActor = actor;
        String finalId = id;

        Logger.d(id + " - " + dv + " - " + alias + " - " + registros + " - " + pagina  + " - " + actor.name());

        coDi.consultaMensajeCobro(id, dv,alias ,registros, pagina, actor , new CoDi.onProcess() {
            @Override
            public <E> void onSuccess(E Object) {
                BanxicoConsultaMCResponse response = (BanxicoConsultaMCResponse)Object;
                Logger.d(new Gson().toJson(response) );

                BanxicoDesifradoMC info = coDi.descifrarConsultaMensajeCobro(response.getInfo().getListaMC(), finalId);

                function.execute(info, finalActor);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void avisoPrivacidad(String token, IFunction function) {

        AvisoPrivacidad avisoPrivacidad = new AvisoPrivacidad(context);

        FCAvisoPrivacidadRequest request = new FCAvisoPrivacidadRequest();
        request.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
        request.setTelefono(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        request.setTokenJwt(token);

        avisoPrivacidad.postAvisoPrivacidad(request, new AvisoPrivacidad.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                Logger.d(new Gson().toJson(e) );
                function.execute();
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void nuevaSolicitud(IFunction function) {

        NuevaSolicitud nuevaSolicitud = new NuevaSolicitud(context);
        nuevaSolicitud.getNuevaSolicitud(new NuevaSolicitud.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCNuevaSolicitudResponse response = (FCNuevaSolicitudResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void validacionCURP(String folio, String curp, IFunction function) {

        ValidacionCURP validacionCURP = new ValidacionCURP(context);
        FCValidacionCURPRequest request = new FCValidacionCURPRequest();
        request.setCurp(curp);
        request.setFolioDeSolicitud(folio);
        request.setUsuario(usuario);

        Logger.d(new Gson().toJson(request) );

        validacionCURP.postValidacionCURP( request, new ValidacionCURP.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCValidacionCURPResponse response = (FCValidacionCURPResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void validaCredencial(FCValidaCredencialRequest request, IFunction function) {

        ValidaCredencial validaCredencial = new ValidaCredencial(context);

        validaCredencial.postValidaCredencial(request, new ValidaCredencial.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCValidaCredencialResponse response = (FCValidaCredencialResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });


    }

    @Override
    public void pld(FCPLDRequest request, IFunction function) {

        PLD pld = new PLD(context);
        
        pld.postConsultaPLD(request, new PLD.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCPLDResponse response = (FCPLDResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });
    }

    @Override
    public void cargaDocumento(String archivo, CargaDocumento.TipoCargaDocumento tipo, FCGuardarDocumentoRequest request, IFunction function) {

        CargaDocumento cargaDocumento = new CargaDocumento(context);

        Logger.d(new Gson().toJson(request));

        Logger.d(archivo);

        cargaDocumento.postCargaDocumento(archivo, tipo, request, new CargaDocumento.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCGuardarDocumentoResponse response = (FCGuardarDocumentoResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void envioToken(FCEnvioTokenRequest request, IFunction function) {

        TokenValidacion tokenValidacion = new TokenValidacion(context);

        tokenValidacion.envioToken(request, new TokenValidacion.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCEnvioTokenResponse response = (FCEnvioTokenResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void validaToken(FCValidaTokenRequest request, IFunction function) {

        TokenValidacion tokenValidacion = new TokenValidacion(context);

        tokenValidacion.validaToken(request, new TokenValidacion.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCValidaTokenResponse response = (FCValidaTokenResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void aperturaCuenta(FCAperturaCuentaRequest request, IFunction function) {

        AperturaCuenta aperturaCuenta = new AperturaCuenta(context);
        request.setImei(imei);

        Logger.d(new Gson().toJson(request) );

        aperturaCuenta.postAperturaCuenta(request, new AperturaCuenta.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCAperturaCuentaResponse response = (FCAperturaCuentaResponse)Object;
                Logger.d(new Gson().toJson(response) );
                function.execute(response);
            }
            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }

    @Override
    public void consultaMovimientos(String token, String numCuenta,IFunction function) {
        UltimosMovimientos ultimosMovimientos = new UltimosMovimientos(context);
        FCUltimosMovimientosRequest request = new FCUltimosMovimientosRequest();
        request.setImei(imei);
        request.setNumCliente(numCliente);
        request.setTokenJwt(token);
        request.setNumCuenta(numCuenta);
        ultimosMovimientos.getUltimosMovimientos(request, new UltimosMovimientos.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                Logger.d(new Gson().toJson(e) );
                function.execute(e);
            }

            @Override
            public void onFailure(String mensaje) {
                Logger.d(mensaje);
                context.loading(false);
                context.alert(TAG, mensaje);
            }
        });

    }
}


