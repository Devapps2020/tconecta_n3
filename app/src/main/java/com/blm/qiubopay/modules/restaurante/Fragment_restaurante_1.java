package com.blm.qiubopay.modules.restaurante;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.operative.RestaurantDataHelper;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.google.gson.Gson;

public class Fragment_restaurante_1 extends HFragment {
    private View view;
    //private HActivity context;
    private Object data;

    private EditText edit_no_comensales;

    private int commensals;
    private int maxCommensals = 10;

    private HEditText monto;

    private RestaurantDataHelper dataHelper;

    public static Fragment_restaurante_1 newInstance(Object... data) {
        Fragment_restaurante_1 fragment = new Fragment_restaurante_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_restaurante_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //this.context = getContext();//(HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_restaurante_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        commensals = 1;

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_restaurante_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        dataHelper = new RestaurantDataHelper(getContext());

        edit_no_comensales = view.findViewById(R.id.edit_no_comensales);

        ImageView btnMore = view.findViewById(R.id.btnMore);
        ImageView btnMinus= view.findViewById(R.id.btnMinus);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreMinusCommensal(true);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreMinusCommensal(false);
            }
        });

        final Button btn_continuar = view.findViewById(R.id.btn_continuar);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getAmountWithoutFormat().trim().isEmpty()) {
                    if (dataHelper.insertOrder(Double.parseDouble(getAmountWithoutFormat()), Integer.parseInt(edit_no_comensales.getText().toString()))) {
                        getContext().setFragment(Fragment_restaurante_2.newInstance());
                    }
                }
            }
        });

        final Button btn_cancelar = view.findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().backFragment();
            }
        });

        monto = new HEditText((EditText) view.findViewById(R.id.edit_importe),
                true, 11, 1, HEditText.Tipo.MONEDA, new ITextChanged() {
            @Override
            public void onChange() {

                if(monto.isValid())
                    btn_continuar.setEnabled(true);
                else
                    btn_continuar.setEnabled(false);

            }

            @Override
            public void onMaxLength() {
                getContext().hideKeyboard();
            }
        });

        edit_no_comensales.setText(""+commensals);

        //checkOrders();
    }

    private String getAmountWithoutFormat(){
        return monto.getText().replace("$","").replace(",","");
    }

    private void moreMinusCommensal(Boolean more) {
        if(more){
            if(commensals < maxCommensals)
                commensals++;
        }else{
            if(commensals > 1)
                commensals--;
        }
        edit_no_comensales.setText(""+commensals);
    }

    private void checkOrders(){
        if(dataHelper.isAnOpenOrder()){
            Log.d("","");
            getContext().setFragment(Fragment_restaurante_2.newInstance(null));
        }
    }
}
