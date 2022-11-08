package com.blm.qiubopay.modules.ahorros;

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
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

public class Fragment_ahorros_ticket extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;
    Button btn_confirmar;



    public static Fragment_ahorros_ticket newInstance() {
        return new Fragment_ahorros_ticket();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_ahorro_ticket_1, container, false), R.drawable.background_splash_header_2);
    }

    @Override
    public void initFragment() {


        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }

        });

        TextView text_no_cliente = getView().findViewById(R.id.text_no_cliente);
        TextView text_no_cuenta = getView().findViewById(R.id.text_no_cuenta);
        TextView text_tipo_ahorro = getView().findViewById(R.id.text_tipo_ahorro);
        TextView text_monto = getView().findViewById(R.id.text_monto);

        Button btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }



    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}