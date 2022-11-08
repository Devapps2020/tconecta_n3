package com.blm.qiubopay.modules.fincomun.originacion;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.FileUtils;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.fincomun.FCRequestDTO;
import com.blm.qiubopay.modules.Fragment_registro_financiero_5;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_2;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_5;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_7;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_solicitud_fincomun_6;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaDispositivoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCAltaUsuarioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCOfertaSeleccionadaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAdicionalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBancosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComentariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosBancariosRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaDispositivoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAltaUsuarioResponse;
import mx.com.fincomun.origilib.Http.Response.FCComentariosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCOfertaSeleccionadaCreditoResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCAdicionalesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBancosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosBancariosResponse;
import mx.com.fincomun.origilib.Model.Originacion.Adicionales;
import mx.com.fincomun.origilib.Model.Originacion.Bancos;
import mx.com.fincomun.origilib.Model.Originacion.Comentarios;
import mx.com.fincomun.origilib.Model.Originacion.Credito.OfertaSeleccionadaCredito;
import mx.com.fincomun.origilib.Model.Originacion.DatosBancarios;
import mx.com.fincomun.origilib.Objects.Adicionales.DHPregunta;
import mx.com.fincomun.origilib.Objects.Bancos.DHBancarios;
import mx.com.fincomun.origilib.Objects.Bancos.DHBancos;
import mx.com.fincomun.origilib.Objects.Catalogos.ActividadEconomica;
import mx.com.fincomun.origilib.Objects.Catalogos.ActividadGeneral;
import mx.com.fincomun.origilib.Objects.Catalogos.Genero;
import mx.com.fincomun.origilib.Objects.Catalogos.Localidad;
import mx.com.fincomun.origilib.Objects.Catalogos.LugarNacimiento;
import mx.com.fincomun.origilib.Objects.Catalogos.TipoCuenta;
import mx.com.fincomun.origilib.Objects.Credito.DHOferta;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

import static com.blm.qiubopay.utils.Utils.getImage;

