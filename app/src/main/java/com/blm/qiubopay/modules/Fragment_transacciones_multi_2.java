package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.model.FINANCIAL_BD_ROW;
import com.blm.qiubopay.database.model.QP_BD_ROW;
import com.blm.qiubopay.database.model.SERVICE_BD_ROW;
import com.blm.qiubopay.database.model.TAE_BD_ROW;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUsersResponse;
import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.models.QPAY_Pagos_Qiubo_Response;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxr;
import com.blm.qiubopay.models.cargos_y_abonos.QPAY_CreditAndDebitTxrResponse;
import com.blm.qiubopay.models.services.QPAY_ServicePaymentResponse;
import com.blm.qiubopay.models.services.ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponse;
import com.blm.qiubopay.models.tae.TaeSale;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.modules.regional.Fragment_pago_regional_menu;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_transacciones_multi_2 extends HFragment {

    //REVISAR LAS LINEAS DE SETFRAGMENT QUE UTILICEN TAE_BD_ROW QUE VAN HACIA TRANSACCIONES 3
    private static final String TAG = "trans_multi_2";

    private static final int TRANSACCIONES_TAE = 1;
    private static final int TRANSACCIONES_SERVICIOS = 2;
    private static final int TRANSACCIONES_FINANCIERAS = 3;
    private static final int TRANSACCIONES_PAGOS_QIUBO = 4;
    private static final int TRANSACCIONES_CARGOS_ABONOS = 5;

    private View view;
    private HActivity context;
    private ArrayList<Object> list;
    private ArrayList<Object> listAllObject;
    private int tipo = 0;
    private QPAY_LinkedUsersResponse dataCajeros;
    private LinkedUser[] cajerosArray;
    private ArrayList<String> cajerosArrayList;
    private ListView list_reportes;
    private CViewEditText spinerCajeros;

    private boolean LOCAL_TRANSACTION = false;

    private int adminId;

    public static Fragment_transacciones_multi_2 newInstance(Object... data) {
        Fragment_transacciones_multi_2 fragment = new Fragment_transacciones_multi_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_transacciones_multi_2_1", new Gson().toJson(data[0]));

        if(data.length > 1)
            args.putInt("Fragment_transacciones_multi_2_2", (int) data[1]);

        if(data.length > 2)
            args.putString("Fragment_transacciones_multi_2_3", new Gson().toJson(data[2]));

        fragment.setArguments(args);
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
        LOCAL_TRANSACTION = false;
        if (getArguments() != null) {
            list = (ArrayList<Object>) new Gson().fromJson(getArguments().getString("Fragment_transacciones_multi_2_1"), List.class);
            listAllObject = list;
            tipo = getArguments().getInt("Fragment_transacciones_multi_2_2");
            dataCajeros = new Gson().fromJson(getArguments().getString("Fragment_transacciones_multi_2_3"), QPAY_LinkedUsersResponse.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_transacciones_multi_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        context.backFragment();
                    }
                });

        //RSB 20191127/20211021. Cambio de etiqueta
        TextView tvTransactionsTitle = getView().findViewById(R.id.tv_transactions_title);
        switch (tipo) {
            case 1: //Transacciones Recargas
                tvTransactionsTitle.setText(R.string.text_operativas_2);
                break;
            case 2: //Transacciones Pago de Servicios
                tvTransactionsTitle.setText(R.string.text_operativas_3);
                break;
            case 3: //Transacciones Financieras
                tvTransactionsTitle.setText(R.string.text_operativas_4);
                break;
            case 4: //Transacciones Pagos Qiubo
                tvTransactionsTitle.setText(R.string.text_operativas_6);
                break;
            case 5://Transacciones de cargos y abonos
                tvTransactionsTitle.setText(R.string.text_operativas_5);
                break;
        }

        final String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();

        // Cajeros

        List<ModelItem> options = new ArrayList();

        int i=0;
        for(String item : buildCajerosArray())
            options.add(new ModelItem(item, "" + i++));

        spinerCajeros = CViewEditText.create(getView().findViewById(R.id.edit_cajero))
                .setRequired(true)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint("Consulta por cajero")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        int position = Integer.parseInt(spinerCajeros.getTag());

                        QPAY_LinkedUser linkedUser = new QPAY_LinkedUser();
                        linkedUser.setQpay_seed(seed);
                        if(position == 1){
                            linkedUser.setQpay_id(0);
                        }
                        else if(position > 1) {
                            linkedUser.setQpay_id(cajerosArray[position-2].getQpay_id());
                        }

                        switch(tipo){
                            case TRANSACCIONES_TAE: transaccionesTAE(linkedUser); break;
                            case TRANSACCIONES_SERVICIOS: transaccionesService(linkedUser); break;
                            case TRANSACCIONES_FINANCIERAS: transaccionesFinanciero(linkedUser); break;
                            case TRANSACCIONES_PAGOS_QIUBO: transaccionesPagosQiubo(linkedUser); break;
                            case TRANSACCIONES_CARGOS_ABONOS: transaccionesFinancieroCargosAbonos(linkedUser); break;
                        }

                    }
                }).setSpinner(options);

        spinerCajeros.setText("TODOS");
        spinerCajeros.setTag("0");

        ArrayList<Object> firstList = list;
        initListAdapter(firstList);

    }

    /**
     *
     */
    private void initListAdapter(ArrayList<Object> listToCharge){

        list = listToCharge;

        list_reportes = view.findViewById(R.id.list_reportes);
        ArrayAdapter adapterTransacciones = new TransaccionesAdapter(context, list);
        list_reportes.setAdapter(adapterTransacciones);
        list_reportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (tipo) {
                    case TRANSACCIONES_TAE: //Transacciones Recargas
                        if(LOCAL_TRANSACTION){
                            TAE_BD_ROW tae_bd_row = (TAE_BD_ROW) list.get(position);
                            context.setFragment(Fragment_transacciones_3.newInstance(tae_bd_row));
                        }else {
                            TaeSale tae = new Gson().fromJson(new Gson().toJson(list.get(position)), TaeSale.class); //#3
                            String json = tae.getQpay_rspBody().replace("\\", "");
                            tae.setDynamicData(new Gson().fromJson(json, TaeSale.class).getDynamicData());

                            context.setFragment(Fragment_transacciones_3.newInstance(tae));
                        }
                        break;
                    case TRANSACCIONES_SERVICIOS: //Transacciones Pago de Servicios
                        if(LOCAL_TRANSACTION){
                            SERVICE_BD_ROW service_bd_row = (SERVICE_BD_ROW) list.get(position);
                            context.setFragment(Fragment_transacciones_4.newInstance(service_bd_row));
                        }
                        else {
                            ServicePayment service = new Gson().fromJson(new Gson().toJson(list.get(position)), ServicePayment.class);
                            String jsonn = service.getQpay_rspBody().replace("\\", "");
                            service.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());

                            service.getDynamicData().setAmount(service.getQpay_amount());

                            service.setProduct(getServiceName(service.getQpay_product()));

                            context.setFragment(Fragment_transacciones_4.newInstance(service));
                        }
                        break;

                    case TRANSACCIONES_FINANCIERAS: //Transacciones Financieras
                        if (LOCAL_TRANSACTION) {
                            FINANCIAL_BD_ROW financial_bd_row = (FINANCIAL_BD_ROW) list.get(position);
                            context.setFragment(Fragment_transacciones_5.newInstance(financial_bd_row));
                        } else {
                            QPAY_VisaEmvResponse visaEmvResponse = new Gson().fromJson(new Gson().toJson(list.get(position)), QPAY_VisaEmvResponse.class);
                            context.setFragment(Fragment_transacciones_5.newInstance(visaEmvResponse));
                        }
                        break;

                    case TRANSACCIONES_PAGOS_QIUBO: //Transacciones PagosConecta
                        if (LOCAL_TRANSACTION) {
                            QP_BD_ROW qp_bd_row = (QP_BD_ROW) list.get(position);
                            context.setFragment(Fragment_transacciones_6.newInstance(qp_bd_row));
                        } else {
                            QPAY_Pago_Qiubo_object pago_qiubo_object = new Gson().fromJson(new Gson().toJson(list.get(position)), QPAY_Pago_Qiubo_object.class);
                            Fragment_transacciones_6.response = pago_qiubo_object;
                            context.setFragment(Fragment_transacciones_6.newInstance());
                        }
                        break;

                    case TRANSACCIONES_CARGOS_ABONOS://Transacciones de cargos y abonos
                        break;
                }

            }
        });

        adapterTransacciones.notifyDataSetChanged();

        context.loading(false);
        getContext().loading(false);
    }


    /**
      * obtiene los cajeros
      * @return
     */
    private ArrayList<String> buildCajerosArray(){
        cajerosArrayList = new ArrayList<String>();

        cajerosArray = dataCajeros.getQpay_object();

        cajerosArrayList.add("TODOS");
        cajerosArrayList.add("MIS TRANSACCIONES");

        if(cajerosArray!=null) {
            for(LinkedUser user:cajerosArray) {
                if(user.getQpay_link_alias()!=null && !user.getQpay_link_alias().isEmpty()) {
                    cajerosArrayList.add(user.getQpay_link_alias());
                } else {
                    cajerosArrayList.add(user.getQpay_name() + " " + user.getQpay_father_surname());
                }
            }
        }

        return cajerosArrayList;
    }


    private void transaccionesTAE(QPAY_LinkedUser linkedUser){

        final ArrayList list_tae = new ArrayList();

        if(linkedUser != null && linkedUser.getQpay_id() != null && linkedUser.getQpay_id() == 0){ // TRX propias
            if(listAllObject != null && !listAllObject.isEmpty()){
                for (Object taeSaleO: listAllObject ) {
                    TaeSale tae = new Gson().fromJson(new Gson().toJson(taeSaleO), TaeSale.class); //#3
                    String json = tae.getQpay_rspBody().replace("\\", "");
                    tae.setDynamicData(new Gson().fromJson(json, TaeSale.class).getDynamicData());
                    if(tae.getQpay_administrator_id() == null){
                        list_tae.add(tae);
                    }
                }
            }

            if (list_tae.isEmpty()) {
                context.alert(R.string.texto_sin_transacciones);
            }
            initListAdapter(list_tae);

            context.loading(false);

        }else {

            context.loading(true);

            try {

                IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                    @Override
                    public void onConnectionEnded(Object result) {

                        context.closeLoading();

                        if (result instanceof ErrorResponse) {
                            //Se muestra el error.

                            context.alert(R.string.general_error);

                        } else {

                            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponse.QPAY_TaeSaleResponseExcluder()).create();
                            String json = gson.toJson(result);
                            QPAY_TaeSaleResponse taeResponse = gson.fromJson(json, QPAY_TaeSaleResponse.class);

                            if (taeResponse.getQpay_response().equals("true")) {

                                if (taeResponse.getQpay_object().length == 0) {
                                    context.alert(R.string.texto_sin_transacciones);
                                } else {
                                    for (int i = 0; i < taeResponse.getQpay_object().length; i++)
                                        if (taeResponse.getQpay_object()[i].getRspCode().equals("000")) {
                                            list_tae.add(taeResponse.getQpay_object()[i]);
                                        }

                                    if (list_tae.isEmpty()) {
                                        context.alert(R.string.texto_sin_transacciones);
                                    }
                                }

                                //20200804 RSB, Imp. Ordenar por fecha
                                orderByDate(list_tae);

                                initListAdapter(list_tae);

                            } else {
                                if (taeResponse.getQpay_code().equals("017")
                                        || taeResponse.getQpay_code().equals("018")
                                        || taeResponse.getQpay_code().equals("019")
                                        || taeResponse.getQpay_code().equals("020")) {
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
                                
                                } else if (taeResponse.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                    //context.alertAEONBlockedUser();
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
    }

    private void transaccionesService(QPAY_LinkedUser linkedUser){

        final ArrayList list_tae = new ArrayList();

        context.loading(true);
        if(linkedUser != null && linkedUser.getQpay_id() != null && linkedUser.getQpay_id() == 0){ // TRX propias

            if(listAllObject != null && !listAllObject.isEmpty()){
                for (Object taeSaleO: listAllObject ) {
                    ServicePayment service = new Gson().fromJson(new Gson().toJson(taeSaleO), ServicePayment.class);
                    String jsonn = service.getQpay_rspBody().replace("\\", "");
                    service.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());

                    service.getDynamicData().setAmount(service.getQpay_amount());

                    service.setProduct(getServiceName(service.getQpay_product()));

                    if(service.getQpay_administrator_id() == null){
                        list_tae.add(service);
                    }
                }
            }

            if (list_tae.isEmpty()) {
                context.alert(R.string.texto_sin_transacciones);
            }

            //20200804 RSB, Imp. Ordenar por fecha
            orderByDate(list_tae);

            initListAdapter(list_tae);

            context.loading(false);

        } else {

            try {

                IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                    @Override
                    public void onConnectionEnded(Object result) {

                        context.closeLoading();

                        if (result instanceof ErrorResponse) {
                            //Se muestra el error.
                            context.alert(R.string.general_error);

                        } else {

                            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponse.QPAY_TaeSaleResponseExcluder()).create();
                            String json = gson.toJson(result);
                            QPAY_ServicePaymentResponse taeResponse = gson.fromJson(json, QPAY_ServicePaymentResponse.class);

                            if (taeResponse.getQpay_response().equals("true")) {

                                if (taeResponse.getQpay_object().length == 0) {
                                    context.alert(R.string.texto_sin_transacciones);
                                } else {
                                    for (int i = 0; i < taeResponse.getQpay_object().length; i++)
                                        if (taeResponse.getQpay_object()[i].getRspCode().equals("000"))
                                            list_tae.add(taeResponse.getQpay_object()[i]);

                                    if (list_tae.isEmpty()) {
                                        context.alert(R.string.texto_sin_transacciones);
                                    }
                                }

                                initListAdapter(list_tae);

                            } else {
                                if (taeResponse.getQpay_code().equals("017")
                                        || taeResponse.getQpay_code().equals("018")
                                        || taeResponse.getQpay_code().equals("019")
                                        || taeResponse.getQpay_code().equals("020")) {
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
                                    
                                } else if (taeResponse.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                    //context.alertAEONBlockedUser();
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
    }

    private void transaccionesFinanciero(QPAY_LinkedUser linkedUser){

        final ArrayList list_tae = new ArrayList();

        context.loading(true);
        if(linkedUser != null && linkedUser.getQpay_id() != null && linkedUser.getQpay_id() == 0){ // TRX propias

            if(listAllObject != null && !listAllObject.isEmpty()){
                for (Object taeSaleO: listAllObject ) {
                    QPAY_VisaEmvResponse visaEmvResponse = new Gson().fromJson(new Gson().toJson(taeSaleO), QPAY_VisaEmvResponse.class);
                    if(visaEmvResponse.getQpay_administrator_id() == null){
                        list_tae.add(visaEmvResponse);
                    }
                }
            }

            if (list_tae.isEmpty()) {
                context.alert(R.string.texto_sin_transacciones);
            }

            initListAdapter(list_tae);
            context.loading(false);

        }else {
            try {

                IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                    @Override
                    public void onConnectionEnded(Object result) {

                        context.closeLoading();

                        if (result instanceof ErrorResponse) {
                            //Se muestra el error.
                            context.loading(false);
                            context.alert(R.string.general_error);

                        } else {

                            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                            String json = gson.toJson(result);
                            QPAY_VisaResponse response = gson.fromJson(json, QPAY_VisaResponse.class);


                            if (response.getQpay_response().equals("true")) {

                                if (response.getQpay_object().length == 0) {
                                    context.alert(R.string.texto_sin_transacciones);
                                } else {
                                    for (int i = 0; i < response.getQpay_object().length; i++)
                                        if (response.getQpay_object()[i].getCspBody().getTxDate() != null &&
                                                !response.getQpay_object()[i].getCspBody().getTxDate().trim().equals("null") &&
                                                response.getQpay_object()[i].getCspBody().getMaskedPan() != null &&
                                                !response.getQpay_object()[i].getCspBody().getMaskedPan().trim().equals("null"))
                                            list_tae.add(response.getQpay_object()[i]);

                                    if (list_tae.isEmpty()) {
                                        context.alert(R.string.texto_sin_transacciones);
                                    }
                                }

                                //20200804 RSB, Imp. Ordenar por fecha
                                orderByDate(list_tae);

                                initListAdapter(list_tae);

                            } else {
                                if (response.getQpay_code().equals("017")
                                        || response.getQpay_code().equals("018")
                                        || response.getQpay_code().equals("019")
                                        || response.getQpay_code().equals("020")) {
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
                                 
                                } else if (response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                    //context.alertAEONBlockedUser();
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
    }

    private void transaccionesPagosQiubo(QPAY_LinkedUser linkedUser){

        final ArrayList list_pagos = new ArrayList();

        context.loading(true);

        if(linkedUser != null && linkedUser.getQpay_id() != null && linkedUser.getQpay_id() == 0){ // TRX propias

            if(listAllObject != null && !listAllObject.isEmpty()){
                for (Object taeSaleO: listAllObject ) {
                    QPAY_Pago_Qiubo_object pago_qiubo_object = new Gson().fromJson(new Gson().toJson(taeSaleO), QPAY_Pago_Qiubo_object.class);
                    if(pago_qiubo_object.getQpay_administrator_id() == null){
                        list_pagos.add(pago_qiubo_object);
                    }
                }
            }

            if (list_pagos.isEmpty()) {
                context.alert(R.string.texto_sin_transacciones);
            }

            initListAdapter(list_pagos);
            context.loading(false);

        }else {
            try {

                IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                    @Override
                    public void onConnectionEnded(Object result) {

                        //Log.d("JSON", "" + new Gson().toJson(result));

                        context.closeLoading();

                        if (result instanceof ErrorResponse) {
                            //Se muestra el error.
                            context.alert(R.string.general_error);

                        } else {

                            String json = new Gson().toJson(result);
                            QPAY_Pagos_Qiubo_Response response = new Gson().fromJson(json, QPAY_Pagos_Qiubo_Response.class);

                            if (response.getQpay_response().equals("true")) {

                                if (response.getQpay_object().length == 0) {
                                    context.alert(R.string.texto_sin_transacciones);
                                } else {
                                    for (int i = 0; i < response.getQpay_object().length; i++)
                                        if (response.getQpay_object()[i].getRspCode().equals("000"))
                                            list_pagos.add(response.getQpay_object()[i]);

                                    if (list_pagos.isEmpty()) {
                                        context.alert(R.string.texto_sin_transacciones);
                                    }
                                }

                                //RSB 20191127. Corrección a Transacciones PagosConecta
                                //REVISAR
                               /* if (Fragment_pago_qiubo_1.objectList != null) {

                                    //20200804 RSB, Imp. Ordenar por fecha
                                    orderByDate(list_pagos);

                                    initListAdapter(list_pagos);
                                }*/

                            } else {
                                if (response.getQpay_code().equals("017")
                                        || response.getQpay_code().equals("018")
                                        || response.getQpay_code().equals("019")
                                        || response.getQpay_code().equals("020")) {
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

                                } else if (response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                   // context.alertAEONBlockedUser();
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
    }

    private void transaccionesFinancieroCargosAbonos(QPAY_LinkedUser linkedUser){

        final ArrayList list_txr = new ArrayList();

        context.loading(true);

        if(linkedUser != null && linkedUser.getQpay_id() != null && linkedUser.getQpay_id() == 0){ // TRX propias
            if(listAllObject != null && !listAllObject.isEmpty()){
                for (Object taeSaleO: listAllObject ) {
                    QPAY_CreditAndDebitTxr object = new Gson().fromJson(new Gson().toJson(taeSaleO), QPAY_CreditAndDebitTxr.class);
                    if(object.getQpay_administrator_id() == null){
                        list_txr.add(object);
                    }
                }
            }

            if (list_txr.isEmpty()) {
                context.alert(R.string.texto_sin_transacciones);
            }
            initListAdapter(list_txr);
            context.loading(false);

        }else {

            try {

                IMultiUserListener petition = new WSHelper(new IGenericConnectionDelegate() {
                    @Override
                    public void onConnectionEnded(Object result) {

                        context.closeLoading();

                        if (result instanceof ErrorResponse) {
                            //Se muestra el error
                            context.alert(R.string.general_error);

                        } else {

                            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_CreditAndDebitTxrResponse.QPAY_CreditAndDebitTxrResponseExcluder()).create();
                            String json = gson.toJson(result);//"{\"qpay_response\":\"true\",\"qpay_code\":\"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[]}";//gson.toJson(result);
                            QPAY_CreditAndDebitTxrResponse response = gson.fromJson(json, QPAY_CreditAndDebitTxrResponse.class);


                            if (response.getQpay_response().equals("true")) {

                                if (response.getQpay_object().length == 0) {
                                    context.alert(R.string.texto_sin_transacciones);
                                } else {
                                    for (int i = 0; i < response.getQpay_object().length; i++) {
                                        if (!response.getQpay_object()[i].getOperationType().trim().equals("Migrate to QTC2")
                                                && !response.getQpay_object()[i].getOperationType().trim().equals("Null")
                                                && !response.getQpay_object()[i].getOperationType().trim().equals(""))
                                            list_txr.add(response.getQpay_object()[i]);
                                    }

                                    if (list_txr.isEmpty()) {
                                        context.alert(R.string.texto_sin_transacciones);
                                    }
                                }

                                //20200804 RSB, Imp. Ordenar por fecha
                                orderByDate(list_txr);

                                initListAdapter(list_txr);

                            } else {
                                if (response.getQpay_code().equals("017")
                                        || response.getQpay_code().equals("018")
                                        || response.getQpay_code().equals("019")
                                        || response.getQpay_code().equals("020")) {
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

                                } else if (response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                    //context.alertAEONBlockedUser();
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
    }


    /**
     * Clase adapter de transacciones
     */
    public class TransaccionesAdapter extends ArrayAdapter<Object> {

        public TransaccionesAdapter(Context context, List<Object> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaccion, parent, false);

            TextView text_titulo = (TextView) convertView.findViewById(R.id.text_titulo);
            TextView text_fecha = (TextView) convertView.findViewById(R.id.text_fecha);
            TextView text_amount = (TextView) convertView.findViewById(R.id.text_amount);
            ImageView img_operation = convertView.findViewById(R.id.img_operation);

            switch (tipo){
                case 1: //Transacciones TAE

                    String carrier = "";

                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_tiempo_aire_60_x_60));

                    if (Globals.LOCAL_TRANSACTION) {
                        TAE_BD_ROW tae_bd_row = (TAE_BD_ROW) getItem(position);
                        if(null != tae_bd_row.getResponse1()) {
                            carrier = getCarrierName(tae_bd_row.getCarrier());

                            context.logger(tae_bd_row);

                            text_titulo.setText(carrier);
                            text_amount.setText(Utils.paserCurrency(tae_bd_row.getResponse1().getQpay_object()[0].getAmount()));
                            text_fecha.setText(Utils.formatDate(tae_bd_row.getResponse1().getCreatedAt()));

                        }else if(null != tae_bd_row.getResponse2()) {
                            TaeSale tae = tae_bd_row.getResponse2();

                            if (tae.getDynamicData() == null) {
                                String jsonn = tae.getQpay_rspBody().replace("\\", "");
                                tae.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());

                                tae_bd_row.setResponse2(tae);
                            }

                            context.logger(tae);

                            carrier = getCarrierName(tae.getQpay_carrier());

                            text_titulo.setText(carrier);
                            text_amount.setText(Utils.paserCurrency(tae.getQpay_amount()));
                            text_fecha.setText(Utils.formatDate(tae.getResponseAt()));
                        }
                    }
                    else {
                        TaeSale tae = new Gson().fromJson(new Gson().toJson(getItem(position)), TaeSale.class);

                        if (tae.getDynamicData() == null) {
                            String jsonn = tae.getQpay_rspBody().replace("\\", "");
                            tae.setDynamicData(new Gson().fromJson(jsonn, TaeSale.class).getDynamicData());
                        }

                        context.logger(tae);

                        carrier = getCarrierName(tae.getQpay_carrier());

                        text_titulo.setText(carrier);
                        text_amount.setText(Utils.paserCurrency(tae.getQpay_amount()));
                        text_fecha.setText(Utils.formatDate(tae.getResponseAt()));
                    }
                    break;
                case 2: //Transacciones pagos de servicios
                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_pago_servicios));

                    if (Globals.LOCAL_TRANSACTION) {
                        SERVICE_BD_ROW service_bd_row = (SERVICE_BD_ROW) getItem(position);
                        if(null != service_bd_row.getResponse1()) {
                            carrier = getServiceName(service_bd_row.getType());

                            text_titulo.setText(carrier);
                            text_amount.setText(Utils.paserCurrency(service_bd_row.getResponse1().getQpay_object()[0].getAmount()));
                            text_fecha.setText(Utils.formatDate(service_bd_row.getResponse1().getCreatedAt()));
                        }else if(null != service_bd_row.getResponse2()) {
                            ServicePayment service = service_bd_row.getResponse2();

                            if (service.getDynamicData() == null) {
                                String jsonn1 = service.getQpay_rspBody().replace("\\", "");
                                service.setDynamicData(new Gson().fromJson(jsonn1, TaeSale.class).getDynamicData());

                                service_bd_row.setResponse2(service);
                            }

                            String service_product = getServiceName(service.getQpay_product());

                            text_titulo.setText(service_product);
                            text_amount.setText(Utils.paserCurrency(service.getQpay_amount()));

                            if (service.getDynamicData().getTimestamp() == null)
                                service.getDynamicData().setTimestamp(Utils.formatDate(service.getDynamicData().getTimestamp()));

                            text_fecha.setText(Utils.formatDate(service.getResponseAt()));
                        }
                    } else {
                        ServicePayment service = new Gson().fromJson(new Gson().toJson(getItem(position)), ServicePayment.class);

                        Logger.e(new Gson().toJson(service));

                        if (service.getDynamicData() == null) {
                            String jsonn1 = service.getQpay_rspBody().replace("\\", "");
                            service.setDynamicData(new Gson().fromJson(jsonn1, TaeSale.class).getDynamicData());
                        }

                        String service_product = getServiceName(service.getQpay_product());

                        String monto = service.getQpay_amount() != null ? service.getQpay_amount() : "000";
                        //QFix. 210510 RSB. Dado que el monto para netwey regresa en entero y no en base 100 o double como todos los demas se tiene que parsear
                        if(service.getQpay_product().compareTo("14")==0){
                            monto = monto.concat("00");
                        }
                        monto = getMonto(monto);

                        String comision = service.getDynamicData().getFlatFee() != null ? service.getDynamicData().getFlatFee() : "000";
                        comision = getMonto(comision);
                        String total = (Double.parseDouble(monto) + ""); //+ Double.parseDouble(comision)) + "";

                        text_titulo.setText(service_product);
                        text_amount.setText(Utils.paserCurrency(total));

                        if (service.getDynamicData().getTimestamp() == null)
                            service.getDynamicData().setTimestamp(Utils.formatDate(service.getDynamicData().getTimestamp()));

                        text_fecha.setText(Utils.formatDate(service.getResponseAt()));
                    }

                    break;
                case 3: //Transacciones Pagos con tarjeta
                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_cobro_con_tarjeta));

                    String name = "";

                    if (Globals.LOCAL_TRANSACTION) {
                        FINANCIAL_BD_ROW financial_bd_row = (FINANCIAL_BD_ROW) getItem(position);
                        if(null != financial_bd_row.getResponse1()) {
                            if (financial_bd_row.getType().equals("SELL"))
                                name = "VENTA";
                            else if (financial_bd_row.getType().equals("CASHBACK"))
                                name = "RETIRO DE EFECTIVO";
                            else if (financial_bd_row.getType().equals("CANCELATION"))
                                name = "CANCELACIÓN";

                            text_titulo.setText(name);
                            text_amount.setText(Utils.paserCurrency(financial_bd_row.getResponse1().getTransactionResponse().getAmount().toString()));
                            text_fecha.setText(Utils.formatDate(financial_bd_row.getResponse1().getTransactionResponse().getDate() + " " + financial_bd_row.getResponse1().getTransactionResponse().getTime()));
                        }else if(null != financial_bd_row.getResponse2()) {
                            QPAY_VisaEmvResponse visaEmvResponse = financial_bd_row.getResponse2();

                            text_titulo.setText(visaEmvResponse.getName());
                            text_amount.setText(Utils.paserCurrency(visaEmvResponse.getCspBody().getAmt()));
                            text_fecha.setText(Utils.formatDate(visaEmvResponse.getCspBody().getTxDate()));
                        }
                    } else {
                        QPAY_VisaEmvResponse visaEmvResponse = new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_VisaEmvResponse.class);

                        if (visaEmvResponse.getCspHeader().getProductId().trim().equals("13001")) {
                            if (visaEmvResponse.getCspHeader().getTxTypeId().equals("140"))
                                name = "CANCELACIÓN";
                            else
                                name = "VENTA";
                        } else if (visaEmvResponse.getCspHeader().getProductId().trim().equals("13005"))
                            name = "RETIRO DE EFECTIVO";

                        visaEmvResponse.setName(name);

                        list.set(position, visaEmvResponse);

                        text_titulo.setText(name);
                        text_amount.setText(Utils.paserCurrency(visaEmvResponse.getCspBody().getAmt()));
                        text_fecha.setText(Utils.formatDate(visaEmvResponse.getCspBody().getTxDate()));
                    }
                    break;
                case 4: //Transacciones pagosConecta
                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_pagos_regionales));

                    if (Globals.LOCAL_TRANSACTION) {
                        QP_BD_ROW qp_bd_row = (QP_BD_ROW) getItem(position);

                        if (null != qp_bd_row.getResponse1()) {

                            //QPAY_Pago_Qiubo_object pago_qiubo_object = qp_bd_row.getResponse2();//new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_Pago_Qiubo_object.class);

                            String producto = Fragment_pago_regional_menu.getNameProduct(qp_bd_row.getResponse1().getClaveQiubo().substring(0, 4));

                            producto = producto.isEmpty() ? Fragment_pago_regional_menu.getNameProduct(qp_bd_row.getResponse1().getClaveQiubo().substring(0, 3)) : producto;

                            text_titulo.setText(producto);
                            text_amount.setText(Utils.paserCurrency(qp_bd_row.getResponse1().getAmount()));
                            text_fecha.setText(Utils.formatDate(qp_bd_row.getResponse1().getTimestamp()));
                        }else if(null != qp_bd_row.getResponse2()) {

                            QPAY_Pago_Qiubo_object pago_qiubo_object = qp_bd_row.getResponse2();//new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_Pago_Qiubo_object.class);

                            String producto = Fragment_pago_regional_menu.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 4));

                            producto = producto.isEmpty() ? Fragment_pago_regional_menu.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 3)) : producto;
                            text_titulo.setText(producto);
                            text_amount.setText(Utils.paserCurrency(pago_qiubo_object.getAmount()));

                            text_fecha.setText(Utils.formatDate(pago_qiubo_object.getTimestamp()));
                        }
                    }else {

                        QPAY_Pago_Qiubo_object pago_qiubo_object = new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_Pago_Qiubo_object.class);

                        String producto = Fragment_pago_regional_menu.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 3));

                        //producto = producto.isEmpty() ? Fragment_pago_qiubo_1.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0, 3)) : producto;

                        text_titulo.setText(producto);
                        text_amount.setText(Utils.paserCurrency(pago_qiubo_object.getAmount()));
                        text_fecha.setText(Utils.formatDate(pago_qiubo_object.getTimestamp()));
                    }
                    break;
                case 5: //Transacciones de cargos y abonos

                    img_operation.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icons_pagos_filled));

                    QPAY_CreditAndDebitTxr object = new Gson().fromJson(new Gson().toJson(getItem(position)), QPAY_CreditAndDebitTxr.class);

                    String product = object.getOperationType();//Fragment_pago_qiubo_1.getNameProduct(object.getClaveQiubo().substring(0,4));

                    //producto = product.isEmpty() ? Fragment_pago_qiubo_1.getNameProduct(pago_qiubo_object.getClaveQiubo().substring(0,3)) : producto;

                    text_titulo.setText(product);
                    text_amount.setText(Utils.paserCurrency(object.getAmount()));
                    text_fecha.setText(Utils.formatDate(object.getResponseAt()));

                    break;
            }


            return convertView;
        }

        /**
         * Metodo para editar monto sea en decimal o base 100
         * @param monto
         * @return
         */
        private String getMonto(String monto) {

            monto = monto.replace(",",".");
            String[] montoArray = monto.split("\\.");
            if(montoArray.length>1){
                //Significa que es decimal, se deja pasar tal cual
            } else {
                //Significa que es base 100, pero en lugar de quitar los ultimos dos dígitos ahora
                //le agregaremos un punto a la cadena y así respetamos CFE
                if(monto.length()<3){
                    monto = String.format("%03d", Integer.valueOf(monto));
                }
                StringBuffer str = new StringBuffer(monto);
                str.insert(monto.length()-2,".");
                monto = str.toString();

            }

            return monto;

        }

    }

    private String getCarrierName(String carrier){
        String back = "";

        switch (carrier){
            case Globals.ID_TELCEL:
                back = getString(R.string.text_telcel).toUpperCase();
                break;
            case Globals.ID_TELCEL_DATOS:
                back = getString(R.string.text_telcel_datos).toUpperCase();
                break;
            case Globals.ID_TELCEL_INTERNET:
                back = getString(R.string.text_telcel_internet).toUpperCase();
                break;
            case Globals.ID_MOVISTAR:
                back = getString(R.string.text_movistar).toUpperCase();
                break;
            case Globals.ID_IUSACELL:
                back = getString(R.string.text_att).toUpperCase();
                break;
            case Globals.ID_UNEFON:
                back = getString(R.string.text_unefon).toUpperCase();
                break;
            case Globals.ID_VIRGIN_MOBILE:
                back = getString(R.string.text_virgin).toUpperCase();;
                break;
            case Globals.ID_MAS_RECARGA:
                back = getString(R.string.text_mas_recarga).toUpperCase();;
                break;
            case Globals.ID_TUENTI:
                back = getString(R.string.text_tuenti).toUpperCase();
                break;
            case Globals.ID_FREEDOM_POP:
                back = getString(R.string.text_freedom).toUpperCase();
                break;
            case Globals.ID_ALO:
                back = getString(R.string.text_alo).toUpperCase();
                break;
            case Globals.ID_BUENOCELL:
                back = getString(R.string.text_bueno_cell).toUpperCase();
                break;
            //210909 RSB. MVNO
            case Globals.ID_MI_MOVIL:
                back = getString(R.string.text_mi_movil).toUpperCase();
                break;
            case Globals.ID_TURBORED_TAE:
                back = getString(R.string.text_turbored).toUpperCase();
                break;
            case Globals.ID_TURBORED_DATOS:
                back = getString(R.string.text_turbored_datos).toUpperCase();
                break;
            case Globals.ID_SPACE:
                back = getString(R.string.text_space).toUpperCase();
                break;
            case Globals.ID_DIRI:
                back = getString(R.string.text_diri).toUpperCase();
                break;
            case Globals.ID_PILLOFON:
                back = getString(R.string.text_pillofon).toUpperCase();
                break;
            case Globals.ID_FOBO:
                back = getString(R.string.text_fobo).toUpperCase();
                break;
        }

        return back;
    }

    private String getServiceName(String service){
        String serviceName = "";
        switch (service){
            case Globals.TELMEX_ID:
                serviceName = getString(R.string.text_telmex).toUpperCase();
                break;
            case Globals.SKY_ID:
                serviceName = getString(R.string.text_sky).toUpperCase();
                break;
            case Globals.CFE_ID:
                serviceName = getString(R.string.text_cfe).toUpperCase();
                break;
            case Globals.DISH_ID:
                serviceName = getString(R.string.text_dish).toUpperCase();
                break;
            case Globals.TELEVIA_ID:
                serviceName = getString(R.string.text_televia).toUpperCase();
                break;
            case Globals.NATURGY_ID:
                serviceName = getString(R.string.text_naturgy).toUpperCase();
                break;
            case Globals.IZZI_ID:
                serviceName = getString(R.string.text_izzi).toUpperCase();
                break;
            case Globals.MEGACABLE_ID:
                serviceName = getString(R.string.text_megacable).toUpperCase();
                break;
            case Globals.PASE_URBANO_ID:
                serviceName = getString(R.string.text_pase_urbano).toUpperCase();
                break;
            case Globals.TOTALPLAY_ID:
                serviceName = getString(R.string.text_totalplay).toUpperCase();
                break;
            case Globals.STARTV_ID:
                serviceName = getString(R.string.text_star_tv).toUpperCase();
                break;
            case Globals.CEA_QRO_ID:
                serviceName = getString(R.string.text_cea_qro).toUpperCase();
                break;
            case Globals.NETWAY_ID:
                serviceName = getString(R.string.text_netwey).toUpperCase();
                break;
            case Globals.VEOLIA_ID:
                serviceName = getString(R.string.text_veolia).toUpperCase();
                break;
            case Globals.OPDM_ID:
                serviceName = getString(R.string.text_opdm).toUpperCase();
                break;
            case Globals.POST_ATT_ID:
                serviceName = getString(R.string.text_post_att).toUpperCase();
                break;
            case Globals.POST_MOVISTAR_ID:
                serviceName = getString(R.string.text_post_movistar).toUpperCase();
                break;
            case Globals.SACMEX_ID:
                serviceName = getString(R.string.text_sacmex).toUpperCase();
                break;
            case Globals.GOB_MX_ID:
                serviceName = getString(R.string.text_gob_mx).toUpperCase();
                break;
        }

        return serviceName;
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


}
