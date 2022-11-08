package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IDongleCost;
import com.blm.qiubopay.listeners.IDonglePurchase;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.dongle.QPAY_DongleBuyResponse;
import com.blm.qiubopay.models.dongle.QPAY_DongleCostResponse;
import com.blm.qiubopay.models.rolls.QPAY_RollsCostResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_compra_rollos_1 extends HFragment {

    private static final String PRODUCT_TYPE = "ROLL";

    private View view;
    private MenuActivity context;

    private TextView precio_lector;
    private Button btn_confirmar;

    private String rollCost;

    private QPAY_RollsCostResponse rollsCostResponse;

    private EditSpinner products;
    private ArrayList<String> products_array;

    private int index;

    public static Fragment_compra_rollos_1 newInstance(Object... data) {
        Fragment_compra_rollos_1 fragment = new Fragment_compra_rollos_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_compra_rollos_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_compra_rollos_1() {
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
            rollsCostResponse = new Gson().fromJson(getArguments().getString("Fragment_compra_rollos_1"), QPAY_RollsCostResponse.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_compra_rollos_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    @Override
    public void initFragment(){

        //HLocation.start(false,getContext());

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        // Obtener elementos de pantalla

        precio_lector = view.findViewById(R.id.precio_lector);
        btn_confirmar = view.findViewById(R.id.btn_confirmar);

        btn_confirmar.setEnabled(false);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirmar.setEnabled(false);

                if(userCanBuyDongle(AppPreferences.getKinetoBalance(), rollCost)) {
                    getContext().alert(getContext().getResources().getString(R.string.alert_message_buy_a_roll_pack).replace("**monto**", Utils.paserCurrency(rollCost)), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Si";
                        }

                        @Override
                        public void onClick() {
                            getContext().loading(true);
                            //context.setFragment(Fragment_compra_rollos_2.newInstance(rollsCostResponse.getQpay_object()[index]));
                            rollsCostResponse.getQpay_object()[index].setProduct_type(PRODUCT_TYPE);
                            context.setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.BUY_ROLLS_ADDRESS,rollsCostResponse.getQpay_object()[index]));
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
                }
                else {
                    getContext().alert(R.string.compra_dongle_1_saldo_insuficiente);
                    btn_confirmar.setEnabled(true);
                }
            }
        });

        setProducts();
    }


    public void setProducts(){
        products = view.findViewById(R.id.edit_forma_pago);
        products_array = new ArrayList();
        if (rollsCostResponse != null && rollsCostResponse.getQpay_object() != null) {
            for(int i=0; i<rollsCostResponse.getQpay_object().length; i++){
                products_array.add(rollsCostResponse.getQpay_object()[i].getDescription());
            }
            setDataSpinner2(products, products_array);
        }
    }


    public void setDataSpinner2(final EditSpinner spiner, final ArrayList<String> array){

        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
        spiner.setAdapter(adapter);
        spiner.setEditable(false);
        spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spiner.setText(products_array.get(position));
                precio_lector.setText(Utils.paserCurrency(""+rollsCostResponse.getQpay_object()[position].getCost()) + " MXN");
                rollCost = "" + rollsCostResponse.getQpay_object()[position].getCost();
                index = position;
                btn_confirmar.setEnabled(true);
            }
        });
    }

    /**
     * Verifica si el usuario tiene saldo suficiente para comprar el lector
     * @param balance
     * @param dongleCost
     * @return
     */
    private Boolean userCanBuyDongle(String balance, String dongleCost)
    {
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

}