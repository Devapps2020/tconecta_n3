package com.blm.qiubopay.modules.fincomun.originacion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.fincomun.FCRequestDTO;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCOfertaSeleccionadaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosBancariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSMSRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCValidaSMSRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCOfertaSeleccionadaCreditoResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosBancariosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSMSResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCValidaSMSResponse;
import mx.com.fincomun.origilib.Model.Originacion.Credito.OfertaSeleccionadaCredito;
import mx.com.fincomun.origilib.Model.Originacion.DatosBancarios;
import mx.com.fincomun.origilib.Model.Originacion.SMS;
import mx.com.fincomun.origilib.Objects.Credito.DHOferta;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.components.otp.PinView;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_codigo extends HFragment implements IMenuContext {

    public static Fragment_fincomun_oferta.Type type = Fragment_fincomun_oferta.Type.OFFER;
    public static String firma = "";
    private View view;
    private HActivity context;
    private Object data;
    private Button btn_next;
    private PinView edit_pin;
    private TextView tv_sms,tv_call;
    private Boolean isLoading  =false;

    public static Fragment_fincomun_codigo newInstance() {
        Fragment_fincomun_codigo fragment = new Fragment_fincomun_codigo();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_codigo"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codigo, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_white)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        edit_pin = getView().findViewById(R.id.edit_pin);
        edit_pin.setChanged(new PinView.ITextChanged() {
            @Override
            public void onChange(int length) {
                if(edit_pin.isValid())
                    btn_next.setEnabled(true);
                else
                    btn_next.setEnabled(false);
            }
        });

        tv_sms = getView().findViewById(R.id.tv_sms);
        tv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLoading) {
                    isLoading = true;

                    getContextMenu().getTokenFC((String... text) -> {
                        if (text != null) {
                            sendSMS(text[0], true, true);
                        }else{
                            isLoading = false;
                        }
                    });
                }
            }
        });
        tv_call = getView().findViewById(R.id.tv_call);
        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;

                    getContextMenu().getTokenFC((String... text) -> {
                        if (text!= null) {
                            sendSMS(text[0], false, false);
                        }else {
                            isLoading = false;
                        }
                    });
                }
            }
        });
        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    getContextMenu().getTokenFC((String... text) -> {
                        if (text != null) {
                            validateSMS(text[0], edit_pin.getText().toString());
                        }else {
                            isLoading = false;
                        }
                    });
                }
            }
        });


        getContextMenu().getTokenFC((String... text) -> {
            sendSMS(text[0],true,false);
        });

    }
    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public void sendSMS(String token,boolean isSms, boolean reenvio){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCSMSRequest fcsmsRequest = new FCSMSRequest();
        fcsmsRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcsmsRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcsmsRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcsmsRequest.setTipo_aplicacion(2);

        fcsmsRequest.setReintento(reenvio?2:1);
        fcsmsRequest.setEnvioVoz(isSms?-1:1);
        SessionApp.getInstance().getFcRequestDTO().setSms(fcsmsRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcsmsRequest));

        SMS sms = new SMS(getContext());
        sms.envioSMS(fcsmsRequest, new SMS.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                try {

                    FCSMSResponse response = (FCSMSResponse) e;

                    Logger.d("RESPONSE : "  + new Gson().toJson(response));

                    if(response.getRespuesta().getCodigo() < 0) {
                        getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    } else {
                        SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.QUINCE);
                        SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                        SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                     //   AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                       // initAlert();

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                isLoading = false;
                getContext().loading(false);

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;

            }
        });
    }

    public void validateSMS(String token, String code) {

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCValidaSMSRequest fcValidaSMSRequest = new FCValidaSMSRequest();
        fcValidaSMSRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcValidaSMSRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcValidaSMSRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        fcValidaSMSRequest.setToken(code);
        fcValidaSMSRequest.setTipo_aplicacion(2);

        Logger.d("REQUEST : "  + new Gson().toJson(fcValidaSMSRequest));

        getContext().loading(true);

        SMS sms = new SMS(getContext());
        sms.validaSMS(fcValidaSMSRequest, new SMS.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCValidaSMSResponse response = (FCValidaSMSResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    getContext().loading(false);

                } else {

                    SessionApp.getInstance().getFcRequestDTO().setValidSMS(1);
                   // AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());


                    switch (type){
                        case RENOVACION:
                        case RECOMPRA:
                            selctOferta(token);
                            break;
                        case OFFER:
                            getContext().setFragment(Fragment_fincomun_contratacion_4.newInstance());
                            break;
                    }

                }
                isLoading = false;

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;

            }
        });
    }

    public void selctOferta(String token) {

        getContext().loading(true);
        DHOferta oferta = new DHOferta();
        oferta.setPlazo(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getNumPagos()));
        oferta.setOferta(Double.valueOf(SessionApp.getInstance().getFcRequestDTO().getSimulador().getMontoPrestamo() + ""));
        oferta.setFrecuencia(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getFrecuenciaPago()));
        oferta.setCuota(SessionApp.getInstance().getFcRequestDTO().getSimulador().getCuota());
        oferta.setTasa(SessionApp.getInstance().getFcRequestDTO().getTasa());


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
                    isLoading = false;
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.DIECISEIS);
                    //  AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                    datosBancarios(token);
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });

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
                    //  AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    QPAY_Register register = new QPAY_Register();
                    register.setFolio("-");
                    register.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                    register.setRegisterType(Globals.REGISTER_TYPE_FC_CREDITO);

                    RegisterActivity.createRegister(getContext(), register, new IFunction<QPAY_BaseResponse>() {
                        @Override
                        public void execute(QPAY_BaseResponse... data) {

                            getContextMenu().backFragment();
                            getContextMenu().showAlertLayout(R.layout.item_alert_success_credit_request,"Folio de solicitud: "+SessionApp.getInstance().getFcRequestDTO().getFolio(), new IClickView() {
                                @Override
                                public void onClick(java.lang.Object... data) {
                                    SessionApp.getInstance().setFcRequestDTO(new FCRequestDTO());

                                    getContextMenu().initHome();
                                }
                            });
                        }
                    });

                }
                isLoading = false;

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
                isLoading = false;
            }
        });

    }
}