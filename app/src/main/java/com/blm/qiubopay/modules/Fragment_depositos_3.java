package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IAccountDeposit;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_AccountDeposit;
import com.blm.qiubopay.models.QPAY_AccountDepositResponse;
import com.blm.qiubopay.models.QPAY_CashCollectionResponse;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.Formatter;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_depositos_3 extends HFragment {

    private View view;
    private MenuActivity context;
    private QPAY_CashCollectionResponse data;

    private ArrayList<CViewEditText> campos;
    private Button btn_confirmar;
    private EditSpinner bancos;
    private int banco = 0;

    private RadioButton banamex, bbva, santander;
    private LinearLayout layout_campos;

    public static Fragment_depositos_3 newInstance(Object... data) {
        Fragment_depositos_3 fragment = new Fragment_depositos_3();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_depositos_3", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_depositos_3"),QPAY_CashCollectionResponse.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_depositos_3, container, false);

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
                getContext().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_DEPOSITO_BANCARIO_inician);

        layout_campos = view.findViewById(R.id.layout_campos);

        banamex = view.findViewById(R.id.rad_banco_1);
        bbva = view.findViewById(R.id.rad_banco_2);
        santander = view.findViewById(R.id.rad_banco_3);

        banamex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bbva.setChecked(false);
                santander.setChecked(false);
                setFormulario(0);
            }
        });

        bbva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banamex.setChecked(false);
                santander.setChecked(false);
                setFormulario(1);
            }
        });

        santander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bbva.setChecked(false);
                banamex.setChecked(false);
                setFormulario(2);
            }
        });


        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QPAY_AccountDeposit petition = new QPAY_AccountDeposit();
                petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                petition.setQpay_product("0" + (banco + 1));
                petition.setQpay_amount(campos.get(0).getText());
                petition.setQpay_reference(campos.get(1).getText());

                String date[] = null;

                switch (banco){
                    case 0:
                        petition.setQpay_branch(campos.get(2).getText());
                        date = campos.get(3).getText().split("/");
                        petition.setQpay_date(date[2] + "-" + date[1] + "-" + date[0] + " 00:00:00");
                        break;
                    case 1:
                        petition.setQpay_branch(null);
                        date = campos.get(2).getText().split("/");
                        //20200515 RSB. Se solicita ocurltar hora para Bancomer
                        //petition.setQpay_date(date[2] + "-" + date[1] + "-" + date[0] + " " + campos.get(3).getText() + ":00");
                        petition.setQpay_date(date[2] + "-" + date[1] + "-" + date[0] + " 12:00:00");
                        break;
                    case 2:
                        petition.setQpay_branch(null);
                        date = campos.get(2).getText().split("/");
                        petition.setQpay_date(date[2] + "-" + date[1] + "-" + date[0] + " 00:00:00");
                        break;
                }

                realizarTransaccion(petition);

            }
        });

    }

    public void setFormulario(int banco){

        layout_campos.setVisibility(View.VISIBLE);

        if(campos != null)
            for (CViewEditText editText : campos)
                editText.setText("");

        campos = new ArrayList();

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_monto))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint(R.string.text_depositos_21)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        this.banco = banco;

        switch (banco){
            case 0://BANAMEX

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_referencia))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(7)
                        .setType(CViewEditText.TYPE.NUMBER)
                        .setHint(R.string.text_depositos_22)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_sucursal))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(10)
                        .setType(CViewEditText.TYPE.NUMBER)
                        .setHint(R.string.text_depositos_23)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_fecha))
                        .setRequired(true)
                        .setEnabled(false)
                        .setMinimum(10)
                        .setMaximum(10)
                        .setType(CViewEditText.TYPE.NONE)
                        .setDatePicker()
                        .setHint(R.string.text_depositos_24)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                view.findViewById(R.id.edit_sucursal).setVisibility(View.VISIBLE);

                break;
            case 1://BBVA

                //20200515 RSB. Se requiere ocultar la hora

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_referencia))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(10)
                        .setType(CViewEditText.TYPE.NUMBER)
                        .setHint(R.string.text_depositos_22)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_fecha))
                        .setRequired(true)
                        .setEnabled(false)
                        .setMinimum(10)
                        .setMaximum(10)
                        .setType(CViewEditText.TYPE.NONE)
                        .setDatePicker()
                        .setHint(R.string.text_depositos_24)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                view.findViewById(R.id.edit_sucursal).setVisibility(View.GONE);

                break;
            case 2://SANTANDER

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_referencia))
                        .setRequired(true)
                        .setMinimum(1)
                        .setMaximum(8)
                        .setType(CViewEditText.TYPE.NUMBER)
                        .setHint(R.string.text_depositos_22)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                campos.add(CViewEditText.create(view.findViewById(R.id.edit_fecha))
                        .setRequired(true)
                        .setEnabled(false)
                        .setMinimum(10)
                        .setMaximum(10)
                        .setType(CViewEditText.TYPE.NONE)
                        .setDatePicker()
                        .setHint(R.string.text_depositos_24)
                        .setAlert(R.string.text_input_required)
                        .setTextChanged(validate));

                view.findViewById(R.id.edit_sucursal).setVisibility(View.GONE);

                break;
        }

        validate();

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_confirmar.setEnabled(false);
                return;
            }

        btn_confirmar.setEnabled(true);
    }

    public void realizarTransaccion(final QPAY_AccountDeposit petition){

        Logger.i(new Gson().toJson(petition));

        Formatter fmt = new Formatter();
        long sucursal = 0;
        long referencia = Long.parseLong(petition.getQpay_reference());

        switch (banco){
            case 0:

                sucursal = Long.parseLong(petition.getQpay_branch().trim());

                //7 digitos referencia
                petition.setQpay_reference("" + new Formatter().format("%07d", referencia));

                //4 digitos sucursal
                petition.setQpay_branch("" + new Formatter().format("%04d", sucursal));

                break;
            case 1:

                //4 digitos referencia <=4
                if(petition.getQpay_reference().length() <= 4)
                    petition.setQpay_reference("" + new Formatter().format("%04d", referencia));
                //9 digitos referencia >= 5
                else
                    petition.setQpay_reference("" + new Formatter().format("%09d", referencia));

                break;
            case 2:
                //8 digitos referencia
                petition.setQpay_reference("" + new Formatter().format("%08d", referencia));

                break;
        }

        petition.setQpay_amount(petition.getQpay_amount().replace(",","").replace("$", ""));

        context.loading(true);

        CApplication.setAnalytics(CApplication.ACTION.CB_DEPOSITO_BANCARIO_finalizan, ImmutableMap.of(CApplication.ACTION.OPCION.name(), "" + petition.getQpay_product(), CApplication.ACTION.MONTO.name(), "" + petition.getQpay_amount()));

        try {

            IAccountDeposit sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);
                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_AccountDepositResponse.QPAY_AccountDepositResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_AccountDepositResponse saleResponse = gson.fromJson(json, QPAY_AccountDepositResponse.class);

                        if(saleResponse.getQpay_response().equals("true")){

                            CApplication.setAnalytics(CApplication.ACTION.CB_deposito_exitoso);

                            context.cargarSaldo(true,false,false,new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    context.alert("Depósito a cuenta exitoso.", new IAlertButton() {
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
                            });

                        } else {
                            context.loading(false);
                            //210518. RSB Acuerdos homologacion. Quitar mensaje banamex predefinido
                            context.validaSesion(saleResponse.getQpay_code(), saleResponse.getQpay_description());
                            /*if(banco == 1) {
                                context.showAlert("La cuenta Bancomer por el momento no está disponible, si tu referencia es válida será abonada automáticamente en las próximas 24 horas. Si esto no ocurre reporta tu problema o llámanos para ayudarte.");
                            }else
                            if (banco == 0) {
                                context.alert("No pudo realizarse tu abono, si tu referencia es valida la abonaremos en las próximas 24 hrs");
                            }else {
                                context.validaSesion(saleResponse.getQpay_code(), saleResponse.getQpay_description());
                            }*/
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            Logger.i(new Gson().toJson(petition));

            sale.doAccountDeposit(petition);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }


}

