package com.blm.qiubopay.modules.tienda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.CalcularActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetSalesByRetailerId;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IPromotions;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.PromotionRequest;
import com.blm.qiubopay.models.bimbo.PromotionResponse;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailer;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailerResponse;
import com.blm.qiubopay.modules.Fragment_credito_bimbo_1;
import com.blm.qiubopay.modules.Fragment_depositos_1;
import com.blm.qiubopay.modules.Fragment_ofertas_bimbo_1;
import com.blm.qiubopay.modules.Fragment_proveedor_bimbo_1;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.home.Fragment_menu_pagos_basicos;
import com.blm.qiubopay.modules.home.Fragment_menu_pagos_financieros;
import com.blm.qiubopay.modules.remesas.Fragment_remesas_1;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_tienda_clientes extends HFragment implements IMenuContext {

    public static Fragment_menu_tienda_clientes newInstance() {
        return new Fragment_menu_tienda_clientes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_tienda_cliente, container, false), R.drawable.background_splash_header_1);
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

        CardView card_operaciones_recargas = getView().findViewById(R.id.card_operaciones_recargas);
        CardView card_operaciones_tarjeta = getView().findViewById(R.id.card_operaciones_tarjeta);
        CardView card_remesas = getView().findViewById(R.id.card_remesas);

        card_operaciones_recargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_menu_pagos_basicos.newInstance());
            }
        });

        card_operaciones_tarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_menu_pagos_financieros.newInstance());
            }
        });

        Button btn_cargar_saldo = getView().findViewById(R.id.btn_cargar_saldo);
        btn_cargar_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_depositos_1.newInstance());
            }
        });

        if(null == AppPreferences.getUserProfile().getQpay_object()[0].getRemittance_activation() || AppPreferences.getUserProfile().getQpay_object()[0].getRemittance_activation().equals("0"))
            card_remesas.setVisibility(View.GONE);

        card_remesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppPreferences.isRemesasModuleBlocked()){
                    if(Utils.isFinishedRemesasBlockedTime()){
                        //Terminó el bloqueo
                        AppPreferences.resetRemesasErrorCounter();
                        getContext().setFragment(Fragment_remesas_1.newInstance());
                    }else {
                        //Continua el bloqueo
                        getContextMenu().alert(String.format(getResources().getString(R.string.alert_message_remesas_3), AppPreferences.getDateRemesasModuleBlocket()), new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                getContextMenu().initHome();
                            }
                        });
                    }
                }
                else
                    getContext().setFragment(Fragment_remesas_1.newInstance());
            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}