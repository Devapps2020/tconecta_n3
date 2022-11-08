package com.blm.qiubopay.modules.fincomun.originacion;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.fincomun.FCRequestDTO;
import com.blm.qiubopay.modules.fincomun.adelantoVenta.Fragment_adelanto_venta_simulador;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_solicitud_fincomun_4;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;
import com.orhanobut.logger.Logger;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCSinOfertaRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCSinOfertaResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSimuladorResponse;
import mx.com.fincomun.origilib.Model.Originacion.Credito.AltaBitacoraSinOferta;
import mx.com.fincomun.origilib.Model.Originacion.Simulador;
import mx.com.fincomun.origilib.Objects.DHOfertaBimbo;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCCalculaCatRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCCalculaCatResponse;
import mx.com.fincomun.origilib.Model.Originacion.Credito.CalculaCat;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_oferta extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<HEditText> campos;
    private Object data;
    private Button btn_accept;
    private TextView tv_decline,tv_offer,tv_simulator,tv_information,tv_felicidades;
    private TableRow tr_cash,tr_adelanto;
    String info = "";
    private Boolean isLoading  =false;
    public enum Type{
        OFFER,
        RECOMPRA,
        RENOVACION,
        ANALISIS,
        ADELANTO_VENTAS
    }
    public static Type type = Type.OFFER;

    public static Fragment_fincomun_oferta newInstance() {
        Fragment_fincomun_oferta fragment = new Fragment_fincomun_oferta();

        return fragment;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_oferta"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_oferta, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        tv_decline = getView().findViewById(R.id.tv_decline);
        tv_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().backFragment();
            }
        });
        tv_offer = getView().findViewById(R.id.tv_offer);

        btn_accept = getView().findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    switch (type) {
                        case OFFER:
                        case RENOVACION:
                            Fragment_fincomun_originacion_simulador.type = type;
                            getContext().setFragment(Fragment_fincomun_originacion_simulador.newInstance());
                            break;
                        case RECOMPRA:
                            Fragment_fincomun_contratacion_4.type = Type.RECOMPRA;
                            getContext().setFragment(Fragment_fincomun_contratacion_4.newInstance());
                            break;
                        case ADELANTO_VENTAS:
                            getContext().setFragment(Fragment_adelanto_venta_simulador.newInstance());
                            break;
                    }
                    isLoading = false;
                }
            }
        });

        tv_simulator = getView().findViewById(R.id.tv_simulator);
        tv_simulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_fincomun_originacion_simulador.newInstance());
            }
        });
        tv_information = getView().findViewById(R.id.tv_information);
        info = context.getResources().getString(R.string.txt_cat);
        tr_cash = getView().findViewById(R.id.tr_cash);
        tr_adelanto = getView().findViewById(R.id.tr_adelanto);
        tv_felicidades = getView().findViewById(R.id.tv_felicidades);
        showNoOffer();

        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_OFERTA);

    }

    private void showNoOffer(){
        switch (type){
            case ADELANTO_VENTAS:
                tv_felicidades.setText("¡Felicidades *Nombre*!");
                tr_adelanto.setVisibility(View.VISIBLE);
                break;
            default:
                if (SessionApp.getInstance().getFcConsultaOfertaBimboResponse() == null || SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo() == null) {
                    getContextMenu().showNoOfferFC(new IClickView() {
                        @Override
                        public void onClick(Object... data) {

                            getContextMenu().setFragment(Fragment_fincomun_oferta_excepcion.newInstance());

                        }
                    }, new IClickView() {
                        @Override
                        public void onClick(Object... data) {
                            getContextMenu().backFragment();
                        }
                    });
                    btn_accept.setEnabled(false);
                    tv_simulator.setEnabled(false);
                }else{
                    DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###");
                    switch (type){
                        case OFFER:
                        case RENOVACION:
                            tv_offer.append("$"+formatter.format(Double.parseDouble(ofertaBimbo.getMonto())));
                            break;
                        case RECOMPRA:
                            tv_offer.setText(getContext().getResources().getString(R.string.txt_recompra)+" $"+formatter.format(Double.parseDouble(ofertaBimbo.getMonto())));
                            tr_cash.setVisibility(View.GONE);
                            break;
                    }

                    configRequest(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());
                }
                break;
        }

    }

    public void configRequest(String monto) {

        FCSimuladorRequest simuladorRequest = new FCSimuladorRequest();

        simuladorRequest.setIdTipoCredito(2);
        simuladorRequest.setIdModCredito(6);
        simuladorRequest.setIdTipoProducto(2);
        simuladorRequest.setMontoPrestamo(new BigInteger(monto.replace(".00", "")));
        simuladorRequest.setIngresos(BigInteger.ONE);
        simuladorRequest.setNumPagos("18");
        simuladorRequest.setDestino("311");
        simuladorRequest.setSucursal(Globals.FC_SUCURSAL);
        simuladorRequest.setUsuario(Globals.FC_USUARIO);//
        simuladorRequest.setFrecuenciaPago("15");
        simuladorRequest.setIdPromotor(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        simuladorRequest.setCuota(getCuota(false,
                Integer.parseInt(monto.replace(".00", "")),
                Integer.parseInt(simuladorRequest.getNumPagos()),
                Integer.parseInt(simuladorRequest.getFrecuenciaPago())));

        SessionApp.getInstance().getFcRequestDTO().setMedico(false);
        SessionApp.getInstance().getFcRequestDTO().setSimulador(simuladorRequest);
    }

    public Double getCuota(boolean seguro, Integer monto, Integer plazo, Integer frecuencia) {

        try {

            Double tasa_periodo = getPlazoPeriodo(seguro, frecuencia)/100;
            Double potencia1 = (tasa_periodo * (Math.pow((1.0 + tasa_periodo), plazo)));
            Double potencia2 = (Math.pow((1.0 + tasa_periodo), plazo) - 1.0);

            return round(monto * (potencia1 / potencia2));

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public Double getPlazoPeriodo(boolean seguro, Integer frecuencia) {

        DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

        Double tasa_seguro = Double.parseDouble(ofertaBimbo.getTasa_con_medico_en_casa()) * 100;
        Double tasa_sin_seguro = Double.parseDouble(ofertaBimbo.getTasa_sin_medico_en_casa()) * 100;
        Double tasa = (seguro ? tasa_seguro : tasa_sin_seguro);

        SessionApp.getInstance().getFcRequestDTO().setTasa(tasa);

        ArrayList<Integer> frecuencias = new ArrayList();
        frecuencias.add(7);
        frecuencias.add(14);
        frecuencias.add(28);

        int periodo = 360;

        if(frecuencias.contains(frecuencia))
            periodo = 364;

        return round((tasa / periodo) * frecuencia * 1.16);
    }

    public Double round(double number) {
        number = Math.round(number * 100);
        return  number/100;
    }

}
