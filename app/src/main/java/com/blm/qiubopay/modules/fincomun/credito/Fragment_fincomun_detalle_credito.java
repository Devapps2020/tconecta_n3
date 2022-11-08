package com.blm.qiubopay.modules.fincomun.credito;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_cuentas_1;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.login.Fragment_fincomun_login;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCPlanPagosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCConsultaCreditosResponse;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaCreditos;
import mx.com.fincomun.origilib.Objects.Credito.DHPlanPago;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_detalle_credito extends HFragment implements IMenuContext {

    public static FCPlanPagosResponse detalle = null;
    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private TextView tv_next_fee,tv_payment_date, tv_payment_amount;
    private TextView tv_credit_number,tv_frecuency, tv_clabe, tv_client_number;
    private TextView tv_cobranza, tv_interes, tv_date,tv_total_pay;
    private TextView tv_cuotas_pagadas, tv_cuotas_por_pagar, tv_total_cuotas,tv_pago_sin_atraso,tv_vencidas;
    private Slider sld_frequency;

    public static Fragment_fincomun_detalle_credito newInstance() {
        Fragment_fincomun_detalle_credito fragment = new Fragment_fincomun_detalle_credito();

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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_detalle_credito"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_detalle_credito, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        tv_payment_amount = getView().findViewById(R.id.tv_payment_amount);
        tv_next_fee = getView().findViewById(R.id.tv_next_fee);
        tv_payment_date = getView().findViewById(R.id.tv_payment_date);

        tv_total_pay = getView().findViewById(R.id.tv_total_pay);
        tv_credit_number = getView().findViewById(R.id.tv_credit_number);
        tv_frecuency = getView().findViewById(R.id.tv_frecuency);
        tv_clabe = getView().findViewById(R.id.tv_clabe);
        tv_client_number = getView().findViewById(R.id.tv_client_number);
        tv_pago_sin_atraso = getView().findViewById(R.id.tv_pago_sin_atraso);
        tv_vencidas = getView().findViewById(R.id.tv_vencidas);

        tv_cobranza = getView().findViewById(R.id.tv_cobranza);
        tv_interes = getView().findViewById(R.id.tv_interes);
        tv_date = getView().findViewById(R.id.tv_date);

        tv_cuotas_pagadas = getView().findViewById(R.id.tv_cuotas_pagadas);
        tv_cuotas_por_pagar = getView().findViewById(R.id.tv_cuotas_por_pagar);
        tv_total_cuotas = getView().findViewById(R.id.tv_total_cuotas);
        sld_frequency = getView().findViewById(R.id.sld_frequency);
        sld_frequency.setEnabled(false);

        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().setFragment(Fragment_fincomun_pago_credito.newInstance());

            }
        });
        fillData();

        consultaCuentas();
    }

    private void fillData() {
        FCConsultaCreditosResponse response = SessionApp.getInstance().getFcConsultaCreditosResponse();
        DHListaCreditos credit = SessionApp.getInstance().getDhListaCreditos();

        Double exigible = (credit.getCuotasVencidas() != 0)
                ? (credit.getCuotasVencidas()+1)*Double.parseDouble(credit.getErogacion())
                    +Double.parseDouble(credit.getGastoCobranza())+Double.parseDouble(credit.getSaldoMoratorio())
                : Double.parseDouble(credit.getErogacion())+Double.parseDouble(credit.getGastoCobranza())+Double.parseDouble(credit.getSaldoMoratorio());
        tv_payment_amount.setText(Utils.paserCurrency(String.valueOf(exigible)));
        tv_pago_sin_atraso.append(" "+Utils.paserCurrency(credit.getErogacion()));//exigible
        tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoInicial()));
        tv_payment_date.setText(credit.getFechaDesembolso().substring(0,10));
        tv_vencidas.setText("Cuotas vencidas: "+credit.getCuotasVencidas());
        tv_total_pay.append(" "+Utils.paserCurrency(credit.getSaldoInsoluto()));
        tv_credit_number.append(" "+credit.getNumeroCredito());
        String frecuency = "";
        switch (credit.getUnidad()){
            case "1":
                frecuency = "único";
                break;
            case "2":
                frecuency = "diarios";
                break;
            case "3":
                frecuency = "semanales";
                break;
            case "4":
                frecuency = "quincenales";
                break;
            case "5":
                frecuency = "mensuales";
                break;
            default:
                frecuency = String.valueOf(credit.getUnidad());
                break;
        }
        tv_frecuency.append(" "+credit.getPlazo()+" pagos "+frecuency);
        tv_client_number.append((TextUtils.isEmpty(response.getNumClienteSib()))?" "+HCoDi.numCliente:" "+response.getNumClienteSib());

        tv_cobranza.append(" "+Utils.paserCurrency((TextUtils.isEmpty(credit.getGastoCobranza()))?"0":credit.getGastoCobranza()));
        tv_interes.append(" "+Utils.paserCurrency((TextUtils.isEmpty(credit.getSaldoMoratorio()))?"0":credit.getSaldoMoratorio()));
        tv_date.append(" "+credit.getFechaLimitePago().substring(0,10));

        if (detalle != null){
            tv_cuotas_pagadas.setText(String.valueOf(detalle.getCredNumAmortPagadas()));
            tv_cuotas_por_pagar.setText(String.valueOf(detalle.getCredNumAmortPorPagar()));
            tv_total_cuotas.setText(String.valueOf(detalle.getPlanDePagos().size()));

            float to = detalle.getPlanDePagos().size();
            sld_frequency.setValueTo(to);
            sld_frequency.setValue((float)detalle.getCredNumAmortPagadas());
        }

    }

    private void consultaCuentas(){
        getContextMenu().loading(true);
        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... data) {

                if (data!= null) {
                    getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            FCConsultaCuentasResponse response = (FCConsultaCuentasResponse) data[0];
                            if (response.getCodigo().equalsIgnoreCase("0")) {
                                SessionApp.getInstance().getDhListaCreditos().setCuentaCable(response.getCuentas().get(0).getClabeCuenta());
                                tv_clabe.append(" " + response.getCuentas().get(0).getClabeCuenta());

                            }

                        }
                    });
                }
            }
        });
    }
}