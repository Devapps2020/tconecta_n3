package com.blm.qiubopay.modules.ahorros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class Fragment_datos_cuenta extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;
    Button btn_confirmar;



    public static Fragment_datos_cuenta newInstance() {
        return new Fragment_datos_cuenta();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_datos_cuenta, container, false), R.drawable.background_splash_header_2);
    }

    @Override
    public void initFragment() {


        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .showLogo()
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }

        });

        TextView tv_total_available = getView().findViewById(R.id.tv_total_available);
        TextView iban_number = getView().findViewById(R.id.iban_number);
        TextView text_clabe = getView().findViewById(R.id.text_clabe);

        ImageButton img_more_1 = getView().findViewById(R.id.img_more_1);
        ImageButton img_more_2 = getView().findViewById(R.id.img_more_2);

        Button btn_ahorrar = getView().findViewById(R.id.btn_ahorrar);

        btn_ahorrar.setOnClickListener(new View.OnClickListener() {
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