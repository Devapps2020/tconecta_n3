package com.blm.qiubopay.modules.fincomun.retiro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.com.fincomun.origilib.Http.Request.Retiro.FCRetiroSinTarjetaRequest;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCRetiroSinTarjetaResponse;
import mx.com.fincomun.origilib.Model.Retiro.RetiroSinTarjeta;

public class Fragment_retiro_tarjeta_fincomun_2 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;

    public static FCRetiroSinTarjetaRequest fcRetiroSinTarjetaRequest;
    public static boolean paraAlguienMas;
    public static String nombre;


    public static Fragment_retiro_tarjeta_fincomun_2 newInstance(Object... data) {
        Fragment_retiro_tarjeta_fincomun_2 fragment = new Fragment_retiro_tarjeta_fincomun_2();
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

        view = inflater.inflate(R.layout.fragment_retiro_tarjeta_fincomun_2, container, false);

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

        LinearLayout layout_alguien_mas = view.findViewById(R.id.layout_alguien_mas);



        TextView numero_alguien1 = view.findViewById(R.id.textview_numero_alguien_1);
        TextView numero_alguien2  = view.findViewById(R.id.textview_numero_alguien_2);
        TextView monto_retiro = view.findViewById(R.id.textview_monto_retiro);
        TextView concepto_retiro = view.findViewById(R.id.textview_concepto_retiro);
        TextView fecha_operacion = view.findViewById(R.id.textview_fecha_operacion);
        Button confirma_retiro = view.findViewById(R.id.button_confirmacion_retiro);
        Button btn_regresar = view.findViewById(R.id.btn_regresar);

        TextView textview_cuenta = view.findViewById(R.id.textview_cuenta);
        TextView textview_nombre = view.findViewById(R.id.textview_nombre);


        if(paraAlguienMas) {
            layout_alguien_mas.setVisibility(View.VISIBLE);
            numero_alguien1.setText(fcRetiroSinTarjetaRequest.getBeneficiario());
            numero_alguien2.setText(fcRetiroSinTarjetaRequest.getCelular());
        } else
            layout_alguien_mas.setVisibility(View.GONE);

        Date myDate = new Date();

        monto_retiro.setText(Utils.paserCurrency("" + fcRetiroSinTarjetaRequest.getMonto()));
        concepto_retiro.setText(fcRetiroSinTarjetaRequest.getConcepto());
        fecha_operacion.setText(new SimpleDateFormat("dd/MM/yyyy").format(myDate));

        textview_cuenta.setText(fcRetiroSinTarjetaRequest.getNumeroCuenta());
        textview_nombre.setText(nombre);

        confirma_retiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getTokenFC((String... text) -> {
                    retiroSinTarjeta(text[0]);
                });
            }
        });

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.backFragment();
            }
        });

    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }


    }

    public void retiroSinTarjeta(String token) {

        fcRetiroSinTarjetaRequest.setTokenJwt(token);

        if(!paraAlguienMas) {

            String name = "" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name() +
            " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_father_surname() +
            " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mother_surname();

            fcRetiroSinTarjetaRequest.setBeneficiario(name);
            fcRetiroSinTarjetaRequest.setCelular("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        }

        Logger.d("REQUEST : "  + new Gson().toJson(fcRetiroSinTarjetaRequest));

        RetiroSinTarjeta retiroSinTarjeta = new RetiroSinTarjeta(context);
        retiroSinTarjeta.retiroSinTarjeta(fcRetiroSinTarjetaRequest, new RetiroSinTarjeta.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCRetiroSinTarjetaResponse response = (FCRetiroSinTarjetaResponse)Object;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                context.loading(false);

                if(response.getClave4() == null || response.getClave4().isEmpty()) {
                    context.alert("Error al disponer el dinero");
                    return;
                }

                Fragment_retiro_tarjeta_fincomun_3.response = response;
                context.setFragment(Fragment_retiro_tarjeta_fincomun_3.newInstance());

            }
            @Override
            public void onFailure(String mensaje) {
                context.loading(false);
                context.alert(mensaje);
            }
        });

    }



}
