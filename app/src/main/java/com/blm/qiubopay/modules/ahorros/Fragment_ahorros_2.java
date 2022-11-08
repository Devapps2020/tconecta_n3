package com.blm.qiubopay.modules.ahorros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class Fragment_ahorros_2 extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;
    Button btn_confirmar;



    public static Fragment_ahorros_2 newInstance() {
        return new Fragment_ahorros_2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_ahorros_2, container, false), R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView())
                .showLogo()
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }

        });

        TextView text_no_cuenta = getView().findViewById(R.id.text_no_cuenta);
        TextView text_edit = getView().findViewById(R.id.text_edit);

        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView img_delete = getView().findViewById(R.id.img_delete);

        img_delete.setOnClickListener(new View.OnClickListener() {
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