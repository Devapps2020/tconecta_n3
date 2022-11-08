package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.fiado.QPAY_Fiado;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_6 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;


    private ArrayList<CViewEditText> campos;


    Button btn_pay_debt;
    Button btn_send_reminder;

    public static QPAY_Fiado fiado;

    public static Fragment_fiado_6 newInstance(Object... data) {
        Fragment_fiado_6 fragment = new Fragment_fiado_6();
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

        view = inflater.inflate(R.layout.fragment_fiado_6, container, false);

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


        btn_pay_debt = view.findViewById(R.id.btn_pay_debt);
        btn_send_reminder = view.findViewById(R.id.btn_send_reminder);


        campos.add(CViewEditText.create(view.findViewById(R.id.edit_detail))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(50)
                .setType(CViewEditText.TYPE.MULTI_TEXT)
                .setHint(R.string.text_fiado_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_legit_amount))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(25)
                .setSuffix("MXN")
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.text_fiado_18)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(view.findViewById(R.id.edit_total_debt))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(25)
                .setSuffix("MXN")
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.text_fiado_19)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        campos.get(0).setEnabled(false);
        campos.get(1).setEnabled(false);
        campos.get(2).setEnabled(false);


        campos.get(0).setText(fiado.getFiado_desc());
        campos.get(1).setText(Utils.paserCurrency(fiado.getFiado_debt_amount()));
        campos.get(2).setText(Utils.paserCurrency(Fragment_fiado_5.deudaTotal));

        btn_pay_debt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_fiado_7.deuda = fiado.getFiado_debt_amount();
                context.setFragment(Fragment_fiado_7.newInstance());
            }
        });

        btn_send_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_fiado_5.recordatorio(context, fiado.getFiado_debt_id(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Recordatorio\nEnviado");
                    }
                });
            }
        });

        TextView txt_date = view.findViewById(R.id.txt_date);
        txt_date.setText(Html.fromHtml("Fecha<br><b>" + fiado.getFiado_creation_date() + "</b>"));

    }

    public void validate(String text) {

        btn_pay_debt.setEnabled(false);
        btn_send_reminder.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid())
                return;

        btn_pay_debt.setEnabled(true);
        btn_send_reminder.setEnabled(true);

    }



    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

