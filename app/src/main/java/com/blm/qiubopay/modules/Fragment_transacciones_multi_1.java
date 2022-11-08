package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;


import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.N3Constants;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IGetBoxCutTxr;
import com.blm.qiubopay.listeners.IGetFinancialBalance;
import com.blm.qiubopay.listeners.IGetQiuboPaymentList;
import com.blm.qiubopay.listeners.IGetVasBalance;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_BalanceResponse;
import com.blm.qiubopay.models.QPAY_CommissionReportResponse;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUserStatus;
import com.blm.qiubopay.models.QPAY_LinkedUsersResponse;
import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.models.QPAY_Pagos_Qiubo_Response;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxr;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxrResponse;
import com.blm.qiubopay.models.corte_caja.QPAY_BoxCutPetition;
import com.blm.qiubopay.models.corte_caja.QPAY_BoxCutResponse;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentListResponse;
import com.blm.qiubopay.models.pidelo.CancelOrder;
import com.blm.qiubopay.models.services.QPAY_ServicePaymentResponse;
import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponse;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.reportes.Fragment_reporte_financiero;
import com.blm.qiubopay.modules.reportes.Fragment_reporte_no_financiero;
import com.blm.qiubopay.modules.tienda.Fragment_menu_ganancias;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.N3PrinterHelper;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.AESEncryption;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.orhanobut.logger.Logger;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_transacciones_multi_1 extends HFragment {

    private static final String TAG = "trans_multi_1";

    private View view;
    private MenuActivity context;

    private int tipo = 0;
    public static QPAY_LinkedUsersResponse users;

    private static final int TRANSACCIONES_TAE = 1;
    private static final int TRANSACCIONES_SERVICIOS = 2;
    private static final int TRANSACCIONES_FINANCIERAS = 3;
    private static final int TRANSACCIONES_PAGOS_QIUBO = 4;
    private static final int TRANSACCIONES_CARGOS_ABONOS = 5;

    private String url = "";
    private String time = "";
    private String date = "";

    private QPAY_BoxCutResponse serviceResponse;

    public static Fragment_transacciones_multi_1 newInstance(Object... data) {
        Fragment_transacciones_multi_1 fragment = new Fragment_transacciones_multi_1();
        /*Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_transacciones_multi_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (getArguments() != null)
        //    data = new Gson().fromJson(getArguments().getString("Fragment_transacciones_multi_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_transacciones_multi_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                context.backFragment();
            }
        });

        LinearLayout layout_recargas = view.findViewById(R.id.layout_recasrgas);
        LinearLayout layout_servicios = view.findViewById(R.id.layout_servicios);
        LinearLayout layout_pagos_qiubo = view.findViewById(R.id.layout_pagos_qiubo);
        LinearLayout layout_transacciones = view.findViewById(R.id.layout_transacciones);
        LinearLayout layout_transacciones_cargos_y_abonos = view.findViewById(R.id.layout_transacciones_cargos_y_abonos);
        LinearLayout layout_comisiones_dia = view.findViewById(R.id.layout_comisiones_dia);
        LinearLayout layout_ventas_financieras = view.findViewById(R.id.layout_ventas_financieras);
        LinearLayout layout_ventas_no_financieras = view.findViewById(R.id.layout_ventas_no_financieras);
        LinearLayout layout_reporte_financiero = view.findViewById(R.id.layout_reporte_financiero);

        final QPAY_LinkedUser linkedUser = new QPAY_LinkedUser();
        linkedUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        layout_recargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = TRANSACCIONES_TAE;
                transaccionesTAE(linkedUser);
            }
        });

        layout_servicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = TRANSACCIONES_SERVICIOS;
                transaccionesService(linkedUser);
            }
        });

        layout_pagos_qiubo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = TRANSACCIONES_PAGOS_QIUBO;
                transaccionesPagosQiubo(linkedUser);
            }
        });

        layout_transacciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = TRANSACCIONES_FINANCIERAS;
                transaccionesFinanciero(linkedUser);
            }
        });

        layout_transacciones_cargos_y_abonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = TRANSACCIONES_CARGOS_ABONOS;
                transaccionesFinancieroCargosAbonos(linkedUser);
            }
        });

        layout_comisiones_dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.loading(true);
                context.cargarSaldoBeneficios(false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        if(null != ((QPAY_CommissionReportResponse) data[0]).getQpay_object()) {
                            Fragment_menu_ganancias.response = ((QPAY_CommissionReportResponse) data[0]).getQpay_object()[0];
                            context.loading(false);
                            context.setFragment(Fragment_menu_ganancias.newInstance());
                        }
                    }
                });
            }
        });

        layout_ventas_financieras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta_financiero(new IFunction<String>() {
                    @Override
                    public void execute(String[] data) {
                        context.setFragment(Fragment_reporte_no_financiero.newInstance(data[0], null));
                    }
                });
            }
        });

        layout_ventas_no_financieras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta_no_financiero(new IFunction<String>() {
                    @Override
                    public void execute(String[] data) {
                        context.setFragment(Fragment_reporte_no_financiero.newInstance(data[0], data[1]));
                    }
                });
            }
        });

        layout_reporte_financiero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(Fragment_reporte_financiero.newInstance());
            }
        });

        if("0".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3()) &&
                !"1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7())) {
            layout_transacciones.setVisibility(View.GONE);
            layout_ventas_financieras.setVisibility(View.GONE);
            layout_reporte_financiero.setVisibility(View.GONE);
        }

        Button btn_corte_caja = getView().findViewById(R.id.btn_corte_caja);
        btn_corte_caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                corteCaja();
            }
        });

        btn_corte_caja.setVisibility(Tools.userIsFinancial() ? View.VISIBLE : View.GONE);

    }

    private void transaccionesTAE(QPAY_LinkedUser linkedUser){

        final ArrayList list_tae = new ArrayList();

        context.loading(true);

        try {

            IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponse.QPAY_TaeSaleResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_TaeSaleResponse taeResponse = gson.fromJson(json, QPAY_TaeSaleResponse.class);

                        if (taeResponse.getQpay_response().equals("true")) {

                            if(taeResponse.getQpay_object().length == 0){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            for (int i = 0; i < taeResponse.getQpay_object().length; i++)
                                if(taeResponse.getQpay_object()[i].getRspCode().equals("000")){
                                    list_tae.add(taeResponse.getQpay_object()[i]);
                                }

                            if(list_tae.isEmpty()){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            //20200804 RSB, Imp. Ordenar por fecha
                            orderByDate(list_tae);

                            context.setFragment(Fragment_transacciones_multi_2.newInstance(list_tae,TRANSACCIONES_TAE,users));

                        } else {
                            if(taeResponse.getQpay_code().equals("017")
                                    || taeResponse.getQpay_code().equals("018")
                                    || taeResponse.getQpay_code().equals("019")
                                    || taeResponse.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }

                                });

                            } else if(taeResponse.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(R.string.texto_sin_transacciones);
                            }

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.getTodayTaeSalesByUser(linkedUser);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    private void transaccionesService(QPAY_LinkedUser linkedUser){

        final ArrayList list_tae = new ArrayList();

        context.loading(true);

        try {

            IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponse.QPAY_TaeSaleResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_ServicePaymentResponse taeResponse = gson.fromJson(json, QPAY_ServicePaymentResponse.class);

                        if (taeResponse.getQpay_response().equals("true")) {

                            if(taeResponse.getQpay_object().length == 0){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            for (int i = 0; i < taeResponse.getQpay_object().length; i++)
                                if(taeResponse.getQpay_object()[i].getRspCode().equals("000"))
                                    list_tae.add(taeResponse.getQpay_object()[i]);

                            if(list_tae.isEmpty()){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            //20200804 RSB, Imp. Ordenar por fecha
                            orderByDate(list_tae);

                            context.setFragment(Fragment_transacciones_multi_2.newInstance(list_tae, TRANSACCIONES_SERVICIOS,users));

                        } else {
                            if(taeResponse.getQpay_code().equals("017")
                                    || taeResponse.getQpay_code().equals("018")
                                    || taeResponse.getQpay_code().equals("019")
                                    || taeResponse.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.

                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if(taeResponse.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(R.string.texto_sin_transacciones);
                            }
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.getTodayServicePaymentsByUser(linkedUser);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    private void transaccionesPagosQiubo(QPAY_LinkedUser linkedUser){

        final ArrayList list_pagos = new ArrayList();

        context.loading(true);

        try {

            IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Log.e("JSON", "" + new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);

                    } else {

                        String json = new Gson().toJson(result);
                        QPAY_Pagos_Qiubo_Response response = new Gson().fromJson(json, QPAY_Pagos_Qiubo_Response.class);

                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++)
                                if(response.getQpay_object()[i].getRspCode().equals("000"))
                                    list_pagos.add(response.getQpay_object()[i]);

                            if(list_pagos.isEmpty()){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            //20200804 RSB, Imp. Ordenar por fecha
                            orderByDate(list_pagos);

                            //--------------------REVISAR------------------
                          /*  //RSB 20191127. Corrección a Transacciones PagosConecta
                            if(Fragment_pago_qiubo_1.objectList != null){
                                context.setFragment(Fragment_transacciones_multi_2.newInstance(list_pagos, TRANSACCIONES_PAGOS_QIUBO,users));
                            } else {
                                getQiuboPaymentList(list_pagos,4);
                            }*/


                        } else {
                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(R.string.texto_sin_transacciones);
                            }
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.getTodayTransactionsCompletedByUser(linkedUser);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    private void transaccionesFinanciero(QPAY_LinkedUser linkedUser){

        final ArrayList list_tae = new ArrayList();

        context.loading(true);

        try {

            /*QPAY_VisaEmvRequest authSeed = new QPAY_VisaEmvRequest();
            authSeed.getCspHeader().setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            authSeed.getCspHeader().setNullObjects();
            authSeed.setCspBody(null);*/

            IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_VisaResponse response = gson.fromJson(json, QPAY_VisaResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++)
                                if(response.getQpay_object()[i].getCspBody().getTxDate()!=null &&
                                        !response.getQpay_object()[i].getCspBody().getTxDate().trim().equals("null") &&
                                        response.getQpay_object()[i].getCspBody().getMaskedPan()!=null &&
                                        !response.getQpay_object()[i].getCspBody().getMaskedPan().trim().equals("null"))
                                    list_tae.add(response.getQpay_object()[i]);

                            if(list_tae.isEmpty()){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            //20200804 RSB, Imp. Ordenar por fecha
                            orderByDate(list_tae);

                            context.setFragment(Fragment_transacciones_multi_2.newInstance(list_tae, TRANSACCIONES_FINANCIERAS,users));

                        } else {
                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            }else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(R.string.texto_sin_transacciones);
                            }
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.getTodayTransactions(linkedUser);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    private void transaccionesFinancieroCargosAbonos(QPAY_LinkedUser linkedUser){

        final ArrayList list_txr = new ArrayList();

        context.loading(true);

        try {

            IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_CreditAndDebitTxrResponse.QPAY_CreditAndDebitTxrResponseExcluder()).create();
                        String json = gson.toJson(result);//"{\"qpay_response\":\"true\",\"qpay_code\":\"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[]}";//gson.toJson(result);
                        QPAY_CreditAndDebitTxrResponse response = gson.fromJson(json, QPAY_CreditAndDebitTxrResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++) {
                                if(!response.getQpay_object()[i].getOperationType().trim().equals("Migrate to QTC2")
                                        && !response.getQpay_object()[i].getOperationType().trim().equals("Null")
                                        && !response.getQpay_object()[i].getOperationType().trim().equals(""))
                                    list_txr.add(response.getQpay_object()[i]);
                            }

                            if(list_txr.isEmpty()){
                                context.alert(R.string.texto_sin_transacciones);
                                return;
                            }

                            //20200804 RSB, Imp. Ordenar por fecha
                            orderByDate(list_txr);

                            context.setFragment(Fragment_transacciones_multi_2.newInstance(list_txr, TRANSACCIONES_CARGOS_ABONOS,users));

                        } else {
                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });

                            }else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(R.string.texto_sin_transacciones);
                            }
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.getTodayChargesAndPaysByUser(linkedUser);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }


    //RSB 20191127. Corrección transacciones
    /**
     * Recupera la lista de pagosConecta
     * @param list_pagos
     * @param tipo
     */
    private void getQiuboPaymentList(final ArrayList list_pagos, final int tipo){

        final ArrayList list_txr = new ArrayList();

        context.loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetQiuboPaymentList petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_QiuboPaymentListResponse.QPAY_QiuboPaymentListExcluder()).create();
                        String json = gson.toJson(result);//"{\"qpay_response\":\"true\",\"qpay_code\":\"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[]}";//gson.toJson(result);
                        QPAY_QiuboPaymentListResponse response = gson.fromJson(json, QPAY_QiuboPaymentListResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                context.alert("En este momento no hay pagos disponibles.");
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++) {
                                list_txr.add(response.getQpay_object()[i]);
                            }

                            if(list_txr.isEmpty()){
                                context.alert("En este momento no hay pagos disponibles.");
                                return;
                            }

                           // Fragment_pago_qiubo_1.objectList = list_txr;
                            context.setFragment(Fragment_transacciones_multi_2.newInstance(list_pagos, tipo, users));

                        } else {
                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });

                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(R.string.texto_sin_transacciones);
                            }
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);

            petition.doGetList(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    //20200804 RSB. Imp. Ordenar por fecha
    /**
     * Metodo para ordenar listas por fechas
     * @param list
     */
    private void orderByDate(ArrayList list){

        switch(tipo){
            case TRANSACCIONES_TAE: //TAE
                Collections.sort(list, new Comparator<TaeSale>() {
                    public int compare(TaeSale o1, TaeSale o2) {
                        int iCompare = 0;
                        if(o1.getResponseAt()!=null && !o1.getResponseAt().isEmpty()
                                && o2.getResponseAt()!=null && !o2.getResponseAt().isEmpty()) {
                            try {
                                Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o1.getResponseAt()));
                                Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o2.getResponseAt()));
                                iCompare = date2.compareTo(date1);
                            } catch (ParseException e) {
                                Log.e(TAG,"ParseError: " + e.getLocalizedMessage());
                            }
                        }
                        return iCompare;
                    }
                });
                break;

            case TRANSACCIONES_SERVICIOS: //PAGO DE SERVICIOS
                Collections.sort(list, new Comparator<ServicePayment>() {
                    public int compare(ServicePayment o1, ServicePayment o2) {
                        int iCompare = 0;
                        if(o1.getResponseAt()!=null && !o1.getResponseAt().isEmpty()
                                && o2.getResponseAt()!=null && !o2.getResponseAt().isEmpty()) {
                            try {
                                Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o1.getResponseAt()));
                                Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o2.getResponseAt()));
                                iCompare = date2.compareTo(date1);
                            } catch (ParseException e) {
                                Log.e(TAG,"ParseError: " + e.getLocalizedMessage());
                            }
                        }
                        return iCompare;
                    }
                });
                break;

            case TRANSACCIONES_FINANCIERAS: //FINANCIERAS
                Collections.sort(list, new Comparator<QPAY_VisaEmvResponse>() {
                    public int compare(QPAY_VisaEmvResponse o1, QPAY_VisaEmvResponse o2) {
                        int iCompare = 0;
                        if(o1.getCspBody().getTxDate()!=null && !o1.getCspBody().getTxDate().isEmpty()
                                && o2.getCspBody().getTxDate()!=null && !o2.getCspBody().getTxDate().isEmpty()) {
                            try {
                                Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o1.getCspBody().getTxDate()));
                                Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o2.getCspBody().getTxDate()));
                                iCompare = date2.compareTo(date1);
                            } catch (ParseException e) {
                                Log.e(TAG,"ParseError: " + e.getLocalizedMessage());
                            }
                        }
                        return iCompare;
                    }
                });
                break;

            case TRANSACCIONES_PAGOS_QIUBO: //PAGOSConecta
                Collections.sort(list, new Comparator<QPAY_Pago_Qiubo_object>() {
                    public int compare(QPAY_Pago_Qiubo_object o1, QPAY_Pago_Qiubo_object o2) {
                        int iCompare = 0;
                        if(o1.getTimestamp()!=null && !o1.getTimestamp().isEmpty()
                                && o2.getTimestamp()!=null && !o2.getTimestamp().isEmpty()) {
                            try {
                                Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o1.getTimestamp()));
                                Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o2.getTimestamp()));
                                iCompare = date2.compareTo(date1);
                            } catch (ParseException e) {
                                Log.e(TAG,"ParseError: " + e.getLocalizedMessage());
                            }
                        }
                        return iCompare;
                    }
                });
                break;

            case TRANSACCIONES_CARGOS_ABONOS: //CARGOS Y ABONOS
                Collections.sort(list, new Comparator<QPAY_CreditAndDebitTxr>() {
                    public int compare(QPAY_CreditAndDebitTxr o1, QPAY_CreditAndDebitTxr o2) {
                        int iCompare = 0;
                        if(o1.getResponseAt()!=null && !o1.getResponseAt().isEmpty()
                                && o2.getResponseAt()!=null && !o2.getResponseAt().isEmpty()) {
                            try {
                                Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o1.getResponseAt()));
                                Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(Utils.formatDate(o2.getResponseAt()));
                                iCompare = date2.compareTo(date1);
                            } catch (ParseException e) {
                                Log.e(TAG,"ParseError: " + e.getLocalizedMessage());
                            }
                        }
                        return iCompare;
                    }
                });
                break;
        }

    }

    public void corteCaja() {

        Fragment_browser.execute = new IAlertButton() {
            @Override
            public String onText() {
                return "TERMINAR";
            }

            @Override
            public void onClick() {

                getContext().alert("¿Desea hacer un corte de caja en este momento?", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        AppPreferences.setBoxCutTime(Utils.getTime());
                        AppPreferences.setBoxCutDate(Utils.getDay());
                        context.initHome();
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

            }
        };

        time = AppPreferences.getBoxCutTime();
        date = AppPreferences.getBoxCutDate();

        if(!date.trim().equals(Utils.getDay().trim())){
            //Hubo un cambio de día
            AppPreferences.setBoxCutTime("");
            AppPreferences.setBoxCutDate("");
            time = "";
            date = "";
        }

        consulta_corte_de_caja(time, new IFunction<String>() {
            @Override
            public void execute(String... data) {
                if("true".equals(serviceResponse.getQpay_response())) {
                    try {

                        if (time.trim().equals(""))
                            url = String.format("%s?u=%s", Globals.URL_TXR_BY_HOUR, URLEncoder.encode(AESEncryption.encrypt(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()), "UTF-8"));
                        else
                            url = String.format("%s?u=%s&i=%s", Globals.URL_TXR_BY_HOUR, URLEncoder.encode(AESEncryption.encrypt(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()), "UTF-8"), URLEncoder.encode(AESEncryption.encrypt(time), "UTF-8"));

                    } catch (Exception ex) {

                    }

                    CApplication.setAnalytics(CApplication.ACTION.CB_terminos_condiciones);

                    printTicket(new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            getContext().setFragment(Fragment_browser.newInstance(url));
                        }
                    }, true);
                }else{
                    getContext().alert(serviceResponse.getQpay_description());
                }
            }

        });

    }

    public void consulta_corte_de_caja(String time, final IFunction<String> function){

        getContext().loading(true);

        try {

            QPAY_BoxCutPetition objectPetition = new QPAY_BoxCutPetition();
            objectPetition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            if (!time.trim().equals("")){
                objectPetition.setQpay_initial_hour(time);
            }
            IGetBoxCutTxr petition  = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        //context.showAlert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BoxCutResponse.QPAY_BoxCutResponseExcluder()).create();
                        String json = gson.toJson(result);
                        serviceResponse = gson.fromJson(json, QPAY_BoxCutResponse.class);

                        if("true".equals(serviceResponse.getQpay_response())){
                            function.execute();
                        }else {

                            if(serviceResponse.getQpay_description().contains("No hay transacciones registradas"))
                                function.execute();
                            else
                                context.validaSesion(serviceResponse.getQpay_code(), serviceResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    //context.showAlert(R.string.general_error);
                }

            }, getContext());

            petition.getTrx(objectPetition);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void consulta_no_financiero(final IFunction<String> function){

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetVasBalance petition  = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BalanceResponse.QPAY_BalanceResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_BalanceResponse serviceResponse = gson.fromJson(json, QPAY_BalanceResponse.class);

                        if("true".equals(serviceResponse.getQpay_response())){
                            if(function != null && serviceResponse.getQpay_object().length > 0)
                                function.execute(
                                        Utils.paserCurrency(serviceResponse.getQpay_object()[0].getQpay_total_txn()),
                                        Utils.paserCurrency(serviceResponse.getQpay_object()[0].getQpay_balance().replace("MXN","")));
                            else
                                function.execute(Utils.paserCurrency("0.0"), Utils.paserCurrency("0.0"));
                        } else {
                            context.validaSesion(serviceResponse.getQpay_code(), serviceResponse.getQpay_description());
                        }


                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }

            }, getContext());

            petition.getBalanceV(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void consulta_financiero(final IFunction<String> function){

        getContext().loading(true);

        try {

            QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();
            visaEmvRequest.getCspHeader().setNullObjects();
            visaEmvRequest.setCspBody(null);

            IGetFinancialBalance petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.e(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BalanceResponse.QPAY_BalanceResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_BalanceResponse serviceResponse = gson.fromJson(json, QPAY_BalanceResponse.class);

                        if("true".equals(serviceResponse.getQpay_response())){
                            if(function != null && serviceResponse.getQpay_object().length > 0)
                                function.execute(Utils.paserCurrency(serviceResponse.getQpay_object()[0].getQpay_total_txn()));
                            else
                                function.execute(Utils.paserCurrency("0.0"));
                        } else {
                            context.validaSesion(serviceResponse.getQpay_code(), serviceResponse.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }

            }, getContext());

            petition.getBalanceF(visaEmvRequest);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    private void printTicket(final IFunction function, Boolean isCardHolderTicket){
        //Logo
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));


        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"CORTE DE CAJA"));

        //Espacio en blanco
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        //Información del comercio
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, Tools.getStateName(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state().trim())));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Comercio: ", "1014327" + "/" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Terminal: ", AppPreferences.getUserProfile().getQpay_object()[0].getQpay_gateway_user()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Fecha: ", Utils.timeStamp()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"", "Moneda: ", "MXN"));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "\nDETALLES DE LA TRANSACCIÓN"));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "\n#TRANSACCIÓN      TIPO      HORA"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "", "NO.TARJETA:", "TOTAL"));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        if(serviceResponse.getQpay_object()[0].getTxns().length > 0){
            for(int i=0; i < serviceResponse.getQpay_object()[0].getTxns().length; i++){
                lines.add(new FormattedLine(N3Constants.FONT_SIZE_SMALL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"",String.format("%s    %s", serviceResponse.getQpay_object()[0].getTxns()[i].getId(), serviceResponse.getQpay_object()[0].getTxns()[i].getTxTypeName()), serviceResponse.getQpay_object()[0].getTxns()[i].getTxCreatedAt()));
                lines.add(new FormattedLine(N3Constants.FONT_SIZE_SMALL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "", String.format("************%s", serviceResponse.getQpay_object()[0].getTxns()[i].getMaskedPan()), "$" + serviceResponse.getQpay_object()[0].getTxns()[i].getTxAmount()));
                lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
            }
        }
        else
        {
            //No se encontraron transacciones
            lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "No hay transacciones registradas"));
            lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        }

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"TOTALES GENERALES"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT,"", String.format("VENTAS: (%s)", serviceResponse.getQpay_object()[0].getSalesCount()), "$" + serviceResponse.getQpay_object()[0].getSalesAmount()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT,"", String.format("CANCELACIONES: (%s)", serviceResponse.getQpay_object()[0].getVoidsCount()), "$" + serviceResponse.getQpay_object()[0].getVoidsAmount()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "","TOTAL:", "$" + serviceResponse.getQpay_object()[0].getTotalAmount()));

        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, getContext());//N3_BACKUP
    }

}
