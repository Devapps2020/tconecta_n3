package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import mx.com.fincomun.origilib.Objects.FCil.DHMovimiento;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.components.HFragment;

public class Fragment_fincomun_movimiento_detalle extends HFragment implements IMenuContext {


    private DHMovimiento operacionSeleccionada;
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    private DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private TextView txt_detalle_tipo_operacion, txt_detalle_monto, txt_refnumerica, txt_fecha_operacion;
    private Button btn_descargar;

    public static Fragment_fincomun_movimiento_detalle newInstance(String params){
        Fragment_fincomun_movimiento_detalle fragment = new Fragment_fincomun_movimiento_detalle();
        Bundle args = new Bundle();
        args.putString("param",params);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            // TODO : Deserializar información
            operacionSeleccionada = new Gson().fromJson(getArguments().getString("param"), DHMovimiento.class);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_detalle, container, false),R.drawable.background_splash_header_2);
    }



    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_white)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                })
                .onClickQuestion(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {

                    }
                });
        txt_detalle_tipo_operacion = getView().findViewById(R.id.txt_detalle_tipo_operacion);

        txt_detalle_monto = getView().findViewById(R.id.txt_detalle_monto);
        txt_detalle_monto.setText(currencyFormatter.format(Double.valueOf(operacionSeleccionada.getCantidad())));

        txt_refnumerica = getView().findViewById(R.id.txt_refnumerica);
        txt_refnumerica.setText(operacionSeleccionada.getDescripcion());

        txt_fecha_operacion = getView().findViewById(R.id.txt_fecha_operacion);
        txt_fecha_operacion.setText(operacionSeleccionada.getFecha());

        btn_descargar = getView().findViewById(R.id.btn_descargar);
        btn_descargar.setOnClickListener(i -> {
            Snackbar.make(getView(), "Descargar", Snackbar.LENGTH_LONG).show();
        });
    }
}
