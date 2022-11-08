package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.evo.ApiController;
import com.blm.qiubopay.evo.Config;
import com.blm.qiubopay.evo.EvoController;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ICallback;
import com.blm.qiubopay.listeners.IGetTransactionId;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_TransactionCard;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.mastercard.gateway.android.sdk.Gateway;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.UUID;

import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_depositos_4 extends HFragment {

    private View view;
    private MenuActivity context;

    private ArrayList<CViewEditText> campos;

    private Button btn_continuar;

    private double porcentaje;

    public static Fragment_depositos_4 newInstance(double comision) {
        Fragment_depositos_4 fragment = new Fragment_depositos_4();
        Bundle args = new Bundle();

        args.putDouble("Fragment_depositos_4", comision);

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
            porcentaje = getArguments().getDouble("Fragment_depositos_4");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_depositos_4, container, false);
        setView(view);
        initFragment();

        return view;
    }

    public void initFragment(){

        btn_continuar = view.findViewById(R.id.btn_continuar);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEVO(campos.get(2).getText().replace(",","").replace("$",""));
            }
        });

        campos = new ArrayList();

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        EditText comision = view.findViewById(R.id.edit_importe_comision).findViewById(R.id.text_input_layout_edit);
        EditText total = view.findViewById(R.id.edit_importe_total).findViewById(R.id.text_input_layout_edit);

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_monto_depo))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint("Monto")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    double monto = Double.parseDouble(campos.get(0).getText().replace(",","").replace("$",""));

                                    if(monto >= 1)
                                        btn_continuar.setEnabled(true);
                                    else
                                        btn_continuar.setEnabled(false);

                                    double monto_comision = calcularComision(monto);

                                    comision.setEnabled(false);
                                    total.setEnabled(false);

                                    comision.setText(Utils.paserCurrency("" + monto_comision));
                                    total.setText(Utils.paserCurrency("" + (monto + monto_comision)));

                                }catch (Exception ex){
                                    comision.setText("");
                                    total.setText("");
                                    btn_continuar.setEnabled(false);
                                }

                            }
                        });



                    }
                }));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_importe_comision))
                .setRequired(false)
                .setType(CViewEditText.TYPE.NONE)
                .setMinimum(0)
                .setMaximum(13)
                .setHint("Comisión por depósito")
                .setEnabled(false));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_importe_total))
                .setRequired(false)
                .setType(CViewEditText.TYPE.NONE)
                .setMinimum(0)
                .setMaximum(13)
                .setHint("Total")
                .setEnabled(false));

    }

    public double calcularComision(double monto){
        return (porcentaje * monto/ 100);
    }

    public void initEVO(String total) {

        context.evoController = new EvoController(context);

        context.evoController.setAmount(total);

        context.evoController.setApiController(ApiController.getInstance());
        context.evoController.getApiController().setMerchantServerUrl(Config.MERCHANT_URL.getValue(context));

        context.evoController.setGateway(new Gateway());
        context.evoController.getGateway().setMerchantId(Config.MERCHANT_ID.getValue(context));

        try {
            Gateway.Region region = Gateway.Region.valueOf(Config.REGION.getValue(context));
            context.evoController.getGateway().setRegion(region);
        } catch (Exception e) {
            e.printStackTrace();
            context.alert(R.string.error_general_evo);
            Logger.d("Invalid Gateway region value provided");
            return;
        }

        // random order/txn IDs for example purposes
        String orderId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();
        context.evoController.setOrderId(orderId.substring(0, orderId.indexOf('-')));
        context.evoController.setTransactionId(transactionId.substring(0, transactionId.indexOf('-')));

        context.evoController.createSession(new ICallback() {
            @Override
            public void onSuccess(Object[] data) {

                QPAY_TransactionCard request = new QPAY_TransactionCard();
                request.setQpay_amount(total);
                request.setQpay_operationType("01");
                request.setQpay_reference("01" + context.evoController.getOrderId());
                request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                request.setQpay_transactionId(context.evoController.getTransactionId());

                initTransaction(request);

            }
            @Override
            public void onError(Object[] data) {
                context.initHome();
            }
        });

    }

    public void initTransaction(QPAY_TransactionCard request) {

        context.loading(true);

        try {

            IGetTransactionId transaction = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        QPAY_BaseResponse response = new Gson().fromJson(new Gson().toJson(result), QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            context.setFragment(Fragment_depositos_5.newInstance());

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
                                        context.startActivity(MenuActivity.class, true);
                                    }
                                });
                            }
                            else
                                context.alert(response.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    //spinnerDialog.hideProgress();
                    //spinnerDialog.showAlert("Error " + ((ErrorResponse) result).getInternalCode() + "\n" + ((ErrorResponse) result).getMessage());
                }

            }, context);

            transaction.getTransactionId(request);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }
    }
}

