package com.blm.qiubopay.modules.fincomun.adelantoVenta;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_1;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_4;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_oferta;
import com.google.gson.Gson;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;

public class Fragment_adelanto_venta_simulador  extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private Object data;
    private Button btn_next;

    public static Fragment_adelanto_venta_simulador newInstance() {
        Fragment_adelanto_venta_simulador fragment = new Fragment_adelanto_venta_simulador();

        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_adelanto_venta_simulador"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_adelanto_venta_simulador, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fincomun_contratacion_1.type = Fragment_fincomun_oferta.Type.ADELANTO_VENTAS;
                getContext().setFragment(Fragment_fincomun_contratacion_1.newInstance());
            }
        });
    }
}