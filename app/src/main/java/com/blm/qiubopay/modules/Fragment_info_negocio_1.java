package com.blm.qiubopay.modules;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.sepomex.StateInfo;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

//import com.example.android.multidex.myCApplication.R;

public class Fragment_info_negocio_1 extends HFragment implements IMenuContext {


    private QPAY_NewUser data;

    private ArrayList<CViewEditText> campos;
    //private ArrayList<RadioButton> options;

    public static Fragment_info_negocio_1 newInstance(Object... data) {
        Fragment_info_negocio_1 fragment = new Fragment_info_negocio_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_info_negocio_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_info_negocio_1, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment(){

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ImageView help_pin = getView().findViewById(R.id.help_pin);
        help_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().alert("Se muestran datos de información comercio");
            }
        });

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombre_comercio))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(19)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_1)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_calle_comercio))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(70)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_2)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_exterior))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_4)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_interior))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.negocio_info_3)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));


        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_estado))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(25)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_6)
                .setAlert(R.string.text_input_required)
                .setText(getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state()).getState_name().toUpperCase()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_codigo_postal))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(5)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_5)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colonia))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_8)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_municipio))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.negocio_info_7)
                .setAlert(R.string.text_input_required)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id_bimbo))
                .setEnabled(false)
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.NONE)
                .setAlert(R.string.text_input_required)
                .setHorizontalScroll()
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id()));

    }


    /**
     * Retorna la descripcion en base al id del Estado
     * @param codeOrName
     * @return
     */
    private StateInfo getStateInfo(String codeOrName) {

        try {
            Integer code = Integer.parseInt(codeOrName.trim());
            codeOrName = String.format("%02d",code);
        } catch (Exception e) {
            Log.e("info_negocio_1",e.getMessage());
        }

        codeOrName = (codeOrName==null?"":codeOrName.trim().toUpperCase());

        ArrayList<StateInfo> stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        for (StateInfo state:stateList){
            if(state.getState_code().compareTo(codeOrName)==0){
                return state;
            }
            if(state.getState_name().trim().toUpperCase().compareTo(codeOrName)==0){
                return state;
            }
        }

        return new StateInfo("","");
    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

