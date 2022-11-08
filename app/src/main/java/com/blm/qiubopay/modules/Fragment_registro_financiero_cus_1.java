package com.blm.qiubopay.modules;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.utils.SessionApp;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_registro_financiero_cus_1 extends HFragment {

    public static Fragment_registro_financiero_cus_1 newInstance(Object... data) {
        Fragment_registro_financiero_cus_1 fragment = new Fragment_registro_financiero_cus_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_cus_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_registro_financiero_cus_1, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        //HLocation.start(true, getContext());

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        SessionApp.getInstance().setFinancialInformation(null);

        Button option_1 = getView().findViewById(R.id.option_1);
        Button option_2 = getView().findViewById(R.id.option_2);

        option_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //20200317 RSB. New Financial Onboarding
                //Se borra validación de registro completo y envía directo a Registrar cuenta

                getContext().setFragment(Fragment_registro_financiero_cus_2.newInstance());

            }
        });

        option_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //20200317 RSB. New Financial Onboarding
                //Se borra validación de registro completo y envía directo a Abrir cuenta
                getContext().setFragment(Fragment_registro_financiero_cus_3.newInstance());
            }
        });


    }

}

