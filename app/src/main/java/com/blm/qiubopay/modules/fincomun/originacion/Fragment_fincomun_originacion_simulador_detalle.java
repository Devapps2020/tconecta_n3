package com.blm.qiubopay.modules.fincomun.originacion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCCalculaCatRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCCalculaCatResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSimuladorResponse;
import mx.com.fincomun.origilib.Model.Originacion.Credito.CalculaCat;
import mx.com.fincomun.origilib.Model.Originacion.Simulador;
import mx.com.fincomun.origilib.Objects.DHOfertaBimbo;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;

public class Fragment_fincomun_originacion_simulador_detalle extends HFragment implements IMenuContext {

    public static Fragment_fincomun_oferta.Type type = Fragment_fincomun_oferta.Type.OFFER;
    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private Switch sw_code_promotor;
    private TextView tv_amount,tv_term_fee,tv_frequency_name,tv_total_amount,tv_tasa,tv_cat,tv_tasa_mensual;
    private LinearLayout ll_back;
    private TableRow tr_solicitas,tr_liquidas;
    private TextView tv_amount_offer, tv_amount_insoluto;
    private Boolean isLoading  =false;
    FCSimuladorRequest simuladorRequest= new FCSimuladorRequest();
    private String promotor = "";
    public static Fragment_fincomun_originacion_simulador_detalle newInstance() {
        Fragment_fincomun_originacion_simulador_detalle fragment = new Fragment_fincomun_originacion_simulador_detalle();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
        isLoading = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_originacion_simulador_detalle"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        isLoading = false;
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_originacion_simulador_detalle, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        campos.add(FCEditText.create(getView().findViewById(R.id.et_code_promotor))
                .setMinimum(1)
                .setMaximum(8)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Ingresa el código de promotor")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new FCEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        promotor = text;
                    }
                }));

        campos.get(0).setRequired(false);
        campos.get(0).setVisibility(View.GONE);

        tr_solicitas = getView().findViewById(R.id.tr_solicitas);
        tr_liquidas = getView().findViewById(R.id.tr_liquidas);
        tv_amount_offer = getView().findViewById(R.id.tv_amount_offer);
        tv_amount_insoluto = getView().findViewById(R.id.tv_amount_insoluto);

        tv_amount = getView().findViewById(R.id.tv_amount);
        tv_term_fee = getView().findViewById(R.id.tv_term_fee);
        tv_frequency_name = getView().findViewById(R.id.tv_frequency_name);
        tv_total_amount = getView().findViewById(R.id.tv_total_amount);

        tv_tasa = getView().findViewById(R.id.tv_tasa);
        tv_tasa_mensual = getView().findViewById(R.id.tv_tasa_mensual);
        tv_cat = getView().findViewById(R.id.tv_cat);
        sw_code_promotor = getView().findViewById(R.id.sw_code_promotor);
        sw_code_promotor.setChecked(false);
        sw_code_promotor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campos.get(0).setVisibility(View.VISIBLE);
                }else{
                    campos.get(0).setRequired(false);
                    campos.get(0).setVisibility(View.GONE);
                    campos.get(0).setText("");
                    promotor = "";
                }
            }
        });

        ll_back = getView().findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().backFragment();
            }
        });
        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading  = true;
                    btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_button_principal_disabled));
                    if (sw_code_promotor.isChecked()) {
                        if (TextUtils.isEmpty(campos.get(0).getText())) {
                            campos.get(0).setRequired(true);
                            getContext().alert("Completa los campos obligatorios");
                            isLoading  = false;
                            btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));

                        } else {
                            getContextMenu().getTokenFC((String... data) -> {
                                if (data != null) {
                                    simuladorRequest.setTokenJwt(data[0]);
                                    simulador(simuladorRequest);
                                }else {
                                    isLoading  = false;
                                    btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
                                }
                            });
                        }
                    } else {
                        getContextMenu().getTokenFC((String... data) -> {
                            if (data != null) {
                                simuladorRequest.setTokenJwt(data[0]);
                                simulador(simuladorRequest);
                            }else {
                                isLoading  = false;
                                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
                            }
                        });
                    }
                }else{
                    getContextMenu().alert("Espera un momento, para continuar con tu petición");
                }

            }
        });

        getContextMenu().getTokenFC((String... text) -> {
            if (text != null) {
                getCAT(text[0]);
            }else{
                isLoading  = false;
                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
            }
        });
        fillResume();
        configView();
        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_DETALLE_CREDITO);

    }

    private void configView() {
        switch (type){
            case OFFER:
                break;
            case RECOMPRA:
                ll_back.setVisibility(View.GONE);
                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContextMenu().backFragment();

                    }
                });

                tr_solicitas.setVisibility(View.VISIBLE);
                tr_liquidas.setVisibility(View.VISIBLE);
                Double montoOferta = Double.parseDouble(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());
                Double saldoInsoluto = Double.parseDouble(SessionApp.getInstance().getDhListaCreditos().getSaldoInsoluto());

                tv_amount_offer.setText(Utils.paserCurrencyInt(String.valueOf(montoOferta)));
                tv_amount_insoluto.setText("- "+Utils.paserCurrencyInt(String.valueOf(saldoInsoluto)));
                tv_amount.setText(Utils.paserCurrencyInt(String.valueOf(Math.round(montoOferta-saldoInsoluto))));
                break;
            case RENOVACION:
                if (!TextUtils.isEmpty(SessionApp.getInstance().getFcRequestDTO().getFolio())){
                    ll_back.setVisibility(View.GONE);
                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContextMenu().backFragment();

                        }
                    });
                }
                break;
        }
    }

    private void fillResume() {
        simuladorRequest = SessionApp.getInstance().getFcRequestDTO().getSimulador();

        tv_amount.setText(Utils.paserCurrencyInt(String.valueOf(simuladorRequest.getMontoPrestamo())));
        tv_term_fee.setText(simuladorRequest.getNumPagos()+" X "+Utils.paserCurrencyInt(String.valueOf(Math.round(simuladorRequest.getCuota()))));
        String nameFrequency = "";
        switch (simuladorRequest.getFrecuenciaPago()){
            case "7":
                nameFrequency = "semanas";
                break;
            case "14":
               // nameFrequency = "catorcenas";
                nameFrequency = "quincenas";
                break;
            case "15":
                nameFrequency = "quincenas";
                break;
            case "30":
                nameFrequency = "meses";
                break;
        }
        tv_frequency_name.setText(nameFrequency);
        tv_total_amount.setText(Utils.paserCurrencyInt(String.valueOf(Math.round(simuladorRequest.getCuota()*Double.parseDouble(simuladorRequest.getNumPagos())))));

    }

    private void getCAT(String token) {
        FCSimuladorRequest simuladorRequest = SessionApp.getInstance().getFcRequestDTO().getSimulador();
        DHOfertaBimbo ofertaBimbo = null;
        Double tasa = 0.0;
        if (SessionApp.getInstance().getFcConsultaOfertaBimboResponse() != null) {
            ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);
            tasa = Double.parseDouble(ofertaBimbo.getTasa_sin_medico_en_casa())*100;

        }
        Double tasaAnual = 0.0;
        try{
            tasaAnual = tasa;
        }catch (Exception ex){}
        tv_tasa.setText(String.valueOf(Math.round(tasaAnual))+"%");

        tv_tasa_mensual.setText(String.valueOf(Math.round(tasaAnual/12)+"%"));

        FCCalculaCatRequest catRequest = new FCCalculaCatRequest();
        catRequest.setMonto(simuladorRequest.getMontoPrestamo().doubleValue());
        catRequest.setFrecuencia(Integer.parseInt(simuladorRequest.getFrecuenciaPago()));
        catRequest.setCuota(simuladorRequest.getCuota());
        catRequest.setnumeroPagos(Integer.parseInt(simuladorRequest.getNumPagos()));
        catRequest.setTokenJwt(token);

        Logger.d("REQUEST : "  + new Gson().toJson(catRequest));

        CalculaCat calculaCat = new CalculaCat(getContext());
        calculaCat.CalculaCat(catRequest,  new CalculaCat.onRequest(){

            @Override
            public <E> void onSuccess(E e) {
                FCCalculaCatResponse response = (FCCalculaCatResponse) e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response != null){
                    if (response.getCodigo() == 0) {
                        tv_cat.setText(String.valueOf(Math.round(response.getCat())) + "%");
                    }
                }
                getContext().loading(false);
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                if(s!= null){

                }
            }
        });

    }


    private void simulador(FCSimuladorRequest request) {
        request.setAsociado(promotor);
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        Simulador simulador = new Simulador(getContext());
        simulador.postSimulador(request, new Simulador.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                FCSimuladorResponse response = (FCSimuladorResponse)Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0 && !response.getFolio().equalsIgnoreCase("-")) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    isLoading  = false;
                    btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    switch (type){
                        case RENOVACION:
                           // Fragment_fincomun_contratacion_4.type = type;
                            getContext().setFragment(Fragment_fincomun_contratacion_1.newInstance());
                            break;
                        default:
                            getContext().setFragment(Fragment_fincomun_contratacion_1.newInstance());
                            break;
                    }
                    new Handler().postDelayed(new Runnable() { public void run(){
                        isLoading  = false;
                        btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
                    }}, 10000);
                }

              //  getContext().loading(false);
            }

            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
                isLoading  = false;
                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
            }
        });
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLoading = false;
        btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
    }

    @Override
    public void onStart() {
        super.onStart();
        isLoading = false;
        btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoading = false;
        btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
    }

    @Override
    public void onPause() {
        super.onPause();
        isLoading = false;
        btn_next.setBackgroundDrawable(ContextCompat.getDrawable(getContextMenu(),R.drawable.background_selector_green));
    }
}