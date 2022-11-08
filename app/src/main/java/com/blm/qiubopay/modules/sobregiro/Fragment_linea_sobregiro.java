package com.blm.qiubopay.modules.sobregiro;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.ILoans;
import com.blm.qiubopay.listeners.ILoansQuery;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Loans;
import com.blm.qiubopay.models.prestamos.QPAY_LoanFirstResponse;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.modules.servicio.Fragment_pago_servicio_confirma;
import com.blm.qiubopay.modules.servicio.Fragment_pago_servicio_monto;
import com.blm.qiubopay.modules.servicio.Fragment_pago_servicio_ticket;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_linea_sobregiro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_linea_sobregiro extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;

    private Button btn_continuar;

    public Fragment_linea_sobregiro() {
        // Required empty public constructor
    }

    public static Fragment_linea_sobregiro newInstance(Object... data) {
        Fragment_linea_sobregiro fragment = new Fragment_linea_sobregiro();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_linea_sobregiro, container, false), R.drawable.background_splash_header_1);
    }


    public void initFragment() {

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        final RadioButton rad_consulta = getView().findViewById(R.id.rad_consulta);
        final RadioButton rad_disposicion = getView().findViewById(R.id.rad_disposicion);
        final View edit_monto = getView().findViewById(R.id.edit_monto);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        campos.add(CViewEditText.create(edit_monto)
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint("Monto")
                .setAlert(R.string.text_input_required)
                        .setTextChanged(new CViewEditText.ITextChanged() {
                            @Override
                            public void onChange(String text) {
                                if(campos!=null && !campos.isEmpty()){
                                    validate(text);
                                }
                            }
                        }));

        edit_monto.setVisibility(View.GONE);
        btn_continuar.setEnabled(false);

        final QPAY_Loans qpay_loans = new QPAY_Loans();
        qpay_loans.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        rad_consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_monto.setVisibility(View.GONE);
                btn_continuar.setText(R.string.text_button_44);
                btn_continuar.setEnabled(true);
                btn_continuar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buscarOlc(qpay_loans);
                    }
                });
            }
        });

        rad_disposicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_monto.setVisibility(View.VISIBLE);
                campos.get(0).setText("");
                btn_continuar.setText(R.string.text_button_56);
                btn_continuar.setEnabled(false);
                btn_continuar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String monto = campos.get(0).getTextDecimal();
                        qpay_loans.setQpay_amount(monto);

                        solicitarPrestamo(qpay_loans, new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                getContext().setFragment(Fragment_pago_servicio_ticket.newLoanInstance(true, data[0], "00"), true);
                            }
                        });
                    }
                });
            }
        });

    }


    public void validate(String text) {

        btn_continuar.setEnabled(false);

        for(CViewEditText edit: campos)
            if(!edit.isValid())
                return;

        btn_continuar.setEnabled(true);

    }


    /**
     * Buscar línea de sobregiro de Fin Común
     */
    public void buscarOlc(QPAY_Loans qpay_loans){

        getContext().loading(true);

        try {
            ILoansQuery sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_LoanFirstResponse.QPAY_LoanFirstResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_LoanFirstResponse loanQueryResponse = gson.fromJson(json, QPAY_LoanFirstResponse.class);

                        if (loanQueryResponse.getQpay_response().equals("true")) {

                            String textInfo = "";
                            textInfo += "Fecha : " + loanQueryResponse.getQpay_object()[0].getDateTime() + "\n\n";
                            textInfo += "Referencia : " + loanQueryResponse.getQpay_object()[0].getRequestId() + "\n\n";
                            textInfo += "Adeudo: $" + loanQueryResponse.getQpay_object()[0].getCurrentOverdraft() + "\n\n";
                            textInfo += "Interés diario por cada\n$100.00 : $" + loanQueryResponse.getQpay_object()[0].getInterestDue() + " peso.";

                            getContext().alert(textInfo, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return getString(R.string.accept_button);
                                }

                                @Override
                                public void onClick() {
                                    getContextMenu().initHome();
                                }
                            });

                        } else  {
                            getContextMenu().validaSesion(loanQueryResponse.getQpay_code(), loanQueryResponse.getQpay_description());
                        }
                    }
                }
                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            sale.doLoansQuery(qpay_loans);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    /**
     * Solicitar línea de sobregiro de Fin Común
     */
    public void solicitarPrestamo(QPAY_Loans loans, final IFunction function){

        getContext().loading(true);

        try {

            loans.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            ILoans sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new Gson();
                        final String json = gson.toJson(result);

                        final QPAY_LoanFirstResponse loanResponse = new Gson().fromJson(json, QPAY_LoanFirstResponse.class);

                        if(loanResponse.getQpay_response().equals("true")) {

                            getContextMenu().cargarSaldo(true,false,false,new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    if(function != null)
                                        function.execute(loanResponse.getQpay_object()[0]);

                                }
                            });

                        }  else  {
                            getContext().loading(false);
                            getContextMenu().validaSesion(loanResponse.getQpay_code(), loanResponse.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            sale.doLoansService(loans);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}