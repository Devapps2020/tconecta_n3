package com.blm.qiubopay.modules.fincomun.basica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.listeners.IMenuContext;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

public class Fragment_cuenta_basica_fincomun_7 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;

    public static Fragment_cuenta_basica_fincomun_7 newInstance(Object... data) {
        Fragment_cuenta_basica_fincomun_7 fragment = new Fragment_cuenta_basica_fincomun_7();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_basica_fincomun_7, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        TextView text_account_number = getView().findViewById(R.id.text_number_account);
        TextView text_customer_number = getView().findViewById(R.id.text_customer_number);

        text_account_number.setText(Fragment_cuenta_basica_fincomun_5.response.getNumCuenta());
        text_customer_number.setText(Fragment_cuenta_basica_fincomun_5.response.getNumCliente());

        Button btn_accept = getView().findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
