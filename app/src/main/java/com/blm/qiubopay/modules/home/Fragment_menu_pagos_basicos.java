package com.blm.qiubopay.modules.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentItem;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_menu;
import com.blm.qiubopay.modules.regional.Fragment_pago_regional_menu;
import com.blm.qiubopay.modules.servicio.Fragment_pago_servicio_menu;

import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_pagos_basicos extends HFragment implements IMenuContext {

    public static Fragment_menu_pagos_basicos newInstance() {
        return new Fragment_menu_pagos_basicos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_pagos_basicos, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        TextView text_saldo = getView().findViewById(R.id.text_saldo);
        TextView text_saldo_pagos = getView().findViewById(R.id.text_saldo_pagos);
        CardView card_recargas = getView().findViewById(R.id.card_recargas);
        CardView card_servicios = getView().findViewById(R.id.card_servicios);
        CardView card_pagos = getView().findViewById(R.id.card_pagos);

        text_saldo.setText(getContextMenu().getSaldo());

        card_recargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_pago_recarga_menu.newInstance());
            }
        });

        card_servicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_pago_servicio_menu.newInstance());

            }
        });

        card_pagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContextMenu().getQiuboPaymentList(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        Fragment_pago_regional_menu.proveedores = (List<QPAY_QiuboPaymentItem>) data[0];
                        getContext().setFragment(Fragment_pago_regional_menu.newInstance());
                    }
                });

            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}