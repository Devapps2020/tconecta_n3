package com.blm.qiubopay.modules.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;

import mx.devapps.utils.components.HFragment;

public class Fragment_menu_pagos_financieros extends HFragment implements IMenuContext {

    public static Fragment_menu_pagos_financieros newInstance() {
        return new Fragment_menu_pagos_financieros();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_pagos_financeros, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CardView card_pagos_tarjeta = getView().findViewById(R.id.card_pagos_tarjeta);
        CardView card_retiro_efectivo = getView().findViewById(R.id.card_retiro_efectivo);
        CardView card_cancelaciones = getView().findViewById(R.id.card_cancelaciones);

        card_pagos_tarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().servicioFinanciero(1);
            }
        });

        card_retiro_efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().servicioFinanciero(2);
            }
        });

        card_cancelaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().servicioFinanciero(0);
            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}