package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasTransactionResponse;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IFinancialVas;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasPetition;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.HashMap;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_depositos_7 extends HFragment {

    private View view;
    private MenuActivity context;
    private QPAY_FinancialVasResponse data;

    private ArrayList<HEditText> campos;
    private Button btn_continuar;
    private IFunction function =  null;

    private RadioGroup rgMontos;
    private CViewEditText edit_importe;

    private String amount;
    private Double minLimit;
    private Double maxLimit;

    private HashMap<Integer,String> montosMap;

    private static final String OTRO_MONTO = "Otro monto";

    public static Fragment_depositos_7 newInstance(Object... data) {
        Fragment_depositos_7 fragment = new Fragment_depositos_7();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_depositos_7", new Gson().toJson(data[0]));

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

        if (getArguments() != null) {
            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_FinancialVasResponse.QPAY_FinancialVasResponseExcluder()).create();
            data = gson.fromJson(getArguments().getString("Fragment_depositos_7"), QPAY_FinancialVasResponse.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_depositos_7, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        String sMinLimit = data.getQpay_object()[0].getQpay_min_limit();
        sMinLimit = (sMinLimit!=null && !sMinLimit.isEmpty() ? sMinLimit : "0.0");
        String sMaxLimit = data.getQpay_object()[0].getQpay_max_limit();
        sMaxLimit = (sMaxLimit!=null && !sMaxLimit.isEmpty() ? sMaxLimit : "0.0");

        minLimit = Double.parseDouble(sMinLimit);
        maxLimit = Double.parseDouble(sMaxLimit);

        // ELEMENTOS DE PANTALLA

        final LinearLayout layoutImporte = view.findViewById(R.id.layout_importe);
        final LinearLayout layout_montos = view.findViewById(R.id.layout_montos);

        final TextView tv_minimo = view.findViewById(R.id.tv_minimo);
        final TextView tv_maximo = view.findViewById(R.id.tv_maximo);

        rgMontos = view.findViewById(R.id.rg_montos);
        btn_continuar = view.findViewById(R.id.btn_continuar);

        edit_importe = CViewEditText.create(view.findViewById(R.id.edit_importe))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint(R.string.text_depositos_30)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        amount = edit_importe.getTextDecimal();
                        validate();
                    }
                });

        // CARGA INICIAL DE ELEMENTOS DE PANTALLA

        tv_minimo.setText( "Mínimo disponible:\n" + Utils.paserCurrency(minLimit.toString()));
        tv_maximo.setText( "Máximo disponible:\n" + Utils.paserCurrency(maxLimit.toString()));
        fillRadioGroup(data.getQpay_object()[0].getQpay_valid_amounts());

        // LISTENERS DE ELEMENTOS DE PANTALLA

        rgMontos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String monto = montosMap.get(checkedId);
                if(monto!=null){
                    if(monto.compareTo(OTRO_MONTO)==0){
                        amount = null;
                        edit_importe.setText("");
                        layoutImporte.setVisibility(View.VISIBLE);
                    } else {
                        edit_importe.setText("");
                        amount = monto;
                        layoutImporte.setVisibility(View.GONE);
                    }
                    validate();
                }
            }
        });

        btn_continuar = view.findViewById(R.id.btn_continuar);

        TextView titulo_saldo = view.findViewById(R.id.text_saldo);
        titulo_saldo.setText(context.getSaldoFinanciero());

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doFinancialVas(new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        if(null != data[0]){
                            QPAY_FinancialVasTransactionResponse response = (QPAY_FinancialVasTransactionResponse)data[0];
                            if(response.getQpay_response().equals("true")) {
                                Tools.setAlreadyUsedFinancialVasToday();
                                context.cargarSaldo(true,false,false, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        Fragment_depositos_8.data = response;
                                        getContext().setFragment(Fragment_depositos_8.newInstance(), true);
                                    }
                                });

                            } else {
                                context.alert("Rechazo \n" + response.getQpay_description(), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        context.initHome();
                                    }
                                });

                            }
                        }
                    }
                });
            }
        });

    }

    public void doFinancialVas(final IFunction function){

        context.loading(true);

        try {

            QPAY_FinancialVasPetition object = new QPAY_FinancialVasPetition();

            object.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            object.setQpay_amount(amount);

            IFinancialVas petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_FinancialVasTransactionResponse
                                .QPAY_FinancialVasTransactionResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_FinancialVasTransactionResponse response = gson.fromJson(json, QPAY_FinancialVasTransactionResponse.class);

                        if(function != null)
                            function.execute(response);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }

            }, context);

            petition.processFinancialVas(object);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }

    private void fillRadioGroup(String[] montos) {

        montosMap = new HashMap<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(String monto : montos) {
            montosMap.put(monto.hashCode(),monto);
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.item_radio, null);
            radioButton.setText("$" + monto);
            radioButton.setId(monto.hashCode());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rgMontos.addView(radioButton, params);
        }

        montosMap.put(OTRO_MONTO.hashCode(),OTRO_MONTO);
        RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.item_radio, null);
        radioButton.setText(OTRO_MONTO);
        radioButton.setId(OTRO_MONTO.hashCode());
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        rgMontos.addView(radioButton, params);

    }

    private void validate() {

        if(amount!=null && !amount.isEmpty()) {

            amount = amount.trim().replace(",", "").replace("$", "");
            Double dAmount = Double.parseDouble(amount);
            if(dAmount<minLimit || dAmount>maxLimit) {
                btn_continuar.setEnabled(false);
            } else {
                btn_continuar.setEnabled(true);
            }
        } else {
            btn_continuar.setEnabled(false);
        }

    }

}

