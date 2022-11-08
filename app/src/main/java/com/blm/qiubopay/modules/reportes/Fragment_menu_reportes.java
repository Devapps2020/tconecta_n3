package com.blm.qiubopay.modules.reportes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewOption;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.model.FINANCIAL_BD_ROW;
import com.blm.qiubopay.database.model.QP_BD_ROW;
import com.blm.qiubopay.database.model.SERVICE_BD_ROW;
import com.blm.qiubopay.database.model.TAE_BD_ROW;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.N3Constants;
import com.blm.qiubopay.listeners.IDebitAndCreditTxrs;
import com.blm.qiubopay.listeners.IGetBoxCutTxr;
import com.blm.qiubopay.listeners.IGetFinancialBalance;
import com.blm.qiubopay.listeners.IGetLastFinancialTransactions;
import com.blm.qiubopay.listeners.IGetLastServicesTransactions;
import com.blm.qiubopay.listeners.IGetLastTaeTransactions;
import com.blm.qiubopay.listeners.IGetQiuboPaymentList;
import com.blm.qiubopay.listeners.IGetTodayTransactionsComplete;
import com.blm.qiubopay.listeners.IGetVasBalance;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_BalanceResponse;
import com.blm.qiubopay.models.QPAY_CommissionReportResponse;
import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.models.QPAY_Pagos_Qiubo_Response;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxr;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxrResponse;
import com.blm.qiubopay.models.corte_caja.QPAY_BoxCutPetition;
import com.blm.qiubopay.models.corte_caja.QPAY_BoxCutResponse;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentItem;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentListResponse;
import com.blm.qiubopay.models.services.QPAY_ServicePaymentResponse;
import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponse;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.modules.Fragment_depositos_1;
import com.blm.qiubopay.modules.Fragment_info_personal_1;
import com.blm.qiubopay.modules.Fragment_transacciones_3;
import com.blm.qiubopay.modules.Fragment_transacciones_4;
import com.blm.qiubopay.modules.Fragment_transacciones_5;
import com.blm.qiubopay.modules.Fragment_transacciones_6;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.home.Fragment_menu_pagos_basicos;
import com.blm.qiubopay.modules.home.Fragment_menu_pagos_financieros;
import com.blm.qiubopay.modules.regional.Fragment_pago_regional_menu;
import com.blm.qiubopay.modules.tienda.Fragment_menu_ganancias;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.AESEncryption;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.orhanobut.logger.Logger;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_reportes extends HFragment implements IMenuContext {

    private DataHelper dataHelper;
    private ArrayList<Object> arrayList;
    private Boolean forceRemoteQuery;
    public static List<Object> list;

    private String url = "";
    private String time = "";
    private String date = "";

    private QPAY_BoxCutResponse serviceResponse;

    public static Fragment_menu_reportes newInstance() {
        return new Fragment_menu_reportes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_reportes, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_recargas)).setText(R.string.text_operativas_2).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "RECARGAS"));

                forceRemoteQuery = false;
                if(Globals.LOCAL_TRANSACTION)
                    transaccionesTAE_DB();
                else
                    transaccionesTAE();
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_servicios)).setText(R.string.text_operativas_3).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "PAGOS DE SERVICIOS"));

                forceRemoteQuery = false;
                if(Globals.LOCAL_TRANSACTION)
                    transaccionesService_DB();
                else
                    transaccionesService();

            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_tarjetas)).setText(R.string.text_operativas_4).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "PAGOS Y RETIROS CON TARJETA"));

                forceRemoteQuery = false;
                if(Globals.LOCAL_TRANSACTION)
                    transaccionesFinanciero_DB();
                else
                    transaccionesFinanciero();

            }
        }).setVisible(Tools.userIsFinancial());

        CViewOption.create(getView().findViewById(R.id.layout_cargos_abonos)).setText(R.string.text_operativas_5).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "CARGOS Y ABONOS"));
                transaccionesFinancieroCargosAbonos();
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_comisiones_dia)).setText(R.string.text_inicio_7).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                getContextMenu().loading(true);
                getContextMenu().cargarSaldoBeneficios(false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        if(null != ((QPAY_CommissionReportResponse) data[0]).getQpay_object()) {
                            Fragment_menu_ganancias.response = ((QPAY_CommissionReportResponse) data[0]).getQpay_object()[0];
                            getContextMenu().loading(false);
                            getContextMenu().setFragment(Fragment_menu_ganancias.newInstance());
                        }
                    }
                });
            }
        });

        if("1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3()) && "1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7()))
            CViewOption.create(getView().findViewById(R.id.layout_financiero)).setText(R.string.text_operativas_8).onClick(new CViewOption.IClick() {
                @Override
                public void onClick(View v) {
                    CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "PAGOS Y RETIROS CON TARJETAS"));
                    consulta_financiero(new IFunction<String>() {
                        @Override
                        public void execute(String[] data) {
                            //AppPreferences.setKinetoBalance(data[1]);
                            getContextMenu().setFragment(Fragment_reporte_no_financiero.newInstance(data[0], null));
                        }
                    });
                }
            });
        else{
            View viewFinanciero = getView().findViewById(R.id.layout_financiero);
            viewFinanciero.setVisibility(View.GONE);
        }

        CViewOption.create(getView().findViewById(R.id.layout_no_financiero)).setText(R.string.text_operativas_7).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "TIEMPO AIRE Y PAGO DE SERVICIOS"));
                consulta_no_financiero(new IFunction<String>() {
                    @Override
                    public void execute(String[] data) {
                        //AppPreferences.setKinetoBalance(data[1]);
                        getContextMenu().setFragment(Fragment_reporte_no_financiero.newInstance(data[0], data[1]));
                    }
                });
            }
        });


        CViewOption.create(getView().findViewById(R.id.layout_reporte_financiero)).setText(R.string.text_operativas_9).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_MIS_TRANSACCIONES_consultan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "REPORTE FINANCIERO"));
                getContextMenu().setFragment(Fragment_reporte_financiero.newInstance());
            }
        }).setVisible("1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3())
                && "1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7()));

        Button btn_corte_caja = getView().findViewById(R.id.btn_corte_caja);
        btn_corte_caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                corteCaja();
            }
        });

        btn_corte_caja.setVisibility(Tools.userIsFinancial() ? View.VISIBLE : View.GONE);

        dataHelper = new DataHelper(getContext());

        if(!AppPreferences.getLocalTxnUpdate()){
            dataHelper.delAllTransactions();
            AppPreferences.setLocalTxnUpdate(true);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public void transaccionesTAE_DB(){
        //arrayList = dataHelper.getTaeTxr();
        arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.TAE_TXR);

        if (null == arrayList || arrayList.size() == 0) {
            //getContext().showAlert("No existen transacciones registradas.");
            forceRemoteQuery = true;
            transaccionesTAE();
        }
        else
            setDataList(1, null);

    }

    public void transaccionesTAE(){

        final ArrayList list_tae = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetLastTaeTransactions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponse.QPAY_TaeSaleResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_TaeSaleResponse taeResponse = gson.fromJson(json, QPAY_TaeSaleResponse.class);

                        if (taeResponse.getQpay_response().equals("true")) {

                            if(taeResponse.getQpay_object().length == 0){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }

                            for (int i = 0; i < taeResponse.getQpay_object().length; i++)
                                if(taeResponse.getQpay_object()[i].getRspCode().equals("000")){
                                    list_tae.add(taeResponse.getQpay_object()[i]);
                                }

                            if(list_tae.isEmpty()){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }
                            else
                            {
                                if(forceRemoteQuery)
                                {
                                    TaeSale taeSale;
                                    for (int i = list_tae.size() - 1; i >= 0; i--) {
                                        taeSale = (TaeSale) list_tae.get(i);

                                        dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.TAE_TXR,
                                                Tools.getDbDateFormat(taeSale.getCreatedAt()),
                                                "ND",
                                                null,
                                                gson.toJson(taeSale));
                                    }

                                    //20200713 RSB. Una vez se consultan las transacciones remotas se activa la persistencia de las locales
                                    AppPreferences.setLocalTxnTae(true);
                                }
                            }

                            if(Globals.LOCAL_TRANSACTION) {
                                arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.TAE_TXR);

                                if (null == arrayList || arrayList.size() == 0) {

                                    getContext().alert("No existen transacciones registradas.");
                                    return;
                                }
                            }

                            setDataList(1, list_tae);

                        } else {
                            getContextMenu().validaSesion(taeResponse.getQpay_code(), taeResponse.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getLastTaeTransactions(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void transaccionesService_DB(){
        arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.SERVICE_TXR);

        if (null == arrayList || arrayList.size() == 0) {
            forceRemoteQuery = true;
            transaccionesService();
        }
        else
            setDataList(2, null);
    }

    public void transaccionesService(){

        final ArrayList list_tae = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetLastServicesTransactions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponse.QPAY_TaeSaleResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_ServicePaymentResponse taeResponse = gson.fromJson(json, QPAY_ServicePaymentResponse.class);

                        if (taeResponse.getQpay_response().equals("true")) {

                            if(taeResponse.getQpay_object().length == 0){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }

                            for (int i = 0; i < taeResponse.getQpay_object().length; i++)
                                if(taeResponse.getQpay_object()[i].getRspCode().equals("000")) {
                                    list_tae.add(taeResponse.getQpay_object()[i]);
                                }

                            if(list_tae.isEmpty()){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }else{
                                if(forceRemoteQuery) {
                                    ServicePayment servicePayment;

                                    for (int i = list_tae.size() - 1; i >= 0; i--) {
                                        servicePayment = (ServicePayment) list_tae.get(i);

                                        if (servicePayment.getDynamicData() == null) {
                                            String jsonn1 = servicePayment.getQpay_rspBody().replace("\\", "");
                                            servicePayment.setDynamicData(new Gson().fromJson(jsonn1, TaeSale.class).getDynamicData());
                                        }

                                        dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.SERVICE_TXR,
                                                Tools.getDbDateFormat(servicePayment.getCreatedAt()),
                                                "ND",
                                                null,
                                                gson.toJson(servicePayment));
                                    }

                                    //20200713 RSB. Una vez se consultan las transacciones remotas se activa la persistencia de las locales
                                    AppPreferences.setLocalTxnService(true);
                                }
                            }

                            if(Globals.LOCAL_TRANSACTION) {
                                arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.SERVICE_TXR);

                                if (null == arrayList || arrayList.size() == 0) {

                                    getContext().alert("No existen transacciones registradas.");
                                    return;
                                }
                            }

                            setDataList(2, list_tae);

                        } else {
                            getContextMenu().validaSesion(taeResponse.getQpay_code(), "No existen transacciones registradas.");
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getLastServicesTransactions(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void transaccionesPagosQiubo_DB(){
        arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.PQ_TXR);

        if (null == arrayList || arrayList.size() == 0) {
            forceRemoteQuery = true;
            transaccionesPagosQiubo();
        }
    }

    public void transaccionesPagosQiubo(){

        final ArrayList list_pagos = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetTodayTransactionsComplete petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Log.e("JSON", "" + new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {
                        Gson gson = new Gson();
                        String json = new Gson().toJson(result);
                        QPAY_Pagos_Qiubo_Response response = new Gson().fromJson(json, QPAY_Pagos_Qiubo_Response.class);

                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++)
                                if(response.getQpay_object()[i].getRspCode().equals("000"))
                                {
                                    list_pagos.add(response.getQpay_object()[i]);
                                }

                            if(list_pagos.isEmpty()){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }else{
                                if(forceRemoteQuery)
                                {
                                    QPAY_Pago_Qiubo_object qpay_pago_qiubo_object;

                                    for (int i = list_pagos.size() - 1; i >= 0; i--) {
                                        qpay_pago_qiubo_object = (QPAY_Pago_Qiubo_object) list_pagos.get(i);

                                        dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.PQ_TXR,
                                                Tools.getDbDateFormat(qpay_pago_qiubo_object.getCreatedAt()),
                                                "ND",
                                                null,
                                                gson.toJson(qpay_pago_qiubo_object));
                                    }

                                    //20200713 RSB. Una vez se consultan las transacciones remotas se activa la persistencia de las locales
                                    AppPreferences.setLocalTxnQiubo(true);
                                }
                            }

                            if(Globals.LOCAL_TRANSACTION) {
                                arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.PQ_TXR);

                                if (null == arrayList || arrayList.size() == 0) {

                                    getContext().alert("No existen transacciones registradas.");
                                    return;
                                }
                            }

                            //RSB 20191127. Corrección a Transacciones PagosConecta



                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), "No existen transacciones registradas.");
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getTodayTransactionsComplete(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void transaccionesFinanciero_DB(){
        arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR);

        if (null == arrayList || arrayList.size() == 0) {
            forceRemoteQuery = true;
            transaccionesFinanciero();
        }
        else
            setDataList(3, null);

    }

    public void transaccionesFinanciero(){

        final ArrayList list_tae = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_VisaEmvRequest authSeed = new QPAY_VisaEmvRequest();
            authSeed.getCspHeader().setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            authSeed.getCspHeader().setNullObjects();
            authSeed.setCspBody(null);

            IGetLastFinancialTransactions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_VisaResponse response = gson.fromJson(json, QPAY_VisaResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++)
                                if(response.getQpay_object()[i].getCspBody().getTxDate()!=null &&
                                        !response.getQpay_object()[i].getCspBody().getTxDate().trim().equals("null") &&
                                        response.getQpay_object()[i].getCspBody().getMaskedPan()!=null &&
                                        !response.getQpay_object()[i].getCspBody().getMaskedPan().trim().equals("null")) {
                                    list_tae.add(response.getQpay_object()[i]);
                                }

                            if(list_tae.isEmpty()){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }
                            else{
                                if(forceRemoteQuery) {
                                    QPAY_VisaEmvResponse qpay_visaEmvResponse;
                                    for (int i = list_tae.size() - 1; i >= 0; i--) {
                                        qpay_visaEmvResponse = (QPAY_VisaEmvResponse) list_tae.get(i);

                                        if (qpay_visaEmvResponse.getCspHeader().getProductId().trim().equals("13001")) {
                                            if (qpay_visaEmvResponse.getCspHeader().getTxTypeId().equals("140"))
                                                qpay_visaEmvResponse.setName("CANCELACIÓN");
                                            else
                                                qpay_visaEmvResponse.setName("VENTA");
                                        } else if (qpay_visaEmvResponse.getCspHeader().getProductId().trim().equals("13005"))
                                            qpay_visaEmvResponse.setName("RETIRO DE EFECTIVO");

                                        dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR,
                                                Tools.getDbDateFormat(qpay_visaEmvResponse.getCreatedAt()),
                                                "ND",
                                                null,
                                                gson.toJson(qpay_visaEmvResponse));
                                    }

                                    //20200713 RSB. Una vez se consultan las transacciones remotas se activa la persistencia de las locales
                                    AppPreferences.setLocalTxnFinancial(true);
                                }
                            }

                            if(Globals.LOCAL_TRANSACTION) {
                                arrayList = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR);

                                if (null == arrayList || arrayList.size() == 0) {
                                    getContext().alert("No existen transacciones registradas.");
                                    return;
                                }
                            }

                            setDataList(3, list_tae);

                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), "No existen transacciones registradas.");
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getLastFinancialTransactions(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void transaccionesFinancieroCargosAbonos(){

        final ArrayList list_txr = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IDebitAndCreditTxrs petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_CreditAndDebitTxrResponse.QPAY_CreditAndDebitTxrResponseExcluder()).create();
                        String json = gson.toJson(result);//"{\"qpay_response\":\"true\",\"qpay_code\":\"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[]}";//gson.toJson(result);
                        QPAY_CreditAndDebitTxrResponse response = gson.fromJson(json, QPAY_CreditAndDebitTxrResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++) {
                                if(!response.getQpay_object()[i].getOperationType().trim().equals("Migrate to QTC2")
                                        && !response.getQpay_object()[i].getOperationType().trim().equals("Null")
                                        && !response.getQpay_object()[i].getOperationType().trim().equals(""))
                                    list_txr.add(response.getQpay_object()[i]);
                            }

                            if(list_txr.isEmpty()){
                                getContext().alert("No existen transacciones registradas.");
                                return;
                            }

                            setDataList(5, list_txr);

                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), "No existen transacciones registradas.");
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.doGetTransactions(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getQiuboPaymentList(final ArrayList list_pagos, final int tipo){

        final ArrayList list_txr = new ArrayList();

        getContext().loading(true);

        try {

            QPAY_Seed authSeed = new QPAY_Seed();
            authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetQiuboPaymentList petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_QiuboPaymentListResponse.QPAY_QiuboPaymentListExcluder()).create();
                        String json = gson.toJson(result);//"{\"qpay_response\":\"true\",\"qpay_code\":\"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[]}";//gson.toJson(result);
                        QPAY_QiuboPaymentListResponse response = gson.fromJson(json, QPAY_QiuboPaymentListResponse.class);


                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object().length == 0){
                                getContext().alert("En este momento no hay pagos disponibles.");
                                return;
                            }

                            for (int i = 0; i < response.getQpay_object().length; i++) {
                                list_txr.add(response.getQpay_object()[i]);
                            }

                            if(list_txr.isEmpty()){
                                getContext().alert("En este momento no hay pagos disponibles.");
                                return;
                            }

                            setDataList(tipo, list_pagos);

                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), "No existen transacciones registradas.");
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.doGetList(authSeed);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    public void setDataList(Integer tipo, List<Object> data) {

        list = data;

        if(!Globals.LOCAL_TRANSACTION || tipo == 5) {

        } else {

            DataHelper dataHelper = new DataHelper(getContext());
            switch (tipo){
                case 1:
                    list = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.TAE_TXR);
                    break;
                case 2:
                    list = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.SERVICE_TXR);
                    break;
                case 3:
                    list = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR);
                    break;
                case 4:
                    list = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.PQ_TXR);
                    break;
                case 5:
                    list = dataHelper.getLocalTransactions(DataHelper.DB_TXR_TYPE.CYA_TXR);
                    break;
            }

        }


        if(list == null)
            list = new ArrayList();

        Fragment_menu_reportes_detalle.tipo = tipo;
        getContextMenu().setFragment(Fragment_menu_reportes_detalle.newInstance());

    }

    public void corteCaja() {

        Fragment_browser.execute = new IAlertButton() {
            @Override
            public String onText() {
                return "TERMINAR";
            }

            @Override
            public void onClick() {

                getContextMenu().alert("¿Desea hacer un corte de caja en este momento?", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        AppPreferences.setBoxCutTime(Utils.getTime());
                        AppPreferences.setBoxCutDate(Utils.getDay());
                        getContextMenu().initHome();
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
                                getContextMenu().validaSesion(serviceResponse.getQpay_code(), serviceResponse.getQpay_description());
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
                            getContextMenu().validaSesion(serviceResponse.getQpay_code(), serviceResponse.getQpay_description());
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
                            getContextMenu().validaSesion(serviceResponse.getQpay_code(), serviceResponse.getQpay_description());
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