public class Fragment_fincomun_contratacion_4 extends HFragment  implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;

    private CheckBox cb_contract_all, cb_contract_1,cb_contract_2,cb_contract_3,cb_contract_4,cb_contract_5;
    private CheckBox cb_confirm_personal_data, cb_confirm;
    private TextView tv_user;
    private TextView tv_offer_recompra,tv_view_detail_offer,tv_detail_personal_data,tv_step;
    private ImageView img_firm;
    private RadioGroup rg_who_use_resources, rg_destino;
    private LinearLayout ll_user,ll_recompra;
    Uri uri;
    private String uses,destino;
    private String firma = "";
    private DHOferta oferta;
    private ArrayList<ModelItem> bancos;
    public static Fragment_fincomun_oferta.Type type = Fragment_fincomun_oferta.Type.OFFER;
    private Boolean isLoading  =false;

    public static Fragment_fincomun_contratacion_4 newInstance() {
        Fragment_fincomun_contratacion_4 fragment = new Fragment_fincomun_contratacion_4();

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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_contratacion_4"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_contratacion_4, container, false),R.drawable.background_splash_header_1);
    }


    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .showTitle("Contratación de préstamo")
                .setColorTitle(R.color.FC_blue_6)
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        oferta = new DHOferta();
        oferta.setPlazo(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getNumPagos()));
        oferta.setOferta(Double.valueOf(SessionApp.getInstance().getFcRequestDTO().getSimulador().getMontoPrestamo() + ""));
        oferta.setFrecuencia(Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getFrecuenciaPago()));
        oferta.setCuota(SessionApp.getInstance().getFcRequestDTO().getSimulador().getCuota());
        oferta.setTasa(SessionApp.getInstance().getFcRequestDTO().getTasa());

        ll_recompra = getView().findViewById(R.id.ll_recompra);

        cb_confirm_personal_data = getView().findViewById(R.id.cb_confirm_personal_data);
        cb_confirm = getView().findViewById(R.id.cb_confirm);

        tv_step = getView().findViewById(R.id.tv_step);
        tv_offer_recompra = getView().findViewById(R.id.tv_offer_recompra);

        tv_view_detail_offer = getView().findViewById(R.id.tv_view_detail_offer);
        tv_view_detail_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fincomun_originacion_simulador_detalle.type = type;
                getContext().setFragment(Fragment_fincomun_originacion_simulador_detalle.newInstance());
            }
        });

        tv_detail_personal_data = getView().findViewById(R.id.tv_detail_personal_data);
        tv_detail_personal_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fincomun_contratacion_2.type = Fragment_fincomun_oferta.Type.RECOMPRA;
                getContext().setFragment(Fragment_fincomun_contratacion_2.newInstance());
            }
        });

        campos = new ArrayList();

        rg_who_use_resources = getView().findViewById(R.id.rg_who_use_resources);
        rg_who_use_resources.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_me:
                        uses = "Usted";
                        break;
                    case R.id.rb_other:
                        uses = "Alguien más";
                        break;
                }

            }
        });

        rg_destino = getView().findViewById(R.id.rg_destino);
        rg_destino.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_destino_1:
                        destino = "Bienes personales";
                        break;
                    case R.id.rb_destino_2:
                        destino = "Emergencia personal";
                        break;
                    case R.id.rb_destino_3:
                        destino = "Gastos de tu negocio";
                        break;
                    case R.id.rb_destino_4:
                        destino = "Inversión para tu negocio";
                        break;
                }
            }
        });

        ll_user = getView().findViewById(R.id.ll_user);

        //0/password
        campos.add(FCEditText.create(getView().findViewById(R.id.et_password))
                .setRequired(false)
                .setMinimum(8)
                .setMaximum(8)
                .setType(FCEditText.TYPE.TEXT_PASS)
                .setHint("Contraseña")
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setAlert(R.string.text_input_required));

        if (SessionApp.getInstance().getFcBanderas() != null
                && SessionApp.getInstance().getFcBanderas().getBanderaUsuario().equalsIgnoreCase("1")){
            ll_user.setVisibility(View.GONE);
        }else{
            ll_user.setVisibility(View.VISIBLE);
            campos.get(0).setRequired(false);
        }

        cb_contract_all = getView().findViewById(R.id.cb_contract_all);
        cb_contract_all.setOnCheckedChangeListener(listenerChange);
        cb_contract_1 = getView().findViewById(R.id.cb_contract_1);
        cb_contract_1.setOnCheckedChangeListener(listenerChange);
        cb_contract_2 = getView().findViewById(R.id.cb_contract_2);
        cb_contract_2.setOnCheckedChangeListener(listenerChange);
        cb_contract_3 = getView().findViewById(R.id.cb_contract_3);
        cb_contract_3.setOnCheckedChangeListener(listenerChange);
        cb_contract_4 = getView().findViewById(R.id.cb_contract_4);
        cb_contract_4.setOnCheckedChangeListener(listenerChange);
        cb_contract_5 = getView().findViewById(R.id.cb_contract_5);
        cb_contract_5.setOnCheckedChangeListener(listenerChange);

        img_firm = getView().findViewById(R.id.img_firm);
        img_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Utils.getFilePhoto(getContext());
                getContext().setFragment(Fragment_registro_financiero_5.newInstance());
            }
        });

        getContextMenu().setImage = data -> {
            img_firm.setImageBitmap(data[0]);
            firma =  Utils.convert(data[0]);
            savePhoto(uri, data[0]);
            getContextMenu().backFragment();
        };

        tv_user = getView().findViewById(R.id.tv_user);
        tv_user.setText("Usuario: "+ AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());
        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    validate();
                }
            }
        });

        configView();
        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_CONFIRMACION_SOLICITUD);
    }

    private void configView() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        switch (type){
            case RECOMPRA:

                Double montoOferta = Double.parseDouble(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());
                String monto= formatter
                        .format(montoOferta);
                Double saldoInsoluto = Double.parseDouble(SessionApp.getInstance().getDhListaCreditos().getSaldoInsoluto());
                Double inCash = montoOferta-saldoInsoluto;
                tv_offer_recompra.setText("Confirmo mi nuevo préstamo de $"+monto+" recibiendo en efectivo $"+formatter.format(inCash));
                ll_recompra.setVisibility(View.VISIBLE);
                tv_step.setVisibility(View.GONE);
                ll_user.setVisibility(View.GONE);
                break;
            case RENOVACION:

                Double montoOfertaR = SessionApp.getInstance().getFcRequestDTO().getSimulador().getMontoPrestamo().doubleValue();
                String montoR= formatter
                        .format(montoOfertaR);
                tv_offer_recompra.setText("Confirmo mi nuevo préstamo de $"+montoR);
                ll_recompra.setVisibility(View.VISIBLE);
                tv_step.setVisibility(View.GONE);
                ll_user.setVisibility(View.GONE);
                break;
            case OFFER:
            case ADELANTO_VENTAS:
                break;
        }
    }

    private void getBancos(String token,IFunction function) {
        getContext().loading(true);

        Bancos bancos = new Bancos(getContext());
        FCBancosRequest request = new FCBancosRequest();
        request.setTokenJwt(token);

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        bancos.postBancos(request, new Bancos.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);
                FCBancosResponse response = (FCBancosResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response != null && response.getCodigo()==0){
                    SessionApp.getInstance().setBancos(response.getBancos());
                    function.execute();
                }else{
                    getContext().alert(response.getDescripcion());
                }

            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert("Error al cargar los bancos");

            }
        });

    }

    private void fillData() {
        bancos = new ArrayList();

        for(DHBancos banco : SessionApp.getInstance().getBancos()) {
            bancos.add(new ModelItem(banco.getDescriBanco(), banco.getClaveBanco()));
        }

        getContext().loading(false);

    }

    private void validate() {

        if(TextUtils.isEmpty(uses)){
            isLoading = false;
            getContextMenu().alert("Responde la pregunta ¿Quien usará los recursos de la cuenta?");
        }else if (TextUtils.isEmpty(destino)){
            isLoading = false;
            getContextMenu().alert("Responde la pregunta ¿Cúal será el destino del crédito?");

        }else if (!cb_contract_all.isChecked()){
            isLoading = false;
            getContextMenu().alert("Acepta los contratos");
        }else if (TextUtils.isEmpty(firma)){
            isLoading = false;
            getContextMenu().alert("Realiza tu firma");
        }else if (ll_user.getVisibility() == View.VISIBLE){
             if (TextUtils.isEmpty(campos.get(0).getText()) || !campos.get(0).getText().matches("^(?=\\w*\\d)(?=\\w*[A-Z])\\S{8}$") ){
                 campos.get(0).setRequired(true);
                 isLoading = false;
                 getContextMenu().alert("Ingresa una contraseña valida");
            }else {
                 altaUsuario();
             }
        }else if(ll_recompra.getVisibility() == View.VISIBLE && !cb_confirm.isChecked()){
            isLoading = false;
            getContextMenu().alert("Acepta los detalles del préstamo");

        }else if(ll_recompra.getVisibility() == View.VISIBLE && !cb_confirm_personal_data.isChecked()){
            isLoading = false;
            getContextMenu().alert("Acepta los datos personales");

        }else {
            getContextMenu().getTokenFC((String... text) -> {
                if (text != null) {
                    postAdicionales(text[0]);
                }else{
                    isLoading  = false;
                }
            });
        }
    }

    CompoundButton.OnCheckedChangeListener listenerChange =new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.cb_contract_all:
                    cb_contract_1.setChecked(isChecked);
                    cb_contract_2.setChecked(isChecked);
                    cb_contract_3.setChecked(isChecked);
                    cb_contract_4.setChecked(isChecked);
                    cb_contract_5.setChecked(isChecked);
                    break;
                default:
                    cb_contract_all.setOnCheckedChangeListener(null);
                    cb_contract_all.setChecked(validateAllChecked());
                    cb_contract_all.setOnCheckedChangeListener(listenerChange);
                    break;
            }
        }
    };

    private boolean validateAllChecked() {
        return cb_contract_1.isChecked()
                && cb_contract_2.isChecked()
                && cb_contract_3.isChecked()
                && cb_contract_4.isChecked()
                && cb_contract_5.isChecked();
    }

    public void savePhoto(Uri uri, Bitmap image) {

        try {

            String path = FileUtils.getPath(getContext(), uri).split(":")[0];
            Logger.d(path);

            img_firm.setImageBitmap(image);


            File file = new File(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public void altaUsuario() {

        getContext().loading(true);

        FCAltaUsuarioRequest request = new FCAltaUsuarioRequest();
        request.setCelular(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        request.setContrasena(campos.get(0).getText());
        request.setEmail(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
        request.setNumCliente(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        request.setUsuario(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());

        Logger.d(new Gson().toJson(request));

        getContext().loading(true);
        getContextMenu().gethCoDi().altaUsuario(request, new IFunction() {
            @Override
            public void execute(Object[] data) {
                if (data!= null) {

                    FCAltaUsuarioResponse response = (FCAltaUsuarioResponse) data[0];
                    Logger.d(new Gson().toJson(response));

                    if (response.codigo != 0) {
                        getContext().alert(response.getDescripcion());
                        getContext().loading(false);
                        isLoading = false;
                        return;
                    }

                    getContextMenu().getTokenFC(new IFunction<String>() {
                        @Override
                        public void execute(String... data) {
                            if (data != null) {
                                altaDispositivo(data[0]);
                            } else {
                                isLoading = false;
                            }
                        }
                    });
                }else {
                    isLoading = false;
                }
            }
        });

    }

    public void altaDispositivo(String token) {

        FCAltaDispositivoRequest request = new FCAltaDispositivoRequest(
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone(),
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone(),
                token);

        Logger.d(new Gson().toJson(request));

        getContextMenu().gethCoDi().altaDispositivo(request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                if (data!=null) {
                    FCAltaDispositivoResponse response = (FCAltaDispositivoResponse) data[0];
                    Logger.d(new Gson().toJson(response));

                    if (response.codigo != 0) {
                        getContext().alert(response.getDescripcion());
                        getContext().loading(false);
                        isLoading = false;
                        return;
                    }

                    getContextMenu().gethCoDi().actualizaBandera(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id(), token, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            getContext().loading(false);

                            getContextMenu().getTokenFC((String... text) -> {
                                if (text != null) {
                                    postAdicionales(text[0]);
                                } else {
                                    isLoading = false;
                                }
                            });
                        }
                    });
                }else{
                    isLoading = false;
                }
            }
        });
    }


    public void postAdicionales(String token){

        FCAdicionalesRequest fcAdicionalesRequest = new FCAdicionalesRequest();

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        fcAdicionalesRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcAdicionalesRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcAdicionalesRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        ArrayList<DHPregunta> preguntas =new ArrayList();

        double cuota = SessionApp.getInstance().getFcRequestDTO().getSimulador().getCuota();
        double monto = SessionApp.getInstance().getFcRequestDTO().getSimulador().getMontoPrestamo().doubleValue();
        String respuesta1="",respuesta2="",respuesta3="",respuesta4="",respuesta5="";
        if(cuota <= 5000)
            respuesta1 = Utils.paserCurrency("5000");

        if(cuota > 5000 && cuota <= 10000)
            respuesta1 = Utils.paserCurrency("10000");

        if(cuota > 10000 && cuota <= 20000)
            respuesta1 = Utils.paserCurrency("20000");

        switch (Integer.parseInt(SessionApp.getInstance().getFcRequestDTO().getSimulador().getFrecuenciaPago())) {
            case 7:
            case 14:
            case 15:
            case 30:
                respuesta2 = "5";
                break;
        }

        if(monto <= 5000)
            respuesta3 = Utils.paserCurrency("5000");

        if(monto > 5000 && cuota <= 10000)
            respuesta3 = Utils.paserCurrency("10000");

        if(monto > 10000 && cuota <= 20000)
            respuesta3 = Utils.paserCurrency("20000");



        respuesta4 = "5";
        respuesta5= "TIENDA DE ABARROTES Y MISCELANEA";

        //Método -> Simulador -> Campo para calcular -> cuota + frecuenciaPago+numPagos
        preguntas.add(new DHPregunta("¿Cuál es el monto de los depósitos que realizará al mes?", respuesta1));

        //Método -> Simulador -> Campo para calcular -> frecuenciaPago+numPagos
        preguntas.add(new DHPregunta("¿Cuántos depósitos realizará en el mes?", respuesta2));

        //Método -> Simulador -> Campo para calcular -> montoPrestamo
        preguntas.add(new DHPregunta("¿Cuál es el monto de los retiros que realizará al mes?", respuesta3));

        //
        preguntas.add(new DHPregunta("¿Cuántos retiros realizará en el mes?", respuesta4));

        preguntas.add(new DHPregunta("¿Cuál es el origen de los recursos para el pago del crédito?", respuesta5));


        preguntas.add(new DHPregunta("¿Cuál será el destino del crédito?", destino));

        preguntas.add(new DHPregunta("¿Los recursos de la cuenta serán utilizados?", uses));

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
                    isLoading = false;
                } else {
                    switch (type){
                        case RENOVACION:
                        case RECOMPRA:
                            sendToken();
                            break;
                        case OFFER:
                        case ADELANTO_VENTAS:
                            selctOferta(token, oferta);
                            break;
                    }
                }

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
                isLoading = false;
            }
        });

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
                    isLoading = false;
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.DIECISEIS);
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
        if (SessionApp.getInstance().isRequiredEdoCuenta()){
            DHBancarios bancarios = new DHBancarios();
            if(SessionApp.getInstance().getFcRequestDTO() != null
                    && SessionApp.getInstance().getFcRequestDTO().getIdentificacion() != null){
                bancarios.setNombre_beneficiario(
                        SessionApp.getInstance().getFcRequestDTO().getIdentificacion().getNombre()
                                + " "+SessionApp.getInstance().getFcRequestDTO().getIdentificacion().getApellidoPaterno()
                                + " "+SessionApp.getInstance().getFcRequestDTO().getIdentificacion().getApellidoMaterno());
            }else{
                bancarios.setNombre_beneficiario("");
            }

            bancarios.setId_tipo_cuenta("40");
            bancarios.setClabe_interbancaria(SessionApp.getInstance().getClabeEdoCuenta());
            bancarios.setNumero_cuenta("");
            bancarios.setBanco(SessionApp.getInstance().getIdBancoEdoCuenta());

            List<DHBancarios> data = new ArrayList<>();
            data.add(bancarios);

            fcDatosBancariosRequest.setBancarios(data);
            fcDatosBancariosRequest.setTipo_consulta("B");

        }

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
                    isLoading = false;
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());

                    getContextMenu().getTokenFC((String... data) -> {
                        if (data!= null){
                            comentarios(data[0]);
                        }else {
                            isLoading = false;
                        }

                    });

                }

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
                isLoading = false;
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

                    getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_EXITO_SOLICITUD);

                    getContextMenu().showAlertLayout(R.layout.item_alert_success_credit_request,"Folio de solicitud: "+SessionApp.getInstance().getFcRequestDTO().getFolio(), new IClickView() {
                        @Override
                        public void onClick(java.lang.Object... data) {
                            SessionApp.getInstance().setFcRequestDTO(new FCRequestDTO());

                            getContextMenu().initHome();
                        }
                    });

                }
                isLoading = false;

            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                Logger.d("RESPONSE : "  + mensaje);
                getContext().alert(mensaje);
                isLoading = false;

            }
        });

    }


    private void sendToken() {
        isLoading = false;
        Fragment_fincomun_codigo.type = Fragment_fincomun_oferta.Type.RECOMPRA;
        Fragment_fincomun_codigo.firma = firma;
        getContextMenu().setFragment(Fragment_fincomun_codigo.newInstance());
    }

}