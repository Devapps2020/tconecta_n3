package com.blm.qiubopay.modules.fincomun.codi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Response.Apertura.FCConsultaBanderaFCILAperturaResponse;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;

public class Fragment_codi_cuentas_1 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;
    private Object data;
    private Button btn_codi;

    public static FCConsultaBanderaFCILAperturaResponse banderas;

    public static Fragment_codi_cuentas_1 newInstance(Object... data) {
        Fragment_codi_cuentas_1 fragment = new Fragment_codi_cuentas_1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_cuentas_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        ListView list_prestamos = getView().findViewById(R.id.list_cuentas);
        ArrayAdapter adapter = new CuentasAdapter(getContext(), SessionApp.getInstance().getCuentasCoDi());
        list_prestamos.setAdapter(adapter);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class CuentasAdapter extends ArrayAdapter<DHCuenta> {

        List<DHCuenta> cuentas;

        public CuentasAdapter(Context context, List<DHCuenta> data) {
            super(context, 0, data);
            cuentas = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cuenta, parent, false);

            DHCuenta cuenta = cuentas.get(position);

            TextView text_nombre = convertView.findViewById(R.id.text_nombre);
            TextView text_numero = convertView.findViewById(R.id.text_numero);
            TextView text_clabe = convertView.findViewById(R.id.text_clabe);
            TextView text_saldo = convertView.findViewById(R.id.text_saldo);

            text_nombre.setText("" + cuenta.getNombreCuenta());
            text_numero.setText("Número: " + cuenta.getNumeroCuenta());
            text_clabe.setText("CLABE: " + cuenta.getClabeCuenta());
            text_saldo.setText("Saldo:\n" + Utils.paserCurrency(cuenta.getSaldo()));

            return convertView;
        }

    }

}
