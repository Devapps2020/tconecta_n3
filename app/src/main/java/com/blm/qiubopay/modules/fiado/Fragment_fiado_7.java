package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.fiado.IPagoParcial;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.PQPrefijo;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.fiado.QPAY_Pago_Parcial_Request;
import com.blm.qiubopay.models.fiado.QPAY_Pago_Parcial_Response;
import com.blm.qiubopay.modules.Fragment_registro_financiero_cus_1;
import com.blm.qiubopay.modules.home.Fragment_menu_inicio;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_7 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<CViewEditText> campos;
    public static ArrayList<PQPrefijo> list;
    Button btn_confirm;
    Button text_cancell;

    RadioButton rad_cash;
    RadioButton rad_card_payment;

    public static String deuda;
    public static String monto;

    public static Fragment_fiado_7 newInstance(Object... data) {
        Fragment_fiado_7 fragment = new Fragment_fiado_7();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_fiado_7, container, false);

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

        campos = new ArrayList();

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate(text);
            }
        };

        rad_cash = view.findViewById(R.id.rad_cash);
        rad_card_payment = view.findViewById(R.id.rad_card_payment);

        btn_confirm = view.findViewById(R.id.btn_confirm);
        text_cancell = view.findViewById(R.id.text_cancell);

        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        txt_nombre.setText(Fragment_fiado_3.selection.getFiado_name());

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_total_debt))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setSuffix("MXN")
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint(R.string.text_fiado_20)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                monto = campos.get(0).getTextDouble();

                if(Double.parseDouble(monto) > Double.parseDouble(Fragment_fiado_5.deudaTotal)){
                    context.alert("Se esta abonando un monto superior al adeudo. Verifique el monto.");
                    return;
                }

                if(rad_cash.isChecked()) {
                    pagoParcial(new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            context.setFragment(Fragment_fiado_8.newInstance());
                        }
                    });
                } else {
                    pagoConTarjeta();
                }

            }
        });

        text_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().initHome();
            }
        });

        rad_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fiado_8.efectivo = true;
                btn_confirm.setText("Confirmar pago");
                rad_card_payment.setChecked(false);
                validate();
            }
        });

        rad_card_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fiado_8.efectivo = false;
                btn_confirm.setText("Continuar");
                rad_cash.setChecked(false);
                validate();
            }
        });

        if(!AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration() || AppPreferences.isCashier()) {
            rad_card_payment.setTextColor(context.getResources().getColor(R.color.colorGray));
            rad_card_payment.setEnabled(false);
        }

        if(Fragment_fiado_5.deudaTotal != null)
            campos.get(0).setTextMonto(Utils.paserCurrency(Fragment_fiado_5.deudaTotal));

    }

    private void validate() {


        btn_confirm.setEnabled(false);
        text_cancell.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid())
                return;

        if(!rad_card_payment.isChecked() && !rad_cash.isChecked())
            return;

        text_cancell.setEnabled(true);
        btn_confirm.setEnabled(true);
    }

    public void pagoParcial(final IFunction function) {

        context.loading(true);

        QPAY_Pago_Parcial_Request data = new QPAY_Pago_Parcial_Request();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        data.setFiado_mail(Fragment_fiado_3.selection.getFiado_mail());
        data.setFiado_payment_amount(campos.get(0).getTextDouble());
        data.setFiado_debt_id(null);

        IPagoParcial service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_Pago_Parcial_Response pago_parcial_response = gson.fromJson(json, QPAY_Pago_Parcial_Response.class);

                        Logger.d(new Gson().toJson(pago_parcial_response));

                        if (pago_parcial_response.getQpay_response().equals("true")) {

                            //Fragment_fiado_3.reload.execute();

                            Fragment_fiado_3.selection.setFiado_payment_id(pago_parcial_response.getQpay_object()[0].getFiado_payment_id());

                            if(function != null)
                                function.execute();

                        } else {

                            if (pago_parcial_response.getQpay_code().equals("017")
                                    || pago_parcial_response.getQpay_code().equals("018")
                                    || pago_parcial_response.getQpay_code().equals("019")
                                    || pago_parcial_response.getQpay_code().equals("020")) {
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        //context.startActivity(InicioActivity.class, true);
                                    }
                                });

                            } else if (pago_parcial_response.getQpay_description().contains("UNAUTHORIZED:HTTP")) {
                                //context.alertAEONBlockedUser();
                            } else {
                                context.alert(pago_parcial_response.getQpay_description());
                            }


                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        service.pagoParcial(data);

    }

    public void pagoConTarjeta() {

        getContextMenu().pagoTarjeta = new IFunction() {
            @Override
            public void execute(Object[] data) {
                pagoParcial(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContextMenu().pagoTarjeta = null;

                    }
                });
            }
        };

        getContextMenu().despuesPago = new IFunction() {
            @Override
            public void execute(Object[] data) {
                context.setFragment(Fragment_fiado_8.newInstance());
                getContextMenu().despuesPago = null;
            }
        };


        getContextMenu().validacionFinanciera.execute();
    }

    public void validate(String text) {

        btn_confirm.setEnabled(false);

        if(!rad_cash.isChecked() && !rad_cash.isChecked())
            return;

        if(!rad_card_payment.isChecked() && !rad_card_payment.isChecked())

            btn_confirm.setEnabled(true);


    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

