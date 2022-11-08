package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IPesitoCredito;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.PesitoCatalogDTO;
import com.blm.qiubopay.models.bimbo.PesitoCatalogResponse;
import com.blm.qiubopay.models.bimbo.PesitoCreditRequest;
import com.blm.qiubopay.models.bimbo.PesitoCreditResponse;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_credito_bimbo_2 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;

    public static SellerUserDTO seller;
    public static PesitoCatalogResponse pesitoCatalog;

    public static Fragment_credito_bimbo_2 newInstance(Object... data) {
        Fragment_credito_bimbo_2 fragment = new Fragment_credito_bimbo_2();
        Bundle args = new Bundle();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_credito_bimbo_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_credito_bimbo_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment() {

        /*

        "sales_center_code": "021739",
          "route_id": 2806,
          "seller_id": 9284169,
          "seller_name": "EFRAIN LUNA IBARRA",
          "retailer_id": 2402093,
          "retailer_name": "MISCELANEA LUPIS",
          "invoice_no": "#42804172020150429",
          "invoice_date": "2020-04-17T05:00:00.000+0000",
          "invoice_amount": 925,
          "receipt_amount": 0,
          "pending_comission": 6,
          "visit": "3",
          "paid_with_out_commision": 3,
          "paid_with_commision": 3,
          "credit_balance": 925,
          "accumulated_paid_amount": 0,

         */

        LinearLayout layout_cuenta = view.findViewById(R.id.layout_cuenta);

        TextView text_nombre = view.findViewById(R.id.text_nombre);
        TextView text_visita = view.findViewById(R.id.text_visita);
        TextView text_saldo = view.findViewById(R.id.text_saldo);

        TextView text_pesito_pendiente = view.findViewById(R.id.text_pesito_pendiente);
        TextView text_no_factura = view.findViewById(R.id.text_no_factura);
        TextView text_fecha_factura = view.findViewById(R.id.text_fecha_factura);
        TextView text_saldo_total = view.findViewById(R.id.text_saldo_total);
        TextView text_monto = view.findViewById(R.id.text_monto);
        TextView text_comision = view.findViewById(R.id.text_comision);

        String[] dias = seller.getSeller_visit_days().split(",");

        String result = "";
        for (String dato : dias)
            result =    dato + " ,";

        result = result.substring(0, result.length() - 1);

        text_nombre.setText(seller.getSeller_name());
        text_visita.setText("Visita: " + result);

        Button btn_request_credti = view.findViewById(R.id.btn_rquest_credit);
        btn_request_credti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestCredito();
            }
        });

        if(pesitoCatalog.getQpay_object() == null || pesitoCatalog.getQpay_object().length == 0 || pesitoCatalog.getQpay_object()[0].getSales_center_code() == null){
            layout_cuenta.setVisibility(View.GONE);
            return;
        } else {
            btn_request_credti.setVisibility(View.GONE);
        }

        try {

            PesitoCatalogDTO pesito = pesitoCatalog.getQpay_object()[0];

            text_pesito_pendiente.setText(Utils.paserCurrency(pesito.getInvoice_amount() + ""));
            text_no_factura.setText(pesito.getInvoice_no() + "");
            text_fecha_factura.setText(pesito.getInvoice_date().substring(0, 10) + "");

            text_saldo_total.setText(Utils.paserCurrency(pesito.getInvoice_amount() + ""));
            text_monto.setText(pesito.getVisit());
            text_comision.setText(Utils.paserCurrency(pesito.getAccumulated_paid_amount() + ""));

        } catch (Exception ex) { }

    }

    public void sendRequestCredito(){

        context.loading(true);

        try {

            PesitoCreditRequest creditRequest = new PesitoCreditRequest();
            creditRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            creditRequest.setRetailer_name(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
            creditRequest.setSeller_id(seller.getSeller_id());
            creditRequest.setSeller_name(seller.getSeller_name());
            creditRequest.setOrganization_id(seller.getOrganization().getOrganization_id());
            creditRequest.setRoute_id(seller.getRoute().getRoute_id());

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            creditRequest.setRequest_pesito_date(date);
            creditRequest.setInvoice_date(date);
            creditRequest.setCeve_code(seller.getSales_center_code());
            creditRequest.setSupervisor_code(seller.getSupervisor().getSupervisor_id());


            CApplication.setAnalytics(CApplication.ACTION.CB_CREDITO_PESITO_solicitan, ImmutableMap.of(CApplication.ACTION.REPARTIDOR.name(), seller.getSeller_id()));

            IPesitoCredito petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                        context.loading(false);

                    } else {

                        context.loading(false);

                        String json = new Gson().toJson(result);
                        PesitoCreditResponse response = new Gson().fromJson(json, PesitoCreditResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            context.alert(R.string.ready_title, R.string.ready, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Entendido";
                                }

                                @Override
                                public void onClick() {
                                    context.initHome();
                                }
                            });

                        } else {
                            context.validaSesion(response.getQpay_code(), response.getQpay_description());
                            context.loading(false);

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            petition.pesitoCredito(creditRequest);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }


}
