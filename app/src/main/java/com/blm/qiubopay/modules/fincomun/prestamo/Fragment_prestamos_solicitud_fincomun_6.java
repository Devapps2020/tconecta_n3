package com.blm.qiubopay.modules.fincomun.prestamo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.modules.Fragment_registro_financiero_5;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCOfertaSeleccionadaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAdicionalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComentariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCConsultaBuroRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosBancariosRequest;
import mx.com.fincomun.origilib.Http.Response.FCComentariosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCOfertaSeleccionadaCreditoResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCAdicionalesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCConsultaBuroResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosBancariosResponse;
import mx.com.fincomun.origilib.Model.Originacion.Adicionales;
import mx.com.fincomun.origilib.Model.Originacion.BuroCredito;
import mx.com.fincomun.origilib.Model.Originacion.Comentarios;
import mx.com.fincomun.origilib.Model.Originacion.Credito.OfertaSeleccionadaCredito;
import mx.com.fincomun.origilib.Model.Originacion.DatosBancarios;
import mx.com.fincomun.origilib.Objects.Adicionales.DHPregunta;
import mx.com.fincomun.origilib.Objects.Credito.DHOferta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_prestamos_solicitud_fincomun_6 extends HFragment implements IMenuContext {

    public static String[] respuestas = new String[] {"", "", "", "", ""};


    private ArrayList<HEditText> campos;

    private CheckBox check_documentos, check_auth, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8;
    private Button btn_accept_offer;
    private String firma = "";
    private DHOferta oferta;

    private RadioButton rad_si_3;
    private RadioButton rad_no_3;

    private RadioButton rad_bienes;
    private RadioButton rad_emergencia;
    private RadioButton rad_gastos;
    private RadioButton rad_inversion;


    private Boolean check = false;

    public static Fragment_prestamos_solicitud_fincomun_6 newInstance(Object... data) {
        Fragment_prestamos_solicitud_fincomun_6 fragment = new Fragment_prestamos_solicitud_fincomun_6();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_prestamos_solicitud_fincomun_15_16, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_FINCOMUN_Confirma_credito);

        oferta = new DHOferta();
        oferta.setPlazo(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getNumPagos()));
        oferta.setOferta(Double.valueOf(SessionApp.getInstance().getFcRequestDTO().getSimulador().getMontoPrestamo() + ""));
        oferta.setFrecuencia(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getFrecuenciaPago()));
        oferta.setCuota(SessionApp.getInstance().getFcRequestDTO().getSimulador().getCuota());
        oferta.setTasa(SessionApp.getInstance().getFcRequestDTO().getTasa());

        Logger.d(new Gson().toJson(oferta));

        btn_accept_offer = getView().findViewById(R.id.btn_accept_offer);
        btn_accept_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContextMenu().getTokenFC((String... text) -> {
                    postAdicionales(text[0]);
                });

                if(true)
                    return;

                getContext().alert(R.string.title_buro,R.string.solicit_autorization , new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {

                        if(rad_no_3.isChecked()) {

                            getContext().alert("Por el momento no podemos llevar a cabo tu solicitud", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    getContextMenu().initHome();
                                }
                            });
                            return;
                        }

                        getContextMenu().getTokenFC((String... text) -> {
                            postAdicionales(text[0]);
                        });

                    }
                });
            }
        });

        TextView text_cat = getView().findViewById(R.id.text_cat);
        TextView text_fecha = getView().findViewById(R.id.text_fecha);
        TextView text_tasa = getView().findViewById(R.id.text_tasa);

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        text_cat.setText("- CAT " + getCat() + "% sin IVA informativo.");
        text_fecha.setText("- Fecha de cálculo: " + simpleDateFormat.format(new Date()));
        text_tasa.setText("- Tasa de interés fija anual: " + 50 + "% Sin IVA");

        TextView text_monto = getView().findViewById(R.id.text_monto);
        TextView text_plazo = getView().findViewById(R.id.text_plazo);
        TextView text_pagos_de = getView().findViewById(R.id.text_pagos_de);
        TextView text_cada = getView().findViewById(R.id.text_cada);

        text_monto.setText(Utils.paserCurrency(oferta.getOferta() + ""));
        text_pagos_de.setText(Utils.paserCurrency(Math.ceil(oferta.getCuota()) + ""));

        text_plazo.setText(oferta.getPlazo() + "");
        text_cada.setText(oferta.getFrecuencia() + " días");

        String name = "";

        switch (oferta.getFrecuencia()) {
            case 7:
                name = " semanas";
                break;
            case 14:
                name = " catorcenas";
                break;
            case 15:
                name = " quincenas";
                break;
            case 30:
                name = " meses";
                break;
        }

        text_plazo.setText(oferta.getPlazo() + " " + name);

        campos = new ArrayList();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };

        check_auth = getView().findViewById(R.id.check_auth);
        check_documentos = getView().findViewById(R.id.check_documentos);
        checkBox1 = getView().findViewById(R.id.checkBox1);
        checkBox2 = getView().findViewById(R.id.checkBox2);
        checkBox3 = getView().findViewById(R.id.checkBox3);
        checkBox4 = getView().findViewById(R.id.checkBox4);
        checkBox5 = getView().findViewById(R.id.checkBox5);
        checkBox6 = getView().findViewById(R.id.checkBox6);
        checkBox7 = getView().findViewById(R.id.checkBox7);
        checkBox8 = getView().findViewById(R.id.checkBox8);

        rad_si_3 = getView().findViewById(R.id.rad_si_3);
        rad_no_3 = getView().findViewById(R.id.rad_no_3);

        rad_bienes = getView().findViewById(R.id.rad_bienes);
        rad_gastos = getView().findViewById(R.id.rad_gastos);
        rad_emergencia = getView().findViewById(R.id.rad_emergencia);
        rad_inversion = getView().findViewById(R.id.rad_inversion);

        rad_si_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_no_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_bienes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_gastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_emergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_inversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        check_documentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check = !check;

                checkBox1.setChecked(check);
                checkBox2.setChecked(check);
                checkBox3.setChecked(check);
                checkBox4.setChecked(check);
                checkBox5.setChecked(check);
                checkBox6.setChecked(check);
                checkBox7.setChecked(check);
                checkBox8.setChecked(check);
                check_auth.setChecked(check);

                validate();
            }
        });

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        ImageView btn_firmar = getView().findViewById(R.id.img_firm);
        btn_firmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_registro_financiero_5.newInstance());
            }
        });
        //VERIFICAR
        getContextMenu().setImage = dataa -> {
            btn_firmar.setImageBitmap(dataa[0]);
            firma =  Utils.convert(dataa[0]);
            getContextMenu().backFragment();
            validate();
        };

        validate();

        LinearLayout layout_mi_medico = getView().findViewById(R.id.layout_mi_medico);
        if(SessionApp.getInstance().getFcRequestDTO().isMedico())
            layout_mi_medico.setVisibility(View.VISIBLE);
        else
            checkBox8.setChecked(true);

    }

    public Double getCat() {
        return 64.5;
    }

    public void selctOferta(String token, DHOferta oferta) {

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCOfertaSeleccionadaCreditoRequest fcOfertaSeleccionadaCreditoRequest = new FCOfertaSeleccionadaCreditoRequest();

        fcOfertaSeleccionadaCreditoRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcOfertaSeleccionadaCreditoRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcOfertaSeleccionadaCreditoRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcOfertaSeleccionadaCreditoRequest.tieneSeguro = (SessionApp.getInstance().getFcRequestDTO().isMedico() ? 1 : 0);
        fcOfertaSeleccionadaCreditoRequest.setOferta(oferta);

        Logger.d("REQUEST : "  + new Gson().toJson(fcOfertaSeleccionadaCreditoRequest));

        OfertaSeleccionadaCredito ofertaSeleccionadaCredito = new OfertaSeleccionadaCredito(getContext());
        ofertaSeleccionadaCredito.postOfertaSeleccionadaCredito(fcOfertaSeleccionadaCreditoRequest, new OfertaSeleccionadaCredito.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCOfertaSeleccionadaCreditoResponse response = (FCOfertaSeleccionadaCreditoResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.DIECISEIS);
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                    datosBancarios(token);
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });

    }

    public void validate(){

        btn_accept_offer.setEnabled(false);

        if(!checkBox1.isChecked() || !checkBox2.isChecked() || !checkBox3.isChecked() || !checkBox4.isChecked()
                || !checkBox5.isChecked() || !checkBox6.isChecked() || !checkBox7.isChecked() || firma.isEmpty())
            return;

        if(SessionApp.getInstance().getFcRequestDTO().isMedico())
            if(!checkBox8.isChecked())
                return;

        if(!rad_si_3.isChecked() && !rad_no_3.isChecked())
            return;

        if(!rad_bienes.isChecked() && !rad_emergencia.isChecked() && !rad_inversion.isChecked() && !rad_gastos.isChecked())
            return;

        btn_accept_offer.setEnabled(true);

    }

    public void datosBancarios(String token) {

        FCDatosBancariosRequest fcDatosBancariosRequest = new FCDatosBancariosRequest();
        fcDatosBancariosRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcDatosBancariosRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcDatosBancariosRequest.setFirma(firma);
        fcDatosBancariosRequest.setTokenJwt(token);
        Logger.d("RESPONSE : "  + new Gson().toJson(fcDatosBancariosRequest));


        DatosBancarios datosBancarios = new DatosBancarios(getContext());

        datosBancarios.postDatosBancarios(fcDatosBancariosRequest, new DatosBancarios.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCDatosBancariosResponse response = (FCDatosBancariosResponse)Object;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    getContext().loading(false);
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    getContextMenu().getTokenFC((String... data) -> {
                        comentarios(data[0]);
                    });

                }

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });

    }

    public void comentarios(String token) {

        String json = "{       " +
                "                \"folio\":  \"32574947\",           " +
                "                \"num_cliente\":\"43\",             " +
                "                \"personales\":\"EXITO\",            " +
                "                \"negocio\":\"EXITO\",              " +
                "                \"gastos_referencias\":\"EXITO\",     " +
                "                \"aval_garantia\":\"EXITO\",          " +
                "                \"documentos\":\"EXITO\",             " +
                "                \"oferta\":\"EXITO\",                 " +
                "                \"identificacion_frente\":\"1\",      " +
                "                \"identificacion_vuelta\":\"1\",      " +
                "                \"comprobante_domicilio\":\"1\",      " +
                "                \"estado_cuenta\":\"1\",              " +
                "                \"foto_negocio_1\":\"1\",             " +
                "                \"foto_negocio_2\":\"1\",             " +
                "                \"foto_negocio_3\":\"1\",             " +
                "                \"requiere_aval\":\"0\",              " +
                "                \"requiere_garantia\":\"0\",          " +
                "                \"tipo_aplicacion\":\"2\",            " +
                "                \"id_bimbo\":\"\",                   " +
                "                \"tipo_cr\":\"1\",                    " +
                "                \"tokenJwt\":\"\"                     " +
                "        }";

        FCComentariosRequest fcComentariosRequest = new Gson().fromJson( json, FCComentariosRequest.class);
        fcComentariosRequest.setTokenJwt(token);
        fcComentariosRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcComentariosRequest.setNum_cliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcComentariosRequest.setId_bimbo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        Logger.d("REQUEST : "  + new Gson().toJson(fcComentariosRequest));

        Comentarios comentarios = new Comentarios(getContext());
        comentarios.postComentarios(fcComentariosRequest, new Comentarios.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCComentariosResponse response = (FCComentariosResponse)Object;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                getContext().loading(false);

                if(response.getRespuesta().getCodigo() != 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {


                    QPAY_Register register = new QPAY_Register();
                    register.setFolio("-");
                    register.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                    register.setRegisterType(Globals.REGISTER_TYPE_FC_CREDITO);

                    RegisterActivity.createRegister(getContext(), register, new IFunction<QPAY_BaseResponse>() {
                        @Override
                        public void execute(QPAY_BaseResponse... data) {

                            getContextMenu().showAlertLayout(R.layout.item_alert_proceso_exitoso, new IClickView() {
                                @Override
                                public void onClick(java.lang.Object... data) {
                                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.NONE);
                                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                                    getContextMenu().initHome();
                                }
                            });

                        }
                    });

                }

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                Logger.d("RESPONSE : "  + mensaje);
                getContext().alert(mensaje);
            }
        });

    }

    public void postBuroCredito(String token) {

        FCConsultaBuroRequest fcConsultaBuroRequest = new FCConsultaBuroRequest();

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        fcConsultaBuroRequest.setImagen(firma);
        fcConsultaBuroRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcConsultaBuroRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcConsultaBuroRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        SessionApp.getInstance().getFcRequestDTO().setConsulta_buro(fcConsultaBuroRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcConsultaBuroRequest));

        BuroCredito buroCredito = new BuroCredito(getContext());
        buroCredito.postBuroCredito(fcConsultaBuroRequest, new BuroCredito.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                try {

                    FCConsultaBuroResponse response = (FCConsultaBuroResponse)e;

                    Logger.d("RESPONSE : "  + new Gson().toJson(response));

                    if(response.getRespuesta().getCodigo() < 0) {
                        getContext().loading(false);
                        getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    } else {
                        SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                        SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                        AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                        selctOferta(token, oferta);

                    }
                } catch (Exception ex) {
                    getContext().alert((String)e);
                }

            }
            @Override
            public void onFailure(String mensaje) { getContext().alert(mensaje); }
        });

    }

    public void postAdicionales(String token){

        FCAdicionalesRequest fcAdicionalesRequest = new FCAdicionalesRequest();

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        fcAdicionalesRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcAdicionalesRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcAdicionalesRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        ArrayList<DHPregunta> preguntas =new ArrayList();

        //Método -> Simulador -> Campo para calcular -> cuota + frecuenciaPago+numPagos
        preguntas.add(new DHPregunta("¿Cuál es el monto de los depósitos que realizará al mes?", respuestas[0]));

        //Método -> Simulador -> Campo para calcular -> frecuenciaPago+numPagos
        preguntas.add(new DHPregunta("¿Cuántos depósitos realizará en el mes?", respuestas[1]));

        //Método -> Simulador -> Campo para calcular -> montoPrestamo
        preguntas.add(new DHPregunta("¿Cuál es el monto de los retiros que realizará al mes?", respuestas[2]));

        //
        preguntas.add(new DHPregunta("¿Cuántos retiros realizará en el mes?", respuestas[3]));

        preguntas.add(new DHPregunta("¿Cuál es el origen de los recursos para el pago del crédito?", respuestas[4]));

        String destino = "";

        if(rad_bienes.isChecked())
            destino = "Bienes personales";

        if(rad_gastos.isChecked())
            destino = "Gastos de tu negocio";

        if(rad_inversion.isChecked())
            destino = "Inversión negocio";

        if(rad_emergencia.isChecked())
            destino = "Emergencia personal";

        preguntas.add(new DHPregunta("¿Cuál será el destino del crédito?", destino));

        preguntas.add(new DHPregunta("¿Los recursos de la cuenta serán utilizados?", rad_si_3.isChecked() ? "Por usted" : "Por alguien mas"));

        fcAdicionalesRequest.setPreguntas(preguntas);
        fcAdicionalesRequest.setFirma(firma);

        Logger.d("REQUEST : "  + new Gson().toJson(fcAdicionalesRequest));

        Adicionales adicionales = new Adicionales(getContext());
        adicionales.postAdicionales(fcAdicionalesRequest, new Adicionales.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                FCAdicionalesResponse response = (FCAdicionalesResponse) Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    getContext().loading(false);
                } else {
                    selctOferta(token, oferta);
                    //postBuroCredito(token);
                }

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
