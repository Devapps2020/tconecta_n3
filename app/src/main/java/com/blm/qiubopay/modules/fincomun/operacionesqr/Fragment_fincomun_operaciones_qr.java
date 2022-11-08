package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_cuentas_1;
import com.blm.qiubopay.utils.SessionApp;

import java.text.NumberFormat;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_operaciones_qr extends HFragment implements IMenuContext {


    public Fragment_fincomun_operaciones_qr() {
    }

    public static Fragment_fincomun_operaciones_qr newInstance(){
        return new Fragment_fincomun_operaciones_qr();
    }

    private enum TAB{
        TAB_1,
        TAB_2
    }

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout tab_1, tab_2;
    private FrameLayout main_fragment;
    private HFragment fragmentAcciones, fragmentMovimientos;
    private View line_1 , line_2;
    private TextView saldo_textview, tv_detalle_cuenta;
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_white)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().initHome();
                    }
                })
                .onClickQuestion(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {

                    }
                });

        initView();
        tab_1 = getView().findViewById(R.id.tab_1);
        line_1  = getView().findViewById(R.id.line_1);

        tab_1.setOnClickListener(i -> {
            switchingTab (TAB.TAB_1);
        });


        tab_2 = getView().findViewById(R.id.tab_2);
        line_2 = getView().findViewById(R.id.line_2);

        tab_2.setOnClickListener(i -> {
            switchingTab(TAB.TAB_2);
        });

        tv_detalle_cuenta = getView().findViewById(R.id.tv_detalle_cuenta);
        tv_detalle_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... data) {
                        if (data!= null) {
                            getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    FCConsultaCuentasResponse response = (FCConsultaCuentasResponse) data[0];
                                    SessionApp.getInstance().setCuentasCoDi(response.getCuentas());
                                    getContext().setFragment(Fragment_codi_cuentas_1.newInstance());

                                }
                            });
                        }
                    }
                });
            }
        });

        saldo_textview = getView().findViewById(R.id.saldo_textview);
        consultaSaldo();
        switchingTab(TAB.TAB_1);
    }

    private void initView(){

        fragmentAcciones = Fragment_fincomun_acciones_qr.newInstance();
        fragmentMovimientos = Fragment_fincomun_movimientos.newInstance();

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //main_fragment = getView().findViewById(R.id.main_fragment);

        fragmentTransaction.add(R.id.main_fragment, fragmentAcciones);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_menu, container, false),R.drawable.background_splash_header_1);
    }


    private void switchingTab(TAB tab){
        switch (tab){
            case TAB_1:
                    tab_1.setBackgroundColor(getResources().getColor(R.color.white));
                    line_1.setBackgroundColor(getResources().getColor(R.color.clear_blue));

                    tab_2.setBackgroundColor(getResources().getColor(R.color.white_two));
                    line_2.setBackgroundColor(getResources().getColor(R.color.powder_blue));

                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_fragment, fragmentAcciones);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                break;
            case TAB_2:
                    tab_2.setBackgroundColor(getResources().getColor(R.color.white));
                    line_2.setBackgroundColor(getResources().getColor(R.color.clear_blue));

                    tab_1.setBackgroundColor(getResources().getColor(R.color.white_two));
                    line_1.setBackgroundColor(getResources().getColor(R.color.powder_blue));

                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.main_fragment, fragmentMovimientos);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                break;
        }
    }


    private void consultaSaldo(){


        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... data) {
                if (data != null) {
                    getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                        @Override
                        public void execute(Object[] data) {

                            FCConsultaCuentasResponse response = (FCConsultaCuentasResponse) data[0];
                            String saldo = currencyFormatter.format(Double.valueOf(response.getCuentas().get(0).getSaldo()));
                            saldo_textview.setText(saldo);
                            initView();
                        }
                    });
                }
            }
        });
    }

}
