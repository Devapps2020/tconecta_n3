package com.blm.qiubopay.modules.servicio;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
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
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.tae.QPAY_TaeSaleResponseFirst;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_numero;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_ticket;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_servicio_monto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_servicio_monto extends HFragment implements IMenuContext {

    private static final String TAG = "pago_servicio_monto";

    private ArrayList<CViewEditText> campos = null;

    private CompaniaDTO companiaDTO;
    private QPAY_ServicePayment servicePayment;

    private Button btn_continuar;

    public Fragment_pago_servicio_monto() {
        // Required empty public constructor
    }

    public static Fragment_pago_servicio_monto newInstance(Object... data) {
        Fragment_pago_servicio_monto fragment = new Fragment_pago_servicio_monto();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_servicio_monto_1", new Gson().toJson(data[0]));
        args.putString("Fragment_pago_servicio_monto_2", new Gson().toJson(data[1]));
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
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_monto_1"),CompaniaDTO.class);
            servicePayment = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_monto_2"),QPAY_ServicePayment.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_monto, container, false), R.drawable.background_splash_header_1);
    }


    public void initFragment() {

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .showTitle(getString(R.string.text_servicio_title))
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        CViewSaldo.create(getContextMenu(), getView(), true);

        ImageView ivCompania = getView().findViewById(R.id.iv_compania);
        ivCompania.setImageDrawable(getResources().getDrawable(companiaDTO.getImage()));

        configScreen();

    }


    public void configScreen() {

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                if(!campos.isEmpty())
                    validate(text);
            }
        };

        final TextView txt_referencia = getView().findViewById(R.id.txt_referencia);

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_monto))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint(R.string.text_pago_servicios_90_1)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setTextDecimal(servicePayment.getQpay_amount()!=null?servicePayment.getQpay_amount():"0"));

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        String referencia = servicePayment.getQpay_account_number()!=null ? servicePayment.getQpay_account_number() : "";

        referencia = referencia.concat(servicePayment.getQpay_account_number1()!=null
                && !servicePayment.getQpay_account_number1().isEmpty() ? servicePayment.getQpay_account_number1() : "");

        referencia = referencia.concat(servicePayment.getQpay_account_number2()!=null
                && !servicePayment.getQpay_account_number2().isEmpty() ? " " + servicePayment.getQpay_account_number2() : "");

        referencia = referencia.concat(servicePayment.getQpay_account_number3()!=null
                && !servicePayment.getQpay_account_number3().isEmpty() ? " " + servicePayment.getQpay_account_number3() : "");

        switch (servicePayment.getQpay_product()) {

            case Globals.DISH_ID:
            case Globals.CEA_QRO_ID:
                referencia = referencia.concat("\n\n"+servicePayment.getQpay_name_client());
                break;

            case Globals.STARTV_ID:
            case Globals.NETWAY_ID:
                referencia = referencia.concat("\n\n"+servicePayment.getQpay_name_client());
                campos.get(0).setEnabled(false);
                break;
                
            case Globals.CFE_ID:
            case Globals.OPDM_ID:
                campos.get(0).setEnabled(false);
                break;
                
            case Globals.MEGACABLE_ID:
                campos.get(0).setEnabled(servicePayment.getQpay_account_number().length() == 10 ? false : true);
                break;


            case Globals.VEOLIA_ID:
                campos.get(0).setType(CViewEditText.TYPE.CURRENCY_SD);
                break;

        }

        txt_referencia.setText(referencia);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String monto = campos.get(0).getTextDecimal();
                servicePayment.setQpay_amount(monto);

                getContext().setFragment(Fragment_pago_servicio_confirma.newInstance((CompaniaDTO)companiaDTO,(QPAY_ServicePayment)servicePayment));

            }
        });

        validate("");

    }


    public void validate(String text) {

        btn_continuar.setEnabled(false);

        for(CViewEditText edit: campos)
            if(!edit.isValid())
                return;

        btn_continuar.setEnabled(true);

    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}