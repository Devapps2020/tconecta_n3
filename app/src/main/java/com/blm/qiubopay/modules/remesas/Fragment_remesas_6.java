package com.blm.qiubopay.modules.remesas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.remesas.TC_RemittanceTrx;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;

import mx.devapps.utils.components.HFragment;

public class Fragment_remesas_6 extends HFragment implements IMenuContext {

    private TC_RemittanceTrx response;
    private Object data;

    public static Fragment_remesas_6 newInstance(Object... data) {
        Fragment_remesas_6 fragment = new Fragment_remesas_6();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_remesas_6", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_remesas_6, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_remesas_6"), Object.class);
    }

    @Override
    public void initFragment() {

        Gson gson = new Gson();
        String queryData = gson.toJson(data);
        response = gson.fromJson(queryData, TC_RemittanceTrx.class);

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        TextView text_categoria     = getView().findViewById(R.id.text_categoria);
        TextView text_remesador     = getView().findViewById(R.id.text_remesador);
        TextView text_remitente     = getView().findViewById(R.id.text_remitente);
        TextView text_monto_origen  = getView().findViewById(R.id.text_monto_origen);
        TextView text_monto_destino = getView().findViewById(R.id.text_monto_destino);
        TextView text_fecha         = getView().findViewById(R.id.text_fecha);
        TextView text_hora          = getView().findViewById(R.id.text_hora);
        TextView text_folio         = getView().findViewById(R.id.text_folio);
        TextView text_tipo_cambio   = getView().findViewById(R.id.text_tipo_cambio);

        text_remesador.setText(response.getRemesador());
        //text_remitente.setText(response.text_monto_orige);
        text_monto_origen.setText(Utils.paserCurrency(""+response.getMontoMonedaOrigen()) + response.getMonedaOrigen());
        text_monto_destino.setText(Utils.paserCurrency(""+response.getMontoMonedaPago()) + response.getMonedaPago());
        text_fecha.setText(getDateOrTime(response.getCreatedAt(),0));
        text_hora.setText(getDateOrTime(response.getCreatedAt(),1));
        text_tipo_cambio.setText("$1.00 USD = " + Utils.paserCurrency(""+response.getTipoCambio()) + response.getMonedaPago());

        text_folio.setText(response.getTransactionId());

        Button btn_terminar = getView().findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

    }

    private String getDateOrTime(String timestamp, int index){
        String back = "";
        String[] array = timestamp.split(" ");

        back = array[index].replace(".0","");

        return back;
    }

}
