package com.blm.qiubopay.modules;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.models.QPAY_CashCollectionResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_compra_dongle_1 extends HFragment {

    public static String costo;

    private View view;
    private MenuActivity context;
    private QPAY_CashCollectionResponse data;

    //private ListView list_beneficios;
    private TextView text_description;
    private TextView precio_lector;
    private TextView text_more;
    private Button btn_confirmar;

    public static Fragment_compra_dongle_1 newInstance(Object... data) {
        Fragment_compra_dongle_1 fragment = new Fragment_compra_dongle_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_compra_dongle_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_compra_dongle_1() {
        // Required empty public constructor
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
            data = new Gson().fromJson(getArguments().getString("Fragment_compra_dongle_1"), QPAY_CashCollectionResponse.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_compra_dongle_tc_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        //HLocation.start(false,getContext());

        precio_lector = view.findViewById(R.id.precio_lector);
        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        text_description = view.findViewById(R.id.text_description);
        text_more = view.findViewById(R.id.text_more);

        precio_lector.setText(Utils.paserCurrency(costo) + "MXN");

        text_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(Fragment_compra_dongle_3.newInstance());
            }
        });


        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirmar.setEnabled(false);

                if(userCanBuyDongle(AppPreferences.getKinetoBalance(), costo)) {
                    context.alert(context.getResources().getString(R.string.alert_message_buy_a_dongle).replace("**monto**", Utils.paserCurrency(costo)), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Si";
                        }

                        @Override
                        public void onClick() {
                            getContext().loading(true);
                            //context.setFragment(Fragment_compra_dongle_2.newInstance());
                            context.setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.BUY_ROLLS_ADDRESS));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_confirmar.setEnabled(true);
                                }
                            }, 4000);
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "No";
                        }

                        @Override
                        public void onClick() {
                            btn_confirmar.setEnabled(true);
                        }
                    });

                } else {
                    context.alert(R.string.compra_dongle_1_saldo_insuficiente);
                    btn_confirmar.setEnabled(true);
                }
            }
        });

        btn_confirmar.setEnabled(true);

        tacharPrecio();

    }

    /**
     * Verifica si el usuario tiene saldo suficiente para comprar el lector
     * @param balance
     * @param dongleCost
     * @return
     */
    private Boolean userCanBuyDongle(String balance, String dongleCost) {

        Boolean back = true;
        try
        {
            Double userBalance = new Double(Tools.getOnlyNumbers(balance));
            Double donglePrice = new Double(Tools.getOnlyNumbers(dongleCost));
            if(donglePrice > userBalance)
                back = false;
        }catch (Exception e)
        {
            back = false;
        }
        return back;
    }

    public void tacharPrecio() {

         TextView textView1 = getView().findViewById(R.id.oldPrice);
        textView1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // subrayado

    }



}