package com.blm.qiubopay.modules.servicio;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ITaeSale;
import com.blm.qiubopay.listeners.iServicePayment;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_confirmacion;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_ticket;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_servicio_confirma#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_servicio_confirma extends HFragment implements IMenuContext {

    private static final String TAG = "servicio_confirmacion";

    private CompaniaDTO companiaDTO;
    private QPAY_ServicePayment servicePayment;

    Button btn_continuar;

    public Fragment_pago_servicio_confirma() {
        // Required empty public constructor
    }

    public static Fragment_pago_servicio_confirma newInstance(Object... data) {
        Fragment_pago_servicio_confirma fragment = new Fragment_pago_servicio_confirma();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_servicio_confirma_1", new Gson().toJson(data[0]));
        args.putString("Fragment_pago_servicio_confirma_2", new Gson().toJson(data[1]));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_confirma_1"),CompaniaDTO.class);
            servicePayment = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_confirma_2"),QPAY_ServicePayment.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_confirma, container, false), R.drawable.background_splash_header_3);
    }

    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .showTitle(getString(R.string.text_servicio_title))
                .setColorTitle(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        ImageView ivCompania = getView().findViewById(R.id.iv_compania);
        ivCompania.setImageDrawable(getResources().getDrawable(companiaDTO.getImage()));

        final TextView tvTipoPago = getView().findViewById(R.id.tv_tipo_pago);
        final TextView tvCompania = getView().findViewById(R.id.tv_compania);
        final TextView tvReferencia = getView().findViewById(R.id.tv_referencia);
        final TextView tvMonto = getView().findViewById(R.id.tv_monto);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        String referencia = servicePayment.getQpay_account_number()!=null ? servicePayment.getQpay_account_number() : "";

        referencia = referencia.concat(servicePayment.getQpay_account_number1()!=null
                && !servicePayment.getQpay_account_number1().isEmpty() ? servicePayment.getQpay_account_number1() : "");

        referencia = referencia.concat(servicePayment.getQpay_account_number2()!=null
                && !servicePayment.getQpay_account_number2().isEmpty() ? " "+servicePayment.getQpay_account_number2() : "");

        referencia = referencia.concat(servicePayment.getQpay_account_number3()!=null
                && !servicePayment.getQpay_account_number3().isEmpty() ? " "+servicePayment.getQpay_account_number3() : "");

        String monto = servicePayment.getQpay_amount();

        tvTipoPago.setText(getString(R.string.text_servicios_confirma_2_1));
        tvCompania.setText(getString(companiaDTO.getName()));
        tvReferencia.setText(referencia);
        tvMonto.setText(Utils.paserCurrency(monto) + " " + getString(R.string.text_currency));

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //20210531 RSB. Recomendaciones. Homologacion. Bloqueo boton
                btn_continuar.setClickable(false);

                if(!getContextMenu().validateUserOperation(true)){

                    getContext().alert(R.string.cajero_sin_permiso, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            getContextMenu().initHome();
                        }
                    });

                    return;
                }

                if(HTimerApp.getTimer().isCancel()) {
                    btn_continuar.setClickable(true);
                    getContextMenu().authPIN(new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            getContext().backFragment();

                            realizarPago(servicePayment, new IFunction() {
                                @Override
                                public void execute(Object[] objects) {
                                    getContext().setFragment(Fragment_pago_servicio_ticket.newInstance(objects[0], servicePayment.getQpay_product()), true);
                                }
                            });
                        }
                    });

                } else {

                    realizarPago(servicePayment, new IFunction() {
                        @Override
                        public void execute(Object[] objects) {
                            getContext().setFragment(Fragment_pago_servicio_ticket.newInstance(objects[0], servicePayment.getQpay_product()), true);
                        }
                    });

                }

            }
        });

    }


    public void realizarPago(final QPAY_ServicePayment servicePayment, final IFunction function){

        getContext().loading(true);

        try {

            servicePayment.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            CApplication.setAnalytics(CApplication.ACTION.CB_PAGOS_SERVICIOS_pagan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + servicePayment.getQpay_product(), CApplication.ACTION.MONTO.name(), "" + servicePayment.getQpay_amount()));

            iServicePayment sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    getContext().loading(false);
                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error_transaction);
                        //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM UNSUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getVasTransactions().setNoExitosos(transactionsModel.getVasTransactions().getNoExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);
                    } else {

                        //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM SUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getVasTransactions().setExitosos(transactionsModel.getVasTransactions().getExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponseFirst.QPAY_TaeSaleResponseFirstExcluder()).create();
                        final String json = gson.toJson(result);

                        QPAY_TaeSaleResponseFirst taeResponse = new Gson().fromJson(json, QPAY_TaeSaleResponseFirst.class);

                        if(taeResponse.getQpay_response().equals("true")){

                            //20210703 RSB. Pendings. Reiniciar timer tras transacción exitosa
                            HTimerApp.getTimer().start(getContext());

                            //20200713 RSB. Valida si debe persistir tras ingresar a reporte de transacciones
                            if(AppPreferences.getLocalTxnService()) {
                                try {
                                    DataHelper dataHelper = new DataHelper(getContext());
                                    dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.SERVICE_TXR, taeResponse.getCreatedAt(), servicePayment.getQpay_product(), json, null);
                                } catch (Exception e) {
                                    Log.e("DATA HELPER", "ERROR AL DAR DE ALTA EL REGISTRO");
                                }
                            }

                            getContextMenu().cargarSaldo(true,false,false, new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    if(function != null)
                                        function.execute(json);
                                }
                            });

                        }  else  {
                            //20210531 RSB. Recomendaciones. Homologacion. Bloqueo boton
                            btn_continuar.setClickable(true);
                            getContext().loading(false);
                            getContextMenu().validaSesion(taeResponse.getQpay_code(), taeResponse.getQpay_description());
                        }
                    }
                }
                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error_transaction);
                    //20210531 RSB. Recomendaciones. Homologacion. Bloqueo boton
                    btn_continuar.setClickable(true);

                    //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM UNSUCCESSFUL
                    TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                    transactionsModel.getVasTransactions().setNoExitosos(transactionsModel.getVasTransactions().getNoExitosos() + 1);
                    AppPreferences.setTodayTransactions(transactionsModel);
                }
            }, getContext());

            //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM TOTAL
            TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
            transactionsModel.getVasTransactions().setTotales(transactionsModel.getVasTransactions().getTotales() + 1);
            AppPreferences.setTodayTransactions(transactionsModel);
            sale.doServicePayment(servicePayment);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            //20210531 RSB. Recomendaciones. Homologacion. Bloqueo boton
            btn_continuar.setClickable(true);
        }

    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}