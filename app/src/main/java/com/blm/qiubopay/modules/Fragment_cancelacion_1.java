package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IVisa;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.mitec.CustomMitecTransaction;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import integration.praga.mit.com.apiintegration.ApiIntegrationService;
import integration.praga.mit.com.apiintegration.model.Cancel;
import integration.praga.mit.com.apiintegration.model.TransactionResponse;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_cancelacion_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private QPAY_VisaResponse data;

    private CustomMitecTransaction customMitecTransaction;

    public static Fragment_cancelacion_1 newInstance(Object... data) {
        Fragment_cancelacion_1 fragment = new Fragment_cancelacion_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_cancelacion_1", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_cancelacion_1"), QPAY_VisaResponse.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if(view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_cancelacion_1, container, false);

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
        }).showLogo();

        CApplication.setAnalytics(CApplication.ACTION.CB_CANCELACION_inician);

        ListView list_cancelaciones = view.findViewById(R.id.list_cancelaciones);
        TransaccionesAdapter adapter = new TransaccionesAdapter(context, data.ventas);
        list_cancelaciones.setAdapter(adapter);

        list_cancelaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                context.alert(context.getResources().getString(R.string.desea_cancelar) + " " +Utils.paserCurrency(data.ventas.get(position).getCspBody().getAmt()) + "?", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        if (Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                            cancelarMitTxr(data.ventas.get(position));
                        else
                            cancelar(data.ventas.get(position));
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });

            }
        });

    }

    public class TransaccionesAdapter extends ArrayAdapter<QPAY_VisaEmvResponse> {

        public TransaccionesAdapter(Context context, ArrayList<QPAY_VisaEmvResponse> datos) {
            super(context, 0, datos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_VisaEmvResponse item = getItem(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cancelacion, parent, false);

            TextView text_titulo = (TextView) convertView.findViewById(R.id.text_titulo);
            TextView text_fecha = (TextView) convertView.findViewById(R.id.text_fecha);
            TextView text_transaccion = (TextView) convertView.findViewById(R.id.text_transaccion);

            text_titulo.setText(Utils.paserCurrency(item.getCspBody().getAmt()));
            text_fecha.setText(Utils.formatDate(item.getCspBody().getTxDate()));
            text_transaccion.setText(item.getCspHeader().getRspId());

            return convertView;
        }
    }

    public void cancelar(final QPAY_VisaEmvResponse cancelacion){

        context.loading(true);

        try {

            CApplication.setAnalytics(CApplication.ACTION.CB_CANCELACION_cancelan);

            QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();

            visaEmvRequest.getCspBody().setNullObjects();

            //cspHeader
            visaEmvRequest.getCspHeader().setQpay_entryMode(null);
            visaEmvRequest.getCspHeader().setQpay_tdc(cancelacion.getCspBody().getMaskedPan());
            visaEmvRequest.getCspHeader().setQpay_cardHolder("");
            visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_longitude("0.0");

            //cspBody
            visaEmvRequest.getCspHeader().setProductId("13001");
            visaEmvRequest.getCspHeader().setTxTypeId("140");
            visaEmvRequest.getCspBody().setTxId(cancelacion.getCspHeader().getRspId());
            visaEmvRequest.getCspBody().setAmt("-" + cancelacion.getCspBody().getAmt());

            Logger.i(new Gson().toJson(visaEmvRequest));

            IVisa visaTransaction = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));


                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_VisaResponse visaResponse = gson.fromJson(json, QPAY_VisaResponse.class);

                        getContext().logger(visaResponse);

                        /*if(visaResponse.getQpay_response().equals("true")){
                            context.cargarSaldo(new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    context.setFragment(Fragment_cancelacion_2.newInstance(visaResponse));
                                }
                            });
                        } else
                            context.alert(visaResponse.getQpay_description());*/
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            visaTransaction.getTransact(visaEmvRequest);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    public void cancelarMitTxr(final QPAY_VisaEmvResponse cancelacion){

        context.loading(true);

        try {
            final Cancel tx = new Cancel();

            tx.setBusinessId(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1());
            tx.setSerialNumber("123456789"); // serial del lector
            tx.setAuth(cancelacion.getCspBody().getAuthNum());
            tx.setAmount(Double.parseDouble(cancelacion.getCspBody().getAmt()));
            tx.setFolio(cancelacion.getCspBody().getRrn());

            //tx.setTerminalModel("NEXGO K206"); // modelo del dispositivo lector
            //tx.setVersionTerminal("1.0.0"); // versión del firmware del dispositivo lector
            //tx.setVersion("1.0.0"); // versión de la aplicación de cobro
            //tx.setUser("DON CHEPE"); // usuario que ejecuta la trasacción, p. ej. CAJERO 1


            /****************************************************************************/
            /*                              CANCELACIÓN                                 */
            /****************************************************************************/
            ApiIntegrationService.init(getContext(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
            //ApiIntegrationService.init(getContext(), Globals.BUSINESS_ID, Globals.BASE_URL, Globals.API_KEY, Globals.ENCRIPTION_KEY);

            //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM TOTAL
            TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
            transactionsModel.getFinancialTransactions().setTotales(transactionsModel.getFinancialTransactions().getTotales() + 1);
            AppPreferences.setTodayTransactions(transactionsModel);
            ApiIntegrationService.getInstance().cancel(tx, new ApiIntegrationService.IPragaIntegrationListener<TransactionResponse>() {
                @Override
                public void onSuccess(TransactionResponse transactionResponse) {

                    //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM SUCCESSFUL
                    TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                    transactionsModel.getFinancialTransactions().setExitosos(transactionsModel.getFinancialTransactions().getExitosos() + 1);
                    AppPreferences.setTodayTransactions(transactionsModel);
                    getContext().logger(transactionResponse);

                    context.loading(false);

                    customMitecTransaction = new CustomMitecTransaction();
                    customMitecTransaction.setTransactionResponse(transactionResponse);
                    customMitecTransaction.setQpay_response(""+Double.parseDouble(cancelacion.getCspBody().getAmt()));
                    customMitecTransaction.setInitial_amount("");
                    customMitecTransaction.setQpay_description("Cancelación");

                    if (transactionResponse.isApproved()) {

                        try {
                            Gson gson_txr = new Gson();
                            DataHelper dataHelper = new DataHelper(getContext());
                            dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR, Tools.getTodayDate(), "CANCELATION", gson_txr.toJson(customMitecTransaction),null);
                        }catch (Exception e){
                            Log.e("DATA HELPER", e.getMessage());
                        }

                        context.cargarSaldo(false,false,true, new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                context.setFragment(Fragment_cancelacion_2.newInstance(transactionResponse));
                            }
                        });

                    }
                    else
                    {
                        context.alert("Cancelación Denegada" + "\nCódigo de error: " + transactionResponse.getErrorCode() + "\nDescripción: " + transactionResponse.getErrorDescription());
                        //context.alert(transactionResponse.getErrorDescription());
                    }
                }


                @Override
                public void onError(String s) {
                    context.loading(false);
                    context.alert(R.string.general_error);

                    //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM UNSUCCESSFUL
                    TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                    transactionsModel.getFinancialTransactions().setNoExitosos(transactionsModel.getFinancialTransactions().getNoExitosos() + 1);
                    AppPreferences.setTodayTransactions(transactionsModel);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);
            context.alert(R.string.general_error_catch);

        }
    }

    /*public void cancelarMitTxr(final QPAY_VisaEmvResponse cancelacion){

        context.loading(true);

        try {

            QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();

            visaEmvRequest.getCspBody().setNullObjects();

            //cspHeader
            visaEmvRequest.getCspHeader().setQpay_entryMode(null);
            visaEmvRequest.getCspHeader().setQpay_tdc(cancelacion.getCspBody().getMaskedPan());
            visaEmvRequest.getCspHeader().setQpay_cardHolder("");
            visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_longitude("0.0");

            //cspBody
            visaEmvRequest.getCspHeader().setProductId("13001");
            visaEmvRequest.getCspHeader().setTxTypeId("140");
            visaEmvRequest.getCspBody().setTxId(cancelacion.getCspHeader().getRspId());
            visaEmvRequest.getCspBody().setAmt("-" + cancelacion.getCspBody().getAmt());

            Logger.i(new Gson().toJson(visaEmvRequest));

            IVisa visaTransaction = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));


                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_VisaResponse visaResponse = gson.fromJson(json, QPAY_VisaResponse.class);

                        if(visaResponse.getQpay_response().equals("true")){
                            context.clearFragment();
                            context.home = false;
                            context.setFragment(Fragment_cancelacion_2.newInstance(visaResponse));
                        } else
                            context.alert(visaResponse.getQpay_description());

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            visaTransaction.getTransact(visaEmvRequest);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }*/


}


