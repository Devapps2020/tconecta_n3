package com.blm.qiubopay.modules.fiado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.fiado.QPAY_Cliente;


import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fiado_2 extends HFragment {

    private View view;
    private HActivity context;

    public static Fragment_fiado_2 newInstance(Object... data) {
        Fragment_fiado_2 fragment = new Fragment_fiado_2();
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

        view = inflater.inflate(R.layout.fragment_fiado_2, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){
        CViewMenuTop.create(view)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        LinearLayout ln_new_client = view.findViewById(R.id.ln_new_client);
        LinearLayout ln_cliente_existing = view.findViewById(R.id.ln_client_existing);

        ln_new_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setFragment(Fragment_fiado_4.newInstance());
            }
        });

        ln_cliente_existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_fiado_1.getClientes(true, context,false, new IFunction<List<QPAY_Cliente>>() {
                    @Override
                    public void execute(List<QPAY_Cliente>[] data) {
                        Fragment_fiado_3.option = 3;
                        Fragment_fiado_3.list = data[0];
                        context.setFragment(Fragment_fiado_3.newInstance());
                    }
                });
            }
        });

    }

}