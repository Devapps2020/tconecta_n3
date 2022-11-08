package com.blm.qiubopay.modules.ahorros;

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
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

public class Fragment_ahorros_1 extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;
    Button btn_confirmar;



    public static Fragment_ahorros_1 newInstance() {
        return new Fragment_ahorros_1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_ahorros_1, container, false), R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        campos = new ArrayList();


        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView())
                .showLogo()
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }

        });

        TextView text_saldo = getView().findViewById(R.id.text_saldo);

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_monto))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(3)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

    }


    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()) {
                btn_confirmar.setEnabled(false);
                return;
            }

        if(campos.size() > 0 && !campos.get(0).getText().equals(campos.get(1).getText())){
            campos.get(1).activeError();
            return;
        }

        btn_confirmar.setEnabled(true);
    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}