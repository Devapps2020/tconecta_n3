package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.listeners.IDongleCost;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_CashCollectionResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.WSHelper;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_compra_dongle_3 extends HFragment {

    private View view;
    private MenuActivity context;
    private QPAY_CashCollectionResponse data;

    private ListView list_beneficios;


    public static Fragment_compra_dongle_3 newInstance(Object... data) {
        Fragment_compra_dongle_3 fragment = new Fragment_compra_dongle_3();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_compra_dongle_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_compra_dongle_3() {
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

        view = inflater.inflate(R.layout.fragment_compra_dongle_tc_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        // Obtener elementos de pantalla
        list_beneficios = view.findViewById(R.id.list_beneficios);

        // Configurar elementos de pantalla

        String[] benefitsArray = getResources().getStringArray(R.array.benefits_array);
        ArrayAdapter<String> benefitsAdapter = new ArrayAdapter<String>(context,
                R.layout.item_beneficio,
                R.id.text_beneficio,benefitsArray);

        list_beneficios.setAdapter(benefitsAdapter);

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


    /**
     * Permite recuperar el precio del lector
     */
    private void recuperarPrecioDongle() {

        context.loading(true);

        try {

            IDongleCost sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {

                        context.alert(R.string.general_error, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                context.initHome();
                            }
                        });

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }
                        @Override
                        public void onClick() {
                            context.initHome();
                        }
                    });

                }
            }, context);

            sale.doGetDongleCost();

        } catch (Exception e) {

            context.loading(false);
            e.printStackTrace();

            context.alert(R.string.general_error_catch, new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    context.initHome();
                }
            });

        }

    }


}