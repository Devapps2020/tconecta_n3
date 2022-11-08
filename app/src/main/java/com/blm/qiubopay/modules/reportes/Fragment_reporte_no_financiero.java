package com.blm.qiubopay.modules.reportes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.google.gson.Gson;

import mx.devapps.utils.components.HFragment;

public class Fragment_reporte_no_financiero extends HFragment implements IMenuContext {

    private String saldo_bolsa, saldo_transacciones;

    public static Fragment_reporte_no_financiero newInstance(String saldo_transacciones, String saldo_bolsa) {
        Fragment_reporte_no_financiero fragment = new Fragment_reporte_no_financiero();
        Bundle args = new Bundle();

        if(saldo_transacciones != null)
            args.putString("Fragment_reporte_no_financiero", new Gson().toJson(saldo_transacciones));

        if(saldo_bolsa != null)
            args.putString("Fragment_reporte_no_financiero_2", new Gson().toJson(saldo_bolsa));

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_reporte_no_financiero newInstance() {
        return new Fragment_reporte_no_financiero();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_reporte_no_financiero, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        saldo_transacciones = "";
        saldo_bolsa         = "";

        if (getArguments() != null)
            saldo_transacciones = getArguments().getString("Fragment_reporte_no_financiero");

        if (getArguments() != null)
            saldo_bolsa = getArguments().getString("Fragment_reporte_no_financiero_2");


        CViewMenuTop.create(getView()).showLogo().setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        TextView tvTitle                    = getView().findViewById(R.id.tv_title);
        TextView text_establece             = getView().findViewById(R.id.text_establece);
        TextView edit_importe_saldo         = getView().findViewById(R.id.edit_importe_saldo);
        TextView edit_importe_transacciones = getView().findViewById(R.id.edit_importe_transacciones);

        edit_importe_transacciones.setText(saldo_transacciones.replace("\"",""));

        LinearLayout layout_saldo = getView().findViewById(R.id.layout_saldo);

        if(saldo_bolsa != null){//Consulta VAS
            tvTitle.setText(R.string.text_operativas_7);
            layout_saldo.setVisibility(View.VISIBLE);
            edit_importe_saldo.setText(saldo_bolsa.replace("\"",""));

        }else{//Consulta financiera
            tvTitle.setText(R.string.text_operativas_8);
            text_establece.setText(getContext().getResources().getString(R.string.text_pedidos_72));
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}