package com.blm.qiubopay.modules.remesas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.remesas.TC_RemittanceResponse1;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_remesas_2 extends HFragment implements IMenuContext {

    private androidx.cardview.widget.CardView card_ine;
    private androidx.cardview.widget.CardView card_ife;
    private androidx.cardview.widget.CardView card_pasaporte;
    private androidx.cardview.widget.CardView card_ingreso_manual;

    private androidx.appcompat.widget.AppCompatButton btn_cancelar;

    private TC_RemittanceResponse1 remittanceResponse;

    private Object data;

    public IFunction function;

    public static Fragment_remesas_2 newInstance(Object... data) {
        Fragment_remesas_2 fragment = new Fragment_remesas_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_remesas_2", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_remesas_2 newInstance() {
        return new Fragment_remesas_2();
    }

    @Override
    public MenuActivity getContextMenu() {
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_remesas_2, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_remesas_2"), Object.class);
    }

    @Override
    public void initFragment() {

        Gson gson = new Gson();
        String queryData = gson.toJson(data);
        remittanceResponse = gson.fromJson(queryData, TC_RemittanceResponse1.class);

        card_ine            = getView().findViewById(R.id.card_ine);
        card_ife            = getView().findViewById(R.id.card_ife);
        card_pasaporte      = getView().findViewById(R.id.card_pasaporte);
        card_ingreso_manual = getView().findViewById(R.id.card_ingreso_manual);
        btn_cancelar        = getView().findViewById(R.id.btn_cancelar);

        TextView textView = getView().findViewById(R.id.text_title);
        textView.setText(String.format("Recuerda revisar que tienes %s en caja.", Utils.paserCurrency("" + remittanceResponse.getRemesa().getMontoMonedaPago())));

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().setFinishBack(null);
                getContext().backFragment();
            }
        });

        card_ine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_remesas_3.newInstance(data,1));//INE
            }
        });

        card_ife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_remesas_3.newInstance(data,2));//IFE
            }
        });

        card_pasaporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_remesas_3.newInstance(data,3));//PASAPORTE
            }
        });

        card_ingreso_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_remesas_3.newInstance(data,0));
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().alert("¿Desea cancelar el proceso de pago de remesa?", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        getContext().backFragmentWithFunction(function);
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });
            }
        });
    }
}
