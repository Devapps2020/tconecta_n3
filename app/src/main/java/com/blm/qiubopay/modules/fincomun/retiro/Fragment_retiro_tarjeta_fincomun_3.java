package com.blm.qiubopay.modules.fincomun.retiro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.com.fincomun.origilib.Http.Response.Retiro.FCRetiroSinTarjetaResponse;

public class Fragment_retiro_tarjeta_fincomun_3 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;

    public static FCRetiroSinTarjetaResponse response;

    public static Fragment_retiro_tarjeta_fincomun_3 newInstance(Object... data) {
        Fragment_retiro_tarjeta_fincomun_3 fragment = new Fragment_retiro_tarjeta_fincomun_3();
        Bundle args = new Bundle();

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

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_prestamos_fincomun_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_retiro_tarjeta_fincomun_3, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };

        Date myDate = new Date();

        TextView text_clave = view.findViewById(R.id.text_clave);
        TextView fecha_disponibilidad = view.findViewById(R.id.textview_fecha_disponibilidad);
        TextView fecha_vencimiento  = view.findViewById(R.id.textview_fecha_vencimiento);
        fecha_disponibilidad.setText(new SimpleDateFormat("dd/MM/yyyy").format(myDate));

        fecha_vencimiento.setText("" + response.getFechaVencimiento());
        text_clave.setText(response.getClave4());

        Button btn_aceptar = view.findViewById(R.id.btn_aceptar);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.initHome();
            }
        });

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

    }


}
