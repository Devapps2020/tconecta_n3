package com.blm.qiubopay.modules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_NewUser;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

public class Fragment_info_personal_1 extends HFragment implements IMenuContext {

    private QPAY_NewUser data;

    private ArrayList<CViewEditText> campos;
    private ArrayList<RadioButton> options;
    private Button btn_confirmar;

    public static Fragment_info_personal_1 newInstance(Object... data) {
        Fragment_info_personal_1 fragment = new Fragment_info_personal_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_info_personal_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_info_personal_1, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_DATOS_PERSONALES_inician);

        campos = new ArrayList();

        options = new ArrayList();

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ImageView help_pin = getView().findViewById(R.id.help_pin);
        help_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().alert("Se muestran datos de información personal");
            }
        });

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombres))
                .setRequired(false)
                .setMinimum(3)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_1)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_apellido_paterno))
                .setRequired(false)
                .setMinimum(3)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_2)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_father_surname()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_apellido_materno))
                .setRequired(false)
                .setMinimum(3)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_3)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mother_surname()));

        String birthDate = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_birth_date();
        if(birthDate != null && !birthDate.isEmpty()) {
            birthDate.replace("-","/");
        }

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_fecha_nacimiento))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_4)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText(birthDate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_correo_electronico))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(70)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_6)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_genero))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(70)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_5)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_gender()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_cliente_bimbo))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_8)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setHorizontalScroll()
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id_agencia))
                .setRequired(false)
                .setMinimum(7)
                .setMaximum(14)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_9)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id()));

        final LinearLayout layoutAccountNumber = getView().findViewById(R.id.layout_account_number);
        String accountNumber = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_account_number();

        if(accountNumber!=null && !accountNumber.isEmpty()){
            layoutAccountNumber.setVisibility(View.VISIBLE);
            campos.add(CViewEditText.create(getView().findViewById(R.id.edit_account_number))
                    .setRequired(false)
                    .setMinimum(5)
                    .setMaximum(30)
                    .setType(CViewEditText.TYPE.NONE)
                    .setHint(R.string.personal_info_11)
                    .setAlert(R.string.text_input_required)
                    .setEnabled(false)
                    .setText(accountNumber));
        } else {
            layoutAccountNumber.setVisibility(View.GONE);
        }

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_telefono))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_7)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id_device))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(20)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.personal_info_10)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_gateway_user()));


        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        LinearLayout layout_id_bimbo = getView().findViewById(R.id.layout_id_bimbo);
        layout_id_bimbo.setVisibility(View.GONE);

        if(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id() != null &&
                !AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id().isEmpty()){

            layout_id_bimbo.setVisibility(View.VISIBLE);
        }

        btn_confirmar.setEnabled(true);

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CApplication.setAnalytics(CApplication.ACTION.CB_DATOS_PERSONALES_finalizan);
                getContext().setFragment(Fragment_registro_2.newInstance(data));
            }
        });

        btn_confirmar.setVisibility(View.GONE);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

