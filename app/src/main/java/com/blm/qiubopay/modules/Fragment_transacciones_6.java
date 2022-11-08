package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.database.model.QP_BD_ROW;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.QPAY_Pago_Qiubo_object;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;


public class Fragment_transacciones_6 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;
    public static QPAY_Pago_Qiubo_object response;
    private QP_BD_ROW qp_bd_row;

    public static Fragment_transacciones_6 newInstance(Object... data) {
        Fragment_transacciones_6 fragment = new Fragment_transacciones_6();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_pago_qiubo_4", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if(Globals.LOCAL_TRANSACTION)
                qp_bd_row = new Gson().fromJson(getArguments().getString("Fragment_pago_qiubo_4"), QP_BD_ROW.class);
            else
                data = new Gson().fromJson(getArguments().getString("Fragment_pago_qiubo_4"), Object.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_view_detalle_transaccion, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ImageView img_service_transaction = view.findViewById(R.id.img_service_transaction);
        TextView title = view.findViewById(R.id.text_title);
        TextView amount = view.findViewById(R.id.monto_recarga);
        TextView transRef = view.findViewById(R.id.numero_recarga);
        TextView timestamp = view.findViewById(R.id.fecha_recarga);
        TextView claveQiubo = view.findViewById(R.id.autorizacion_recarga);

        title.setText("Pago regional");


        //TextView terminalRef = view.findViewById(R.id.terminalRef);
        //TextView trxIdChannel = view.findViewById(R.id.trxIdChannel);
       // TextView tienda = view.findViewById(R.id.tienda);

        //img_service_transaction.setImageDrawable(context.getDrawable(R.drawable.icons_pagos_regionales));
        //title.setText("Pagos regionales");

        if(null != qp_bd_row){
            if(null != qp_bd_row.getResponse2()) {
                response = qp_bd_row.getResponse2();
            }
        }


        //tienda.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());

        //Button btn_terminar = view.findViewById(R.id.btn_terminar);
        //btn_terminar.setVisibility(View.GONE);
        /*btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.initHome();
            }
        });*/

        Button btn_compartir = view.findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != response)
                    Utils.shareVoucherPagosQiubo(context, response.getClaveQiubo(), response.getTransRef());
                else
                    Utils.shareVoucherPagosQiubo(context, qp_bd_row.getResponse1().getClaveQiubo(), qp_bd_row.getResponse1().getTransRef());
            }
        });

        btn_compartir.setEnabled(true);

    }

}

