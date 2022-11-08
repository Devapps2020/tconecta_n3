package com.blm.qiubopay.modules.fincomun.prestamo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.modules.fincomun.retiro.Fragment_retiro_tarjeta_fincomun_1;
import com.blm.qiubopay.modules.fincomun.retiro.Fragment_retiro_tarjeta_fincomun_2;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Retiro.FCConsultaCuentasRequest;
import mx.com.fincomun.origilib.Http.Request.Retiro.FCRetiroSinTarjetaRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCConsultaCreditosResponse;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Model.Retiro.ConsultaCuentas;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaCreditos;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaPagosCreditos;

public class Fragment_prestamos_detalle_fincomun_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;
    private FCConsultaCreditosResponse response = SessionApp.getInstance().getFcConsultaCreditosResponse();
    private DHListaCreditos credito = SessionApp.getInstance().getDhListaCreditos();

    public static Fragment_prestamos_detalle_fincomun_1 newInstance(Object... data) {
        Fragment_prestamos_detalle_fincomun_1 fragment = new Fragment_prestamos_detalle_fincomun_1();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_prestamos_detalle_fincomun_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_prestamos_detalle_fincomun_1, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };

        Button btn_how_paymant = view.findViewById(R.id.btn_how_payment);
        LinearLayout linear_view_more = view.findViewById(R.id.linear_view_more);
        TextView tex_view_more = view.findViewById(R.id.tex_view_more);
        TextView nombre_cliente = view.findViewById(R.id.textview_nombre_cliente);
        TextView numero_cliente = view.findViewById(R.id.textview_numero_cliente);
        TextView tipo_cuenta = view.findViewById(R.id.textview_tipocuenta_creditocontratato);
        TextView numero_credito = view.findViewById(R.id.textview_numero_credito);
        TextView clabe_credito = view.findViewById(R.id.textview_clabe_credito);
        TextView limite_operacional_mensual = view.findViewById(R.id.textview_limite_operacional_mensual);
        TextView servicios_contratados = view.findViewById(R.id.textview_servicios_contratados);
        TextView fecha_contratacion = view.findViewById(R.id.textview_fecha_hora_contratacion);
        TextView fecha_desembolso_credito = view.findViewById(R.id.textview_fecha_desembolso_credito);
        TextView text_fecha_limite = view.findViewById(R.id.text_fecha_limite);
        TextView text_monto = view.findViewById(R.id.text_monto);

        numero_cliente.setText("" + SessionApp.getInstance().getFcRequestDTO().getNumCliente());
        numero_credito.setText("" + credito.getNumeroCredito());
        clabe_credito.setText("" + credito.getCuentaCable());
        fecha_desembolso_credito.setText("" + credito.getFechaDesembolso().split(" ")[0]);
        text_fecha_limite.setText("" + credito.getFechaLimitePago().split(" ")[0]);
        text_monto.setText(Utils.paserCurrency(credito.getMontoPago()));

        nombre_cliente.setText("");
        tipo_cuenta.setText("");
        limite_operacional_mensual.setText("");
        servicios_contratados.setText("");
        fecha_contratacion.setText("");

        btn_how_paymant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //- Referencia Chedraui: #3

                String referencias = "- Clabe interbancaria: #2\n- Referencia Telecom: #4\n- Referencia BBVA: #5\n     • Convenio  CIE 0715328";

                referencias = referencias.replace("#2", "" + credito.getCuentaCable());
                referencias = referencias.replace("#4", "" + credito.getRefTelecom());
                referencias = referencias.replace("#5", "" + credito.getRefBBVA());

                context.showAlertLayout(R.layout.item_alert_como_pagar, referencias, credito.getRefOxxo(),credito.getRefChedraui());
            }
        });

        tex_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_view_more.setVisibility(View.VISIBLE );
                tex_view_more.setVisibility(View.GONE);
            }
        });

        Button btn_retiro_sin_tarjeta = view.findViewById(R.id.retiro_tarjeta);
        btn_retiro_sin_tarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getTokenFC((String... text) -> {
                    consultaCuentas(text[0]);
                });
            }
        });

        LinearLayout layout_pagos = view.findViewById(R.id.layout_pagos);

        if(response.getListaPagosCreditos() == null) {
            layout_pagos.setVisibility(View.GONE);
        } else {

            layout_pagos.setVisibility(View.VISIBLE);

            ListView list_pagos = view.findViewById(R.id.list_pagos);
            ArrayAdapter adapter = new PagosAdapter(context, response.getListaPagosCreditos());
            list_pagos.setAdapter(adapter);

            Utils.setListViewHeightBasedOnChildren(list_pagos);
        }



    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

    }

    public void consultaCuentas(String token) {

        FCConsultaCuentasRequest fcConsultaCuentasRequest = new FCConsultaCuentasRequest();
        fcConsultaCuentasRequest.setNumCliente(SessionApp.getInstance().getFcConsultaCreditosResponse().getNumClienteSib());
        fcConsultaCuentasRequest.setTokenJwt(token);

        Logger.d("REQUEST : "  + new Gson().toJson(fcConsultaCuentasRequest));

        ConsultaCuentas consultaCuentas = new ConsultaCuentas(context);
        consultaCuentas.consultaCuentas(fcConsultaCuentasRequest, new ConsultaCuentas.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCConsultaCuentasResponse response = (FCConsultaCuentasResponse)Object;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getCuentas() == null || response.getCuentas().isEmpty())
                    context.alert(response.getDescripcion());
                else {

                    if(Double.parseDouble(response.getCuentas().get(0).getSaldo()) == 0) {
                        context.alert("Sin saldo disponible");
                        context.loading(false);
                        return;
                    }


                    context.showAlertRetiro_step_1(new IClickView() {
                        @Override
                        public void onClick(java.lang.Object... data) {

                            context.showAlertRetiro_step_2(new IClickView() {
                                @Override
                                public void onClick(java.lang.Object... data) {

                                    Fragment_retiro_tarjeta_fincomun_1.response = response;
                                    Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest = new FCRetiroSinTarjetaRequest();

                                    Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setBeneficiario("" + data[0]);
                                    Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setCelular("" + data[1]);
                                    Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setCompania("" + data[2]);

                                    context.setFragment(Fragment_retiro_tarjeta_fincomun_1.newInstance());
                                }
                            });
                        }
                    });



                }

                context.loading(false);
            }
            @Override
            public void onFailure(String mensaje) {
                context.loading(false);
                context.alert(mensaje);
            }
        });

    }

    public class PagosAdapter extends ArrayAdapter<DHListaPagosCreditos> {

        List<DHListaPagosCreditos> pagos;

        public PagosAdapter(Context context, List<DHListaPagosCreditos> data) {
            super(context, 0, data);
            pagos = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_pagos, parent, false);

            TextView text_fecha = convertView.findViewById(R.id.text_fecha);
            TextView text_description = convertView.findViewById(R.id.text_description);
            TextView text_monto = convertView.findViewById(R.id.text_monto);

            DHListaPagosCreditos pago = pagos.get(position);

            text_fecha.setText("" + pago.getFechaPago());
            text_description.setText("" + pago.getDescripcionPago());
            text_monto.setText(Utils.paserCurrency("" + pago.getFechaPago()));

            return convertView;
        }

    }

}
