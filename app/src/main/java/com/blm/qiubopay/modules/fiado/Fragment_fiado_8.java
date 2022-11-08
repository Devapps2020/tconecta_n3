package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;

public class Fragment_fiado_8 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    public static boolean efectivo;
    private ArrayList<CViewEditText> campos;


    public static Fragment_fiado_8 newInstance(Object... data) {
        Fragment_fiado_8 fragment = new Fragment_fiado_8();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_fiado_8, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        campos = new ArrayList();



        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate(text);
            }
        };


        campos.add(CViewEditText.create(view.findViewById(R.id.edit_balance))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setSuffix("MXN")
                .setHint(R.string.text_fiado_21)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        Button btn_send_receipt = view.findViewById(R.id.btn_send_receipt);
        Button btn_finish = view.findViewById(R.id.btn_finish);

        btn_send_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setFragment(Fragment_fiado_9.newInstance());
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().initHome();
            }
        });



        Double deuda = Double.parseDouble(Fragment_fiado_7.deuda);
        Double monto = Double.parseDouble(Fragment_fiado_7.monto);
        deuda = deuda - monto;

        if(deuda < 0)
            deuda = 0.0;

        campos.get(0).setText(Utils.paserCurrency(deuda + ""));
        campos.get(0).setEnabled(false);

        Fragment_fiado_7.monto = null;
        Fragment_fiado_7.deuda = null;
        Fragment_fiado_6.fiado = null;

        TextView text_tipo_pago = view.findViewById(R.id.text_tipo_pago);
        text_tipo_pago.setText(efectivo ? "Pago realizado en efectivo" : "Pago realizado con tarjeta");

    }

    public void validate(String text) {



    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

