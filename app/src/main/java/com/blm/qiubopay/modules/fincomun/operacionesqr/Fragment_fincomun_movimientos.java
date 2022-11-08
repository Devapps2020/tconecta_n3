package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_buzon_1;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_cuentas_1;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.FCil.FCUltimosMovimientosRequest;
import mx.com.fincomun.origilib.Http.Response.FCil.FCUltimosMovimientosResponse;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Http.Response.Transferencias.FCTransferenciasResponse;
import mx.com.fincomun.origilib.Model.FCil.UltimosMovimientos;
import mx.com.fincomun.origilib.Objects.FCil.DHMovimiento;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_movimientos extends HFragment implements IMenuContext {

    private List<NotificationDTO> notifications;
    private ArrayList<DHMovimiento> movimientosCuenta = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter_fincomun_movimientos adapterRv;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn_todos, btn_pagos, btn_cobros, btn_info_cuenta;
    private IAccionesMovimientos.Data accionesData;



    private IAccionesMovimientos.GUI accionesMovimientos = new IAccionesMovimientos.GUI() {
        @Override
        public <T> void onItemClick(T itemClicked) {
            getContext().setFragment(Fragment_fincomun_movimiento_detalle.newInstance(new Gson().toJson(itemClicked)));
        }
    };

    public Fragment_fincomun_movimientos() {
    }

    public static Fragment_fincomun_movimientos newInstance(){
        return new Fragment_fincomun_movimientos();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_menu_2, container, false));
    }


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
                        getContextMenu().backFragment();
                    }
                })
                .onClickQuestion(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {

                    }
                });

        consultaMovimientos();
        recyclerView = getView().findViewById(R.id.rv_movimientos);
        layoutManager = new LinearLayoutManager(getContext());
        adapterRv = new Adapter_fincomun_movimientos(movimientosCuenta, accionesMovimientos, getContext());
        accionesData = adapterRv;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterRv);
        adapterRv.notifyDataSetChanged();
        btn_todos = getView().findViewById(R.id.btn_todos);
        btn_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtraInformacion(v.getId());
            }
        });

        btn_pagos = getView().findViewById(R.id.btn_pagos);
        btn_pagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtraInformacion(v.getId());
            }
        });

        btn_cobros = getView().findViewById(R.id.btn_cobros);
        btn_cobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtraInformacion(v.getId());
            }
        });


        btn_info_cuenta = getView().findViewById(R.id.btn_info_cuenta);
        btn_info_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... data) {

                        getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                FCConsultaCuentasResponse response = (FCConsultaCuentasResponse)data[0];
                                SessionApp.getInstance().setCuentasCoDi(response.getCuentas());
                                getContext().setFragment(Fragment_codi_cuentas_1.newInstance());

                            }
                        });
                    }
                });

            }
        });
    }

    private void filtraInformacion(Integer button){
        List<DHMovimiento> filtered = new ArrayList<>();
        accionesData.removeAllItems();
        adapterRv.notifyDataSetChanged();

        switch (button){
            case R.id.btn_todos :
                btn_todos.setBackground(getContext().getDrawable(R.drawable.background_button_dark_enabled));
                btn_pagos.setBackground(getContext().getDrawable(R.drawable.background_button_blue_enabled));
                btn_cobros.setBackground(getContext().getDrawable(R.drawable.background_button_blue_enabled));


                btn_todos.setTextColor(getResources().getColor(R.color.white));
                btn_pagos.setTextColor(getResources().getColor(R.color.FC_grey_1));
                btn_cobros.setTextColor(getResources().getColor(R.color.FC_grey_1));

                accionesData.addList(movimientosCuenta);
            break;

            case R.id.btn_pagos :

                btn_todos.setBackground(getContext().getDrawable(R.drawable.background_button_blue_enabled));
                btn_pagos.setBackground(getContext().getDrawable(R.drawable.background_button_dark_enabled));
                btn_cobros.setBackground(getContext().getDrawable(R.drawable.background_button_blue_enabled));


                btn_todos.setTextColor(getResources().getColor(R.color.FC_grey_1));
                btn_pagos.setTextColor(getResources().getColor(R.color.white));
                btn_cobros.setTextColor(getResources().getColor(R.color.FC_grey_1));

                for (DHMovimiento item : movimientosCuenta){
                    if (item.getNatura().contains("1")){
                        filtered.add(item);
                    }
                }
                accionesData.addList(filtered);
                break;

            case R.id.btn_cobros :
                btn_todos.setBackground(getContext().getDrawable(R.drawable.background_button_blue_enabled));
                btn_pagos.setBackground(getContext().getDrawable(R.drawable.background_button_blue_enabled));
                btn_cobros.setBackground(getContext().getDrawable(R.drawable.background_button_dark_enabled));

                btn_todos.setTextColor(getResources().getColor(R.color.FC_grey_1));
                btn_pagos.setTextColor(getResources().getColor(R.color.FC_grey_1));
                btn_cobros.setTextColor(getResources().getColor(R.color.white));

                for (DHMovimiento item : movimientosCuenta){
                    if (!item.getNatura().contains("1")){
                        filtered.add(item);
                    }
                }
                accionesData.addList(filtered);
                break;
        }
        adapterRv.notifyDataSetChanged();
    }



    private void consultaMovimientos(){



        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... tokens) {

                getContextMenu().gethCoDi().consultaCuentas(tokens[0], new IFunction() {
                    @Override
                    public void execute(Object[] cuentas) {

                        FCConsultaCuentasResponse response = (FCConsultaCuentasResponse)cuentas[0];

                        if (response != null && !response.getCuentas().isEmpty()){

                            getContextMenu().gethCoDi().consultaMovimientos(tokens[0], response.getCuentas().get(0).getNumeroCuenta(), new IFunction() {
                                @Override
                                public void execute(Object[] movimientos) {
                                    FCUltimosMovimientosResponse movimientosResponse = (FCUltimosMovimientosResponse) movimientos[0];
                                    movimientosCuenta = movimientosResponse.getMovimientos();

                                    ArrayList<DHMovimiento> tempList = new ArrayList<DHMovimiento>();
                                    for (int i=0; i <  movimientosCuenta.size(); i++) {
                                        DHMovimiento item =  movimientosCuenta.get(i);
                                        if (item.getDescripcion().toLowerCase().contains("saldo inicial")){
                                            tempList.add(item);
                                        }else if (item.getDescripcion().toLowerCase().contains("saldo final")){
                                            tempList.add(item);
                                        }
                                    }

                                    for (DHMovimiento item: tempList) {
                                        movimientosCuenta.remove(item);
                                    }

                                    filtraInformacion(R.id.btn_todos);
                                    getContext().loading(false);
                                }
                            });
                        }
                    }
                });


            }
        });
    }
}
