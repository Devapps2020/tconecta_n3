package com.blm.qiubopay.modules.fincomun.enrolamiento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_fincomun_1;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaClienteResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_enrolamiento_fincomun_1 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;
    private Object data;

    public static Fragment_enrolamiento_fincomun_1 newInstance(Object... data) {
        Fragment_enrolamiento_fincomun_1 fragment = new Fragment_enrolamiento_fincomun_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_enrolamiento_fincomun_1, container, false),R.drawable.background_splash_header_1);
    }
    @Override
    public void initFragment(){
        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        EditText edit_id_bimbo = getView().findViewById(R.id.edit_id_bimbo);
        edit_id_bimbo.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
        edit_id_bimbo.setEnabled(false);

        Button btn_continuar = getView().findViewById(R.id.btn_continuar);
        Button btn_cancelar = getView().findViewById(R.id.btn_cancelar);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().loading(true);
                getContextMenu().gethCoDi().validaCliente( AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        FCValidaClienteResponse response = (FCValidaClienteResponse) data[0];
                        Fragment_prestamos_fincomun_1.banderas.setNumCliente(response.getNumCliente());

                        if(response.getCodigo() == 0) {
                            getContext().setFragment(Fragment_prestamos_fincomun_1.newInstance());
                        } else {
                            getContext().setFragment(Fragment_enrolamiento_fincomun_2.newInstance());
                        }

                        getContext().loading(false);

                    }
                });

            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().backFragment();
            }
        });

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
