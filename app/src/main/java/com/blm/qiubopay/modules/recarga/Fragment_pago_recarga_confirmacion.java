package com.blm.qiubopay.modules.recarga;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.ServicePackagesAdapter;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HTimerApp;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ITaeSale;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_recarga_confirmacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_recarga_confirmacion extends HFragment implements IMenuContext {

    private static final String TAG = "recarga_confirmacion";

    private CompaniaDTO companiaDTO;
    private QPAY_TaeSale taeSale;

    Button btn_continuar;

    public Fragment_pago_recarga_confirmacion() {
        // Required empty public constructor
    }

    public static Fragment_pago_recarga_confirmacion newInstance(Object... data) {
        Fragment_pago_recarga_confirmacion fragment = new Fragment_pago_recarga_confirmacion();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_recarga_confirmacion_1", new Gson().toJson(data[0]));
        args.putString("Fragment_pago_recarga_confirmacion_2", new Gson().toJson(data[1]));
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
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_recarga_confirmacion_1"),CompaniaDTO.class);
            taeSale = new Gson().fromJson(getArguments().getString("Fragment_pago_recarga_confirmacion_2"),QPAY_TaeSale.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_recarga_confirmacion, container, false), R.drawable.background_splash_header_3);
    }

    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .showTitle(getString(R.string.text_recarga_title))
                .setColorTitle(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        final TextView tvTipoPago = getView().findViewById(R.id.tv_tipo_pago);
        final TextView tvCompania = getView().findViewById(R.id.tv_compania);
        final TextView tvCelular = getView().findViewById(R.id.tv_celular);
        final TextView tvMonto = getView().findViewById(R.id.tv_monto);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        tvTipoPago.setText(getString(R.string.text_recarga_confirma_2_1));
        tvCompania.setText(getString(companiaDTO.getName()).toUpperCase());
        tvCelular.setText(taeSale.getQpay_mobile_number());
        tvMonto.setText(taeSale.getQpay_amount());

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

    public void realizarRecarga(final QPAY_TaeSale taeSale, final IFunction function){

        getContext().loading(true);

        CApplication.setAnalytics(CApplication.ACTION.CB_PAGOS_RECARGAS_pagan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + taeSale.getQpay_carrier(), CApplication.ACTION.MONTO.name(), "" + taeSale.getQpay_amount()));

        taeSale.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        taeSale.setQpay_mobile_number(taeSale.getQpay_mobile_number());
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
                    } else {

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
                }
            }, getContext());

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