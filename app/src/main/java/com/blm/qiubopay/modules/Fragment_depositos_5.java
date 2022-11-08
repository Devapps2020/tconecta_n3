package com.blm.qiubopay.modules;

import android.content.Context;
import android.content.Intent;
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
import com.blm.qiubopay.helpers.HCreditCardFormat;
import com.blm.qiubopay.helpers.interfaces.ICallback;
import com.mastercard.gateway.android.sdk.Gateway;
import com.mastercard.gateway.android.sdk.Gateway3DSecureCallback;
import com.mastercard.gateway.android.sdk.GatewayMap;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_depositos_5 extends HFragment {

    private View view;
    private MenuActivity context;

    private ArrayList<CViewEditText> campos;
    private Button btn_pagar;

    private ICallback finalizar;

    public static Fragment_depositos_5 newInstance(Object... data) {
        Fragment_depositos_5 fragment = new Fragment_depositos_5();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_depositos_5, container, false);

        initFragment();

        return view;
    }

    public void initFragment() {

        campos = new ArrayList();

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        btn_pagar = view.findViewById(R.id.btn_pagar);

        btn_pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuar();
            }
        });

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_nombre_tarjeta))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(20)
                .setType(CViewEditText.TYPE.TEXT_SN)
                .setHint("Nombre en tarjeta")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_numero_tarjeta))
                .setRequired(true)
                .setMinimum(16)
                .setMaximum(16)
                .setType(CViewEditText.TYPE.NUMBER_PASS)
                .setHint("Número de tarjeta")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_fecha_tarjeta))
                .setRequired(true)
                .setMinimum(5)
                .setMaximum(5)
                .setType(CViewEditText.TYPE.FECHA_TARJETA)
                .setHint("Expiración")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_cvv_tarjeta))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(3)
                .setType(CViewEditText.TYPE.NUMBER_PASS)
                .setHint("CVV")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        validate();

        finalizar = new ICallback() {
            @Override
            public void onSuccess(Object[] data) {
                realizarDeposito();
            }

            @Override
            public void onError(Object[] data) {
                context.backFragment();
            }
        };

    }

    public void realizarDeposito() {
        context.cargarSaldo(false,false,false,new IFunction() {
            @Override
            public void execute(Object[] data) {
                context.initHome();
            }
        });
    }


    public void validate(){

        btn_pagar.setEnabled(false);

        for(int i=0; i<campos.size(); i++) {
            if(!campos.get(i).isValid())
                return;
        }

        if(!validateCard(campos.get(1).getText()))
            return;

        btn_pagar.setEnabled(true);
    }

    public void continuar() {

        String date[] = campos.get(2).getText().split("/");

        context.evoController.updateSession(new ICallback() {
            @Override
            public void onSuccess(Object[] data) {
                context.evoController.check3dsEnrollment(finalizar);
            }
            @Override
            public void onError(Object[] data) {
                context.backFragment();
            }
        },  campos.get(0).getText(), campos.get(1).getText(), date[0], date[1],campos.get(3).getText());

        context.setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {

                // handle the 3DSecure lifecycle
                if (Gateway.handle3DSecureResult(requestCode, resultCode, data, new Gateway3DSecureCallback() {
                    @Override
                    public void on3DSecureComplete(GatewayMap result) {

                        int apiVersionInt = Integer.valueOf(context.evoController.getApiVersion());

                        if (apiVersionInt <= 46) {
                            if ("AUTHENTICATION_FAILED".equalsIgnoreCase((String) result.get("3DSecure.summaryStatus"))) {
                                on3DSecureCancel();
                                return;
                            }
                        } else { // version >= 47
                            if ("DO_NOT_PROCEED".equalsIgnoreCase((String) result.get("response.gatewayRecommendation"))) {
                                on3DSecureCancel();
                                return;
                            }
                        }

                        context.evoController.processPayment(finalizar);

                    }

                    @Override
                    public void on3DSecureCancel() {
                        context.loading(false);
                        context.alert(R.string.error_general_evo_cancel_3ds, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                context.backFragment();

                            }
                        });
                    }
                }))  return;

            }
        });

    }

    public boolean validateCard(String ccNum) {

        ArrayList<String> listOfPattern=new ArrayList<String>();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        String ptMasterCard2 = "^2[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        listOfPattern.add(ptMasterCard2);

        for(String p:listOfPattern){
            if(ccNum.matches(p)){
                return true;
            }
        }

        return false;

    }

}

