package com.blm.qiubopay.modules.regional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_StartTxn_object;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import mx.devapps.utils.components.HFragment;


public class Fragment_pago_regional_ticket extends HFragment implements IMenuContext {


    private Object data;
    public static QPAY_StartTxn_object response;

    public static Fragment_pago_regional_ticket newInstance(Object... data) {
        Fragment_pago_regional_ticket fragment = new Fragment_pago_regional_ticket();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_pago_qiubo_4", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_regional_ticket, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().initHome();
            }
        });

        TextView timestamp = getView().findViewById(R.id.timestamp);
        TextView terminalRef = getView().findViewById(R.id.terminalRef);
        TextView claveQiubo = getView().findViewById(R.id.claveQiubo);
        TextView amount = getView().findViewById(R.id.amount);
        TextView transRef = getView().findViewById(R.id.transRef);
        TextView tienda = getView().findViewById(R.id.tienda);

        timestamp.setText(response.getTimestamp());
        terminalRef.setText(response.getTerminalRef());
        claveQiubo.setText(response.getClaveQiubo());
        amount.setText(Utils.paserCurrency(response.getAmount()));
        //trxIdChannel.setText(response.getTrxIdChannel());
        transRef.setText(response.getTransRef());
        tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());

        Button btn_terminar = getView().findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        Button btn_compartir = getView().findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Utils.shareVoucherPagosQiubo(getContext(), response.getClaveQiubo(), response.getTransRef());
            }
        });

        btn_compartir.setEnabled(true);

        Button btn_ver = getView().findViewById(R.id.btn_ver);
        btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlToLoad = Globals.HOST + "/api/v1/c/ticketConcentra?c=" + response.getClaveQiubo() + "&tr=" + response.getTransRef();
                getContext().setFragment(Fragment_browser.newInstance(urlToLoad));

            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

