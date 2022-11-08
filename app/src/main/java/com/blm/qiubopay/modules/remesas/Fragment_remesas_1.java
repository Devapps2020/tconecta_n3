package com.blm.qiubopay.modules.remesas;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IRemesas;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.remesas.TC_GetRemittanceResponse;
import com.blm.qiubopay.models.remesas.TC_QueryRemittancePetition;
import com.blm.qiubopay.models.remesas.TC_QueryRemittanceResponse;
import com.blm.qiubopay.models.remesas.TC_RemittanceTrx;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_remesas_1 extends HFragment implements IMenuContext {

    private MenuActivity context;

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    //Layouts
    private LinearLayout layout_tab1;
    private LinearLayout layoutResume;

    //View resumen de remesa
    private TextView txt_amount_title;
    private TextView txt_beneficiario;
    private TextView txt_remitente;
    private TextView txt_remesador;
    private TextView txt_clave_remesa;
    private TextView txt_monto;
    private TextView txt_monto_pesos;

    private TC_QueryRemittanceResponse response;
    private TC_GetRemittanceResponse getRemittanceResponse;

    private AlertDialog alertConfirmation = null;

    private boolean payRemittance = false;

    private ListView list_trx;
    private LinearLayout layout_list;
    List<TC_RemittanceTrx> remittances;

    private IFunction function;

    public static Fragment_remesas_1 newInstance() {
        return new Fragment_remesas_1();
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_remesas_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void initFragment() {

        campos = new ArrayList();

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
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

        list_trx = getView().findViewById(R.id.list_trx);
        layout_list = getView().findViewById(R.id.layout_list);

        LinearLayout tab_1 = getView().findViewById(R.id.tab_1);
        LinearLayout tab_2 = getView().findViewById(R.id.tab_2);
        TextView text_1 = getView().findViewById(R.id.text_1);
        TextView text_2 = getView().findViewById(R.id.text_2);
        View line_1 = getView().findViewById(R.id.line_1);
        View line_2 = getView().findViewById(R.id.line_2);

        layout_tab1  = getView().findViewById(R.id.layout_tab1);
        layoutResume = getView().findViewById(R.id.layoutResume);

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        /* View Resumen Rmesa*/
        txt_amount_title    = getView().findViewById(R.id.txt_amount_title);
        txt_beneficiario    = getView().findViewById(R.id.txt_beneficiario);
        txt_remitente       = getView().findViewById(R.id.txt_remitente);
        txt_remesador       = getView().findViewById(R.id.txt_remesador);
        txt_clave_remesa    = getView().findViewById(R.id.txt_clave_remesa);
        txt_monto           = getView().findViewById(R.id.txt_monto);
        txt_monto_pesos     = getView().findViewById(R.id.txt_monto_pesos);
        /* View Resumen Rmesa*/

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_codigo_remesa))
                .setRequired(true)
                .setMinimum(Globals.environment == Globals.ENVIRONMENT.PRD ? 8 : 7)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_1)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_primer_nombre))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_2)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_segundo_nombre))
                .setRequired(false)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_primer_apellido))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_3)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_segundo_apellido))
                .setRequired(false)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_1.setEnabled(false);
                tab_2.setEnabled(true);

                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.GONE);
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));

                hideShowTab1(false);
                hideShowTab2(true);

            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_1.setEnabled(true);
                tab_2.setEnabled(false);

                line_1.setVisibility(View.GONE);
                line_2.setVisibility(View.VISIBLE);
                text_2.setTextColor(getContext().getResources().getColor(R.color.colorBimboBlueDark));
                text_1.setTextColor(getContext().getResources().getColor(R.color.colorBimboGrayDark));

                hideShowTab1(true);
                hideShowTab2(false);

                getRemittances();
            }
        });

        btn_continuar.setText("Consultar");
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!payRemittance)
                    queryRemittance();
                else
                    showConfirmationAlert();
            }
        });

        function = new IFunction() {
            @Override
            public void execute(Object[] data) {
                Log.d("Finish Back Function","Regresa al fragment");
                resetView();
            }
        };

        hideShowResume(true);

        validate();
        
    }

    private void hideShowTab1(Boolean hide){
        if(hide)
            layout_tab1.setVisibility(View.GONE);
        else
            layout_tab1.setVisibility(View.VISIBLE);
    }

    private void hideShowTab2(Boolean hide){
        if(hide)
            layout_list.setVisibility(View.GONE);
        else
            layout_list.setVisibility(View.VISIBLE);
    }

    private void hideShowResume(Boolean hide){
        if(hide) {
            payRemittance = false;
            layoutResume.setVisibility(View.GONE);
        }
        else {
            payRemittance = true;
            updateRemittanceInformationResume();
            layoutResume.setVisibility(View.VISIBLE);
        }
    }

    private void enabledDisableEntryFields(Boolean enabled){
        campos.get(0).setEnabled(enabled);
        campos.get(1).setEnabled(enabled);
        campos.get(2).setEnabled(enabled);
        campos.get(3).setEnabled(enabled);
        campos.get(4).setEnabled(enabled);
    }

    private void resetValues(){
        campos.get(0).setText("");
        campos.get(1).setText("");
        campos.get(2).setText("");
        campos.get(3).setText("");
        campos.get(4).setText("");
    }

    private void resetView(){
        resetValues();
        enabledDisableEntryFields(true);
        hideShowResume(true);
        btn_continuar.setText("CONSULTAR");
        validate();
    }

    private void updateRemittanceInformationResume(){
        txt_amount_title.setText("Monto a pagar: " + Utils.paserCurrency(""+response.getQpay_object()[0].getRemesa().getMontoMonedaOrigen()) + " " + response.getQpay_object()[0].getRemesa().getMonedaOrigen());
        txt_beneficiario.setText(String.format("%s %s %s %s", response.getQpay_object()[0].getBeneficiario().getPrimerNombre(),response.getQpay_object()[0].getBeneficiario().getSegundoNombre(),response.getQpay_object()[0].getBeneficiario().getPrimerApellido(),response.getQpay_object()[0].getBeneficiario().getSegundoApellido()));
        txt_remitente.setText(String.format("%s %s %s %s", response.getQpay_object()[0].getRemitente().getPrimerNombre(),response.getQpay_object()[0].getRemitente().getSegundoNombre(),response.getQpay_object()[0].getRemitente().getPrimerApellido(),response.getQpay_object()[0].getRemitente().getSegundoApellido()));
        txt_remesador.setText(response.getQpay_object()[0].getRemesa().getRemesador());
        txt_clave_remesa.setText("" + response.getQpay_object()[0].getRemesa().getIdRemesa());
        txt_monto.setText("" + Utils.paserCurrency("" + response.getQpay_object()[0].getRemesa().getMontoMonedaOrigen()) + " " + response.getQpay_object()[0].getRemesa().getMonedaOrigen());
        txt_monto_pesos.setText("" + Utils.paserCurrency("" + response.getQpay_object()[0].getRemesa().getMontoMonedaPago()) + " " + response.getQpay_object()[0].getRemesa().getMonedaPago());
    }

    private void validate(){

        btn_continuar.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_continuar.setEnabled(true);

    }

    //Alerte de confirmación
    public void showConfirmationAlert(final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_remesas, null);

        //¿Dispones de $100.00 en la caja para dar al cliente?
        TextView textView = view.findViewById(R.id.text_title);

        textView.setText(String.format("¿Dispones de %s en la caja para dar al cliente?",Utils.paserCurrency("" + response.getQpay_object()[0].getRemesa().getMontoMonedaPago())));

        Button btn_solicitar = view.findViewById(R.id.btn_aceptar);
        Button btn_cancelar = view.findViewById(R.id.btn_cancelar);

        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertConfirmation.dismiss();

                Fragment_remesas_2 fragment = Fragment_remesas_2.newInstance(response.getQpay_object()[0]);
                getContext().setFinishBack(function);
                fragment.function = function;

                getContext().setFragment(fragment);

            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertConfirmation.dismiss();
                //Se reinicia la vista
                resetView();
            }
        });

        builder.setView(view);
        builder.setCancelable(false);

        alertConfirmation = builder.create();
        alertConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertConfirmation.show();

        builder = null;

    }

    //Servicio de consulta de remesa
    private void queryRemittance(){

        getContext().loading(true);

        try {
            final TC_QueryRemittancePetition petition = new TC_QueryRemittancePetition();

            petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            petition.setAccountNumber(campos.get(0).getText());
            petition.getBeneficiario().setPrimerNombre(campos.get(1).getText());
            petition.getBeneficiario().setSegundoNombre(campos.get(2).getText());
            petition.getBeneficiario().setPrimerApellido(campos.get(3).getText());
            petition.getBeneficiario().setSegundoApellido(campos.get(4).getText());

            IRemesas service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {

                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new TC_QueryRemittanceResponse.TC_QueryRemittanceResponseExcluder()).create();
                        String json = gson.toJson(result);
                        response = gson.fromJson(json, TC_QueryRemittanceResponse.class);
                        Log.d("CONSULTA_INFO_REMESA", json);

                        if (response.getQpay_response().equals("true")) {
                            AppPreferences.resetRemesasErrorCounter();
                            btn_continuar.setText("PROCEDER AL PAGO");
                            response.getQpay_object()[0].getRemesa().setAccountNumber(petition.getAccountNumber());
                            enabledDisableEntryFields(false);
                            hideShowResume(false);
                        } else {
                            if(!response.getQpay_description().trim().equals("Remesa Pagada"))
                                AppPreferences.addRemesasErrorCounter();

                            getContext().loading(false);
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description(), new IFunction() {
                                @Override
                                public void execute(Object[] data1) {
                                    if(AppPreferences.isRemesasModuleBlocked()){
                                        AppPreferences.setDateRemesasModuleBlocket(Utils.getTimestampToBlockRemesas());
                                        getContextMenu().alert(String.format(getResources().getString(R.string.alert_message_remesas_3), AppPreferences.getDateRemesasModuleBlocket()), new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {
                                                getContextMenu().initHome();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            service.queryRemittance(petition);

        } catch (Exception e) {

            getContext().loading(false);

            e.printStackTrace();
            getContext().alert(R.string.general_error_catch);
        }

    }

    //Servicio de consulta de transacciones de remesas
    private void getRemittances(){

        getContext().loading(true);

        try {
            final QPAY_Seed petition = new QPAY_Seed();

            petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IRemesas service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {

                        getContext().alert(R.string.general_error);

                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new TC_GetRemittanceResponse.TC_GetRemittanceResponseExcluder()).create();
                        String json = gson.toJson(result);
                        getRemittanceResponse = gson.fromJson(json, TC_GetRemittanceResponse.class);
                        Log.d("CONSULTA_DE_REMESAS", json);

                        showRemittances();

                        if (getRemittanceResponse.getQpay_response().equals("true")) {

                            if(getRemittanceResponse.getQpay_object().length != 0){
                                //Se muestran las remesas pagadas.
                                showRemittances();
                            }else{
                                getContext().alert("No hay transacciones disponibles.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });
                            }
                        } else {
                            getContext().loading(false);
                            getContextMenu().validaSesion(getRemittanceResponse.getQpay_code(), getRemittanceResponse.getQpay_description());
                        }

                    }

                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            service.getRemittances(petition);

        } catch (Exception e) {

            getContext().loading(false);

            e.printStackTrace();
            getContext().alert(R.string.general_error_catch);
        }
    }

    private void showRemittances(){
        this.context = getContextMenu();

        remittances = new ArrayList();

        for(int i=0; i<getRemittanceResponse.getQpay_object().length; i++)
            remittances.add(getRemittanceResponse.getQpay_object()[i]);

        ArrayAdapter adapter = new Fragment_remesas_1.RemittancesAdapter(getContextMenu(), remittances);
        list_trx.setAdapter(adapter);
    }

    public class RemittancesAdapter extends ArrayAdapter<TC_RemittanceTrx> {

        public RemittancesAdapter(Context context, List<TC_RemittanceTrx> trxs) {
            super(context, 0, trxs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_remesas_trx, parent, false);
            }

            TC_RemittanceTrx remittanceTrx = getItem(position);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.setFragment(Fragment_remesas_6.newInstance(getItem(position)));
                }
            });

            TextView text_amount    = convertView.findViewById(R.id.text_amount);
            TextView text_date      = convertView.findViewById(R.id.text_date);
            TextView text_fee       = convertView.findViewById(R.id.text_fee);
            TextView text_id        = convertView.findViewById(R.id.text_id);

            text_amount.setText("-" + Utils.paserCurrency(remittanceTrx.getAmount()));
            text_date.setText(getDate(remittanceTrx.getCreatedAt()));
            text_fee.setText(String.format("Comisión +%s %s", Utils.paserCurrency(remittanceTrx.getCommission()), remittanceTrx.getMonedaPago()));
            text_id.setText(String.format("ID: %s", remittanceTrx.getTransactionId()));

            return convertView;
        }

        private String getDate(String timestamp){
            String back = "";
            String[] array = timestamp.split(" ");

            back = array[0];

            return back;
        }
    }

}
