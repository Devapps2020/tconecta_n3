package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.cardview.widget.CardView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.modules.sobregiro.Fragment_linea_sobregiro;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IBalance;
import com.blm.qiubopay.listeners.IFinancialVas;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Balance;
import com.blm.qiubopay.models.QPAY_BalanceResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasPetition;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasResponse;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.modules.servicio.Fragment_pago_servicio_menu;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;

import mx.devapps.utils.interfaces.IFunction;

public class Fragment_depositos_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;

    private WebView webView;

    public static Fragment_depositos_1 newInstance(Object... data) {
        Fragment_depositos_1 fragment = new Fragment_depositos_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_depositos_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
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

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_depositos_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if(view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_depositos_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_ABONO_SALDO_ASF00);

        CardView card_deposito = view.findViewById(R.id.card_deposito);
        CardView card_bimbo = view.findViewById(R.id.card_bimbo);
        CardView card_prestamos = view.findViewById(R.id.card_prestamos);
        CardView card_deposito_tarjeta = view.findViewById(R.id.card_deposito_tarjeta);
        CardView card_linea_sobregiro = view.findViewById(R.id.card_linea_sobregiro);
        //View view_vas_financiero = view.findViewById(R.id.view_vas_financiero);
        CardView card_vas_financiero = view.findViewById(R.id.card_vas_financiero);

        if(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id() == null ||
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id().isEmpty())
            card_bimbo.setVisibility(View.GONE);

        card_bimbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //20200416 RSB. MultiUser MultiDevice. Se valida si es cajero
                if(AppPreferences.isCashier() && !context.validateUserOperation(false)) {
                    context.alert(R.string.cajero_sin_permiso);
                } else {
                    context.setFragment(Fragment_depositos_2.newInstance());
                }

            }
        });

        card_deposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.OPCION,  ImmutableMap.of(CApplication.ACTION.OPCION.name(), "RecolecciónBimbo"));
                context.setFragment(Fragment_depositos_3.newInstance());
            }
        });

        //20200416 RSB. MultiUser MultiDevice.
        if(AppPreferences.isCashier()) {
            card_prestamos.setVisibility(View.GONE);
            //view_vas_financiero.setVisibility(View.GONE);
            card_vas_financiero.setVisibility(View.GONE);
        }

        if(Tools.userIsOnlyVAS()){
            //view_vas_financiero.setVisibility(View.GONE);
            card_vas_financiero.setVisibility(View.GONE);
        }

        //20200813 RSB. Validar VAS con Financiero
        if(!AppPreferences.isVASFinancieroActive()){
            //view_vas_financiero.setVisibility(View.GONE);
            card_vas_financiero.setVisibility(View.GONE);
        }

        card_prestamos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(Fragment_linea_sobregiro.newInstance());
            }
        });

        //210929 RSB. eCommerce parametrizable
        if(AppPreferences.getUserProfile().getQpay_object()[0].getEcommerce_activation() != null &&
                AppPreferences.getUserProfile().getQpay_object()[0].getEcommerce_activation().equals("1"))
            card_deposito_tarjeta.setVisibility(View.VISIBLE);

        card_deposito_tarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(Fragment_depositos_4.newInstance(02.00));
            }
        });

        card_vas_financiero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVas();;
            }
        });

        if(data != null)
            checkVas();
    }

    private void checkVas(){
        checkFinancialVas(new IFunction() {
            @Override
            public void execute(Object[] data) {
                if(null != data[0]){
                    QPAY_FinancialVasResponse response = (QPAY_FinancialVasResponse)data[0];
                    if(response.getQpay_response().equals("true")) {
                        context.setFragment(Fragment_depositos_7.newInstance(response));//new Gson().toJson(response)));
                    } else {
                        context.alert(response.getQpay_description());
                    }
                }
            }
        });
    }

    public void cargarComisionVariable(final IFunction function){

        context.loading(true);

        try {

            QPAY_Balance qpay_balance = new QPAY_Balance();

            qpay_balance.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IBalance balance = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {
                        QPAY_BalanceResponse balanceResponse = (QPAY_BalanceResponse) result;

                        if (balanceResponse.getQpay_response().equals("true")) {

                        } else {
                            AppPreferences.setKinetoBalance("Saldo $0.00");
                        }

                        if(function != null)
                            function.execute(05.00);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    //spinnerDialog.hideProgress();
                    //spinnerDialog.showAlert("Error " + ((ErrorResponse) result).getInternalCode() + "\n" + ((ErrorResponse) result).getMessage());
                }

            }, context);

            balance.getBalance(qpay_balance);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    public void checkFinancialVas(final IFunction function){

        context.loading(true);

        try {

            QPAY_FinancialVasPetition object = new QPAY_FinancialVasPetition();

            object.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            object.setQpay_amount("0");

            IFinancialVas petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_FinancialVasResponse.QPAY_FinancialVasResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_FinancialVasResponse response = gson.fromJson(json, QPAY_FinancialVasResponse.class);

                        if(function != null)
                            function.execute(response);

                        /*if (response.getQpay_response().equals("true")) {
                            if(function != null)
                                function.execute(true);
                        } else {
                            if(function != null)
                                function.execute(false);
                        }*/
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            petition.checkIfFinancialVasIsAvailable(object);
            //petition.processFinancialVas(object);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

}

