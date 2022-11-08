package com.blm.qiubopay.modules.recarga;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ITaeSale;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_recarga_numero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_recarga_numero extends HFragment implements IMenuContext {

    private static final String TAG = "recarga_numero";

    private ArrayList<CViewEditText> campos = null;

    private CompaniaDTO companiaDTO;
    private QPAY_TaeSale taeSale;

    private Button btn_continuar;

    public Fragment_pago_recarga_numero() {
        // Required empty public constructor
    }

    public static Fragment_pago_recarga_numero newInstance(Object... data) {
        Fragment_pago_recarga_numero fragment = new Fragment_pago_recarga_numero();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_recarga_numero_1", new Gson().toJson(data[0]));
        args.putString("Fragment_pago_recarga_numero_2", new Gson().toJson(data[1]));
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

        if(getArguments() != null){
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_recarga_numero_1"),CompaniaDTO.class);
            taeSale = new Gson().fromJson(getArguments().getString("Fragment_pago_recarga_numero_2"),QPAY_TaeSale.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_recarga_numero, container, false), R.drawable.background_splash_header_1);
    }


    public void initFragment() {

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .showTitle(getString(R.string.text_recarga_title))
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        ImageView ivCompania = getView().findViewById(R.id.iv_compania);
        TextView tvMonto =  getView().findViewById(R.id.tv_monto);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate(text);
            }
        };

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_pago_servicios_30)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if(campos.size()>1){
                            if(!campos.get(1).getText().isEmpty() && !text.equals(campos.get(1).getText()))
                                campos.get(1).setText("");

                            if(text.length() == 10)
                                validate.onChange(text);
                        }
                    }
                }));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular_confirm))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_pago_servicios_31)
                .setAlert(R.string.text_input_required)
                .setError(R.string.text_error_pago_servicios_6)
                .setTextChanged(validate));

        ivCompania.setImageDrawable(getResources().getDrawable(companiaDTO.getImage()));

        tvMonto.setText(taeSale.getQpay_amount().replace(".00",""));

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

                            realizarRecarga(taeSale, new IFunction() {
                                @Override
                                public void execute(Object[] dato) {
                                    getContext().setFragment(Fragment_pago_recarga_ticket.newInstance(dato[0], taeSale.getQpay_carrier(), dato[1]), true);
                                }
                            });
                        }
                    });

                } else {

                    realizarRecarga(taeSale, new IFunction() {
                        @Override
                        public void execute(Object[] dato) {
                            getContext().setFragment(Fragment_pago_recarga_ticket.newInstance(dato[0], taeSale.getQpay_carrier(), dato[1]), true);
                        }
                    });

                }

            }
        });

    }

    public void validate(String text) {

        btn_continuar.setEnabled(false);

        for(CViewEditText edit: campos)
            if(!edit.isValid())
                return;

        if(campos.size() > 0 && !campos.get(0).getText().equals(campos.get(1).getText())) {
            campos.get(1).activeError();
            return;
        }

        btn_continuar.setEnabled(true);

    }


    public void realizarRecarga(final QPAY_TaeSale taeSale, final IFunction function){

        getContext().loading(true);

        CApplication.setAnalytics(CApplication.ACTION.CB_PAGOS_RECARGAS_pagan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + taeSale.getQpay_carrier(), CApplication.ACTION.MONTO.name(), "" + taeSale.getQpay_amount()));

        taeSale.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        taeSale.setQpay_mobile_number(campos.get(0).getTextInteger());
        taeSale.setQpay_amount(taeSale.getQpay_amount().replace("$", "").replace(".00","") + ".00");
        taeSale.setQpay_routeId("0");

        if(SessionApp.getInstance().getSellerUserResponse() != null && SessionApp.getInstance().getSellerUserResponse().getQpay_object().length > 0) {
            for (SellerUserDTO user : SessionApp.getInstance().getSellerUserResponse().getQpay_object()) {
                if(user.getSeller_id().startsWith("11") || user.getSeller_id().startsWith("12")) {
                    taeSale.setQpay_routeId(SessionApp.getInstance().getSellerUserResponse().getQpay_object()[0].getSeller_id());
                    break;
                }
            }
        }

        ITaeSale salesListener = null;

        try {

            salesListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    getContext().loading(false);
                    if (result instanceof ErrorResponse) {
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

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_TaeSaleResponseFirst.QPAY_TaeSaleResponseFirstExcluder()).create();
                        final String json = gson.toJson(result);

                        QPAY_TaeSaleResponseFirst taeResponse = new Gson().fromJson(json, QPAY_TaeSaleResponseFirst.class);

                        if(taeResponse.getQpay_response().equals("true")){

                            //20210703 RSB. Pendings. Reiniciar timer tras transacción exitosa
                            HTimerApp.getTimer().start(getContext());

                            if(AppPreferences.getLocalTxnTae()){
                                try {
                                    DataHelper dataHelper = new DataHelper(getContext());
                                    dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.TAE_TXR, taeResponse.getCreatedAt(), taeSale.getQpay_carrier(), gson.toJson(taeResponse),null);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            getContextMenu().cargarSaldo(true,false,false,new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    if(function != null)
                                        function.execute(json, taeSale);

                                }
                            });

                        } else {

                            getContext().loading(false);
                            getContextMenu().validaSesion(taeResponse.getQpay_code(), taeResponse.getQpay_description());

                            //20210531 RSB. Recomendaciones. Homologacion. Bloqueo boton
                            btn_continuar.setClickable(true);

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error_transaction);
                    //20210531 RSB. Recomendaciones. Homologacion. Bloqueo boton
                    btn_continuar.setClickable(true);

                    //TODO TRANSACTION COUNTER. TRANSACTION SUM UNSUCCESSFUL
                    TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                    transactionsModel.getVasTransactions().setNoExitosos(transactionsModel.getVasTransactions().getNoExitosos() + 1);
                    AppPreferences.setTodayTransactions(transactionsModel);
                }
            }, getContext());

            //TODO TRANSACTION COUNTER. VAS TRANSACTION SUM TOTAL
            TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
            transactionsModel.getVasTransactions().setTotales(transactionsModel.getVasTransactions().getTotales() + 1);
            AppPreferences.setTodayTransactions(transactionsModel);
            salesListener.doTaeSale(taeSale);

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