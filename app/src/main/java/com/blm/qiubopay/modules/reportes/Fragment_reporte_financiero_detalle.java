package com.blm.qiubopay.modules.reportes;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.model.FINANCIAL_BD_ROW;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.reportes.FinancialReportTxn;
import com.blm.qiubopay.models.visa.response.QPAY_VisaEmvResponse;
import com.blm.qiubopay.modules.Fragment_transacciones_5;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;

import mx.devapps.utils.interfaces.IAlertButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_reporte_financiero_detalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_reporte_financiero_detalle extends HFragment {

    private View view;
    private MenuActivity context;

    FinancialReportTxn txn;

    public static Fragment_reporte_financiero_detalle newInstance(FinancialReportTxn data) {
        Fragment_reporte_financiero_detalle fragment = new Fragment_reporte_financiero_detalle();
        Bundle args = new Bundle();

        if(data != null)
            args.putString("Fragment_reporte_financiero_detalle", new Gson().toJson(data));

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
            txn = new Gson().fromJson(getArguments().getString("Fragment_reporte_financiero_detalle"), FinancialReportTxn.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_view_detalle_transaccion, container, false);

        setView(view);

        try {
            initFragment();
        } catch (Exception e) {
            e.printStackTrace();
            getContext().alert("Información no disponible", new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    context.initHome();
                }
            });
        }

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
        TextView text_title = view.findViewById(R.id.text_title);
        TextView txt_monto = view.findViewById(R.id.monto_recarga);
        TextView txt_referencia = view.findViewById(R.id.numero_recarga);
        TextView txt_fecha = view.findViewById(R.id.fecha_recarga);
        ImageView img_tarjeta = view.findViewById(R.id.img_tarjeta);
        TextView text_label_tarjeta = view.findViewById(R.id.text_label_tarjeta);
        TextView txt_tarjeta = view.findViewById(R.id.autorizacion_recarga);
        Button btn_compartir = view.findViewById(R.id.btn_compartir);

        btn_compartir.setText(R.string.accept_button);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().backFragment();
            }
        });

        String amount = txn.getImporte()!=null ? txn.getImporte().toString() : "";
        amount = !amount.isEmpty() ? Utils.paserCurrency(amount) :"";

        img_service_transaction.setImageDrawable(context.getDrawable(R.drawable.icons_cobro_con_tarjeta));
        text_title.setText("MONTO");
        txt_monto.setText(amount);
        txt_referencia.setText(txn.getAutorizacion());
        txt_fecha.setText(txn.getFechaHora());
        text_label_tarjeta.setText("Tarjeta");
        img_tarjeta.setImageDrawable(context.getDrawable(R.drawable.icons_cobro_con_tarjeta));
        txt_tarjeta.setText("**** **** **** " + txn.getTarjeta());


    }


